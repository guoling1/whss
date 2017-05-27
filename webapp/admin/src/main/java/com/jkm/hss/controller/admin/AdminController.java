package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.admin.entity.*;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.enums.EnumIsMaster;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.admin.helper.requestparam.*;
import com.jkm.hss.admin.helper.responseparam.*;
import com.jkm.hss.admin.service.AdminRoleService;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumOemType;
import com.jkm.hss.dealer.enums.EnumRecommendBtn;
import com.jkm.hss.dealer.helper.DealerConsts;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerAdd2Request;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerAddRequest;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerUpdate2Request;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerUpdateRequest;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.*;
import com.jkm.hss.helper.response.DistributeQRCodeResponse;
import com.jkm.hss.helper.response.DistributeRangeQRCodeResponse;
import com.jkm.hss.helper.response.FirstLevelDealerAddResponse;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.helper.ValidationUtil;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/user")
public class AdminController extends BaseController {
    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerRateService dealerRateService;

    @Autowired
    private BankCardBinService bankCardBinService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductChannelDetailService productChannelDetailService;

    @Autowired
    private DealerChannelRateService dealerChannelRateService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private AdminRoleService adminRoleService;

    /**
     * 登录
     *
     * @param adminUserLoginRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResponse login(@RequestBody final AdminUserLoginRequest adminUserLoginRequest, final HttpServletResponse response) {
        final String _username = StringUtils.trimToEmpty(adminUserLoginRequest.getUsername());
        final String _password = StringUtils.trimToEmpty(adminUserLoginRequest.getPassword());
        if (!(_username.length() >= 4 && _username.length() <= 16)) {
            return CommonResponse.simpleResponse(-1, "登录名长度4-16位");
        }
        if (!(_password.length() >= 6 && _password.length() <= 32)) {
            return CommonResponse.simpleResponse(-1, "密码长度6-32位");
        }

        final Optional<AdminUser> userOptional = this.adminUserService.getAdminUserByNameAndType(_username,EnumAdminType.BOSS.getCode());
        if (!userOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "登陆用户不存在");
        }

        if (!userOptional.get().isActive()) {
            return CommonResponse.simpleResponse(-1, "用户被禁用");
        }

        final Optional<AdminUserPassport> tokenOptional = this.adminUserService.login(_username, _password);
        if (!tokenOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "登录名或密码错误");
        }
        this.adminUserService.updateLastLoginDate(tokenOptional.get().getAuid());
        CookieUtil.setSessionCookie(response, ApplicationConsts.ADMIN_COOKIE_KEY, tokenOptional.get().getToken(),
                ApplicationConsts.getApplicationConfig().domain(), (int)(DealerConsts.TOKEN_EXPIRE_MILLIS / 1000));
        List<AdminUserLoginResponse> loginMenu = this.adminRoleService.getLoginMenu(userOptional.get().getRoleId(),EnumAdminType.BOSS.getCode(),userOptional.get().getIsMaster());
        String roleName = "";
        if(userOptional.get().getRoleId()<=0){
            roleName = "超级管理员";
        }else{
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectById(userOptional.get().getRoleId());
            roleName = adminRoleOptional.get().getRoleName();
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, roleName,loginMenu);
    }

    /**
     * 退出登录
     *
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout")
    public CommonResponse logout(final HttpServletResponse response) {
        this.adminUserService.logout(getAdminUser().getId());
        CookieUtil.deleteCookie(response, ApplicationConsts.ADMIN_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登出成功");
    }

    /**
     * 给一级代理商分配码段
     *
     * @param distributeQRCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeQRCode", method = RequestMethod.POST)
    public CommonResponse distributeQRCode(@RequestBody DistributeQRCodeRequest distributeQRCodeRequest) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeQRCodeRequest.getDealerId());
        if(!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        if (distributeQRCodeRequest.getCount() <= 0) {
            return CommonResponse.simpleResponse(-1, "分配个数不可以是0");
        }
        if (StringUtils.isBlank(distributeQRCodeRequest.getSysType())) {
            return CommonResponse.simpleResponse(-1, "请选择所属项目");
        }
        final Dealer dealer = dealerOptional.get();
        final Triple<Integer, String, List<Pair<QRCode, QRCode>>> resultTriple = this.adminUserService.distributeQRCode(super.getAdminUser().getId(),
                distributeQRCodeRequest.getDealerId(), distributeQRCodeRequest.getCount(),distributeQRCodeRequest.getSysType());
        if (1 != resultTriple.getLeft()) {
            return CommonResponse.simpleResponse(-1, resultTriple.getMiddle());
        }
        final List<Pair<QRCode, QRCode>> codePairs = resultTriple.getRight();
        final DistributeQRCodeResponse distributeQRCodeResponse = new DistributeQRCodeResponse();
        distributeQRCodeResponse.setDealerId(distributeQRCodeRequest.getDealerId());
        distributeQRCodeResponse.setName(dealer.getProxyName());
        distributeQRCodeResponse.setMobile(dealer.getMobile());
        distributeQRCodeResponse.setDistributeDate(new Date());
        distributeQRCodeResponse.setCount(distributeQRCodeRequest.getCount());
        final ArrayList<DistributeQRCodeResponse.Code> codes = new ArrayList<>();
        for (Pair<QRCode, QRCode> pair : codePairs) {
            final DistributeQRCodeResponse.Code code = distributeQRCodeResponse.new Code();
            code.setStartCode(pair.getLeft().getCode());
            code.setEndCode(pair.getRight().getCode());
            codes.add(code);
        }
        distributeQRCodeResponse.setCodes(codes);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distributeQRCodeResponse);
    }


    /**
     * 按码段范围给一级代理商分配码段
     *
     * @param distributeRangeQRCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeRangeQRCode", method = RequestMethod.POST)
    public CommonResponse distributeRangeQRCode(@RequestBody DistributeRangeQRCodeRequest distributeRangeQRCodeRequest) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeRangeQRCodeRequest.getDealerId());
        if(!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        if (StringUtils.isBlank(distributeRangeQRCodeRequest.getSysType())) {
            return CommonResponse.simpleResponse(-1, "请选择所属项目");
        }
        final Dealer dealer = dealerOptional.get();
        final List<Pair<QRCode, QRCode>> pairs = this.adminUserService.distributeRangeQRCode(distributeRangeQRCodeRequest.getDealerId(),
                distributeRangeQRCodeRequest.getStartCode(), distributeRangeQRCodeRequest.getEndCode(),distributeRangeQRCodeRequest.getSysType());
        if (CollectionUtils.isEmpty(pairs)) {
            return CommonResponse.simpleResponse(-1, "此范围不存在可分配的二维码");
        }
        final DistributeRangeQRCodeResponse distribute = new DistributeRangeQRCodeResponse();
        distribute.setDealerId(dealer.getId());
        distribute.setName(dealer.getProxyName());
        distribute.setMobile(dealer.getMobile());
        distribute.setDistributeDate(new Date());
        final ArrayList<DistributeRangeQRCodeResponse.Code> codes = new ArrayList<>();
        int count = 0;
        for (Pair<QRCode, QRCode> pair : pairs) {
            final DistributeRangeQRCodeResponse.Code code = distribute.new Code();
            code.setStartCode(pair.getLeft().getCode());
            code.setEndCode(pair.getRight().getCode());
            count += (int) (Long.valueOf(pair.getRight().getCode()) - Long.valueOf(pair.getLeft().getCode()) + 1);
            codes.add(code);
        }
        distribute.setCount(count);
        distribute.setCodes(codes);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distribute);
    }


    /**
     *码段查询
     */
    @ResponseBody
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    public CommonResponse getCode(@RequestBody CodeQueryRequest code) {
        CodeQueryResponse codeQueryResponse = adminUserService.getCode(code.getCode());
        if (codeQueryResponse==null){
            return CommonResponse.simpleResponse(-1, "未查询到与之匹配的信息。");
        }

        if (codeQueryResponse.getActivateStatus()==2){

            if (codeQueryResponse.getFirstLevelDealerId()==0){
                String jkm="金开门";
                codeQueryResponse.setJkm(jkm);
            }
            if (codeQueryResponse.getFirstLevelDealerId()>0){
                CodeQueryResponse res = adminUserService.getProxyName(codeQueryResponse.getFirstLevelDealerId());
                codeQueryResponse.setProxyName(res.getProxyName());
            }
            if (codeQueryResponse.getSecondLevelDealerId()>0){
                CodeQueryResponse res1 =adminUserService.getProxyName1(codeQueryResponse.getSecondLevelDealerId());
                codeQueryResponse.setProxyName1(res1.getProxyName());
            }
            if (codeQueryResponse.getMerchantId()>0){
                CodeQueryResponse getName =adminUserService.getMerchantName(codeQueryResponse.getMerchantId());
                codeQueryResponse.setMerchantName(getName.getMerchantName());
            }
        }
        if (codeQueryResponse.getDistributeStatus()==1&&codeQueryResponse.getActivateStatus()==1){
            return CommonResponse.simpleResponse(-1, "该码未被注册且未被分配，该码可用。");
        }
        if (codeQueryResponse.getDistributeStatus()==2&&codeQueryResponse.getActivateStatus()==1){

            if (codeQueryResponse.getFirstLevelDealerId()>0){
                CodeQueryResponse res = adminUserService.getProxyName(codeQueryResponse.getFirstLevelDealerId());
                codeQueryResponse.setProxyName(res.getProxyName());
            }
            if (codeQueryResponse.getSecondLevelDealerId()>0){
                CodeQueryResponse res1 =adminUserService.getProxyName1(codeQueryResponse.getSecondLevelDealerId());
                codeQueryResponse.setProxyName1(res1.getProxyName());
            }

            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "该码已被分配但未注册，该码可用。", codeQueryResponse);
        }

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", codeQueryResponse);
    }


    /**
     * 添加一级代理商
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addFirstDealer2", method = RequestMethod.POST)
    public CommonResponse addFirstDealer2(@RequestBody final FirstLevelDealerAdd2Request firstLevelDealerAdd2Request) {
        try{
            if(!ValidateUtils.isMobile(firstLevelDealerAdd2Request.getMobile())) {
                return CommonResponse.simpleResponse(-1, "手机号格式错误");
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getByMobile(firstLevelDealerAdd2Request.getMobile());
            if (dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "手机号已经注册");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getName())) {
                return CommonResponse.simpleResponse(-1, "名称不能为空");
            }
            final long proxyNameCount = this.dealerService.selectByProxyNameAndOemType(firstLevelDealerAdd2Request.getName(),firstLevelDealerAdd2Request.getOemType());
            if (proxyNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "名称已经存在");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getLoginName())) {
                return CommonResponse.simpleResponse(-1, "登录名不能为空");
            }
            Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndType(firstLevelDealerAdd2Request.getLoginName(),EnumAdminType.FIRSTDEALER.getCode());
            if (adminUserOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "登录名已经存在");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getLoginPwd())) {
                return CommonResponse.simpleResponse(-1, "登录密码不能为空");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getEmail())) {
                return CommonResponse.simpleResponse(-1, "联系邮箱不能为空");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getBelongProvinceCode())) {
                return CommonResponse.simpleResponse(-1, "所在省份编码不能为空");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getBelongProvinceName())) {
                return CommonResponse.simpleResponse(-1, "所在省份不能为空");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getBelongCityCode())) {
                return CommonResponse.simpleResponse(-1, "所在市编码不能为空");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getBelongCityName())) {
                return CommonResponse.simpleResponse(-1, "所在市不能为空");
            }
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getBelongArea())) {
                return CommonResponse.simpleResponse(-1, "详细地址不能为空");
            }
            final String bankCard = firstLevelDealerAdd2Request.getBankCard();
            final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankCard);
            if (!bankCardBinOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "结算卡格式错误");
            }
            firstLevelDealerAdd2Request.setBankName(bankCardBinOptional.get().getBankName());
            if(StringUtils.isBlank(firstLevelDealerAdd2Request.getBankAccountName())) {
                return CommonResponse.simpleResponse(-1, "开户名称不能为空");
            }
            if(!ValidationUtil.isIdCard(firstLevelDealerAdd2Request.getIdCard())){
                return CommonResponse.simpleResponse(-1, "身份证格式不正确");
            }
            if(!ValidateUtils.isMobile(firstLevelDealerAdd2Request.getBankReserveMobile())) {
                return CommonResponse.simpleResponse(-1, "开户手机号格式错误");
            }
            //设置为代理商类型
            firstLevelDealerAdd2Request.setOemId(0);
            final long dealerId = this.dealerService.createFirstDealer2(firstLevelDealerAdd2Request);

            //创建登录用户
            AdminUser adminUser = new AdminUser();
            adminUser.setUsername(firstLevelDealerAdd2Request.getLoginName());
            adminUser.setSalt("100000");
            adminUser.setPassword(DealerSupport.passwordDigest(firstLevelDealerAdd2Request.getLoginPwd(),"JKM"));
            adminUser.setRealname(firstLevelDealerAdd2Request.getBankAccountName());
            adminUser.setEmail(firstLevelDealerAdd2Request.getEmail());
            adminUser.setMobile(AdminUserSupporter.encryptMobile(firstLevelDealerAdd2Request.getMobile()));
            adminUser.setCompanyId("");
            adminUser.setDeptId("");
            adminUser.setIdCard(AdminUserSupporter.encryptIdenrity(firstLevelDealerAdd2Request.getIdCard()));
            adminUser.setRoleId(0l);
            adminUser.setIdentityFacePic("");
            adminUser.setIdentityOppositePic("");
            if(firstLevelDealerAdd2Request.getOemType()==1){//分公司
                adminUser.setType(EnumAdminType.OEM.getCode());
            }else{
                adminUser.setType(EnumAdminType.FIRSTDEALER.getCode());
            }
            adminUser.setDealerId(dealerId);
            adminUser.setIsMaster(EnumIsMaster.MASTER.getCode());
            adminUser.setStatus(EnumAdminUserStatus.NORMAL.getCode());
            this.adminUserService.createFirstDealerUser(adminUser,firstLevelDealerAdd2Request.getOemType());
            final FirstLevelDealerAddResponse firstLevelDealerAddResponse = new FirstLevelDealerAddResponse();
            firstLevelDealerAddResponse.setDealerId(dealerId);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "添加成功", firstLevelDealerAddResponse);
        }catch (Exception e){
            log.error("错误信息是",e.getMessage());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }


    /**
     * 更新一级代理商
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateDealer2", method = RequestMethod.POST)
    public CommonResponse updateDealer2(@RequestBody FirstLevelDealerUpdate2Request request) {
        try{
            if(!ValidateUtils.isMobile(request.getMobile())) {
                return CommonResponse.simpleResponse(-1, "手机号格式错误");
            }
            Optional<Dealer> dealerOptional1 = dealerService.getById(request.getDealerId());
            if(!dealerOptional1.isPresent()){
                return CommonResponse.simpleResponse(-1, "代理商或公司不存在");
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getByMobileUnIncludeNow(request.getMobile(), request.getDealerId());
            if (dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "手机号已经注册");
            }
            if(StringUtils.isBlank(request.getName())) {
                return CommonResponse.simpleResponse(-1, "名称不能为空");
            }
            long proxyNameCount = this.dealerService.getByProxyNameUnIncludeNow(request.getName(),dealerOptional1.get().getOemType(), request.getDealerId());
            if (proxyNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "名称已经存在");
            }
            if(StringUtils.isBlank(request.getLoginName())) {
                return CommonResponse.simpleResponse(-1, "登录名不能为空");
            }
            Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndTypeUnIncludeNow(request.getLoginName(),EnumAdminType.FIRSTDEALER.getCode(),request.getDealerId());
            if (adminUserOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "登录名已经存在");
            }
            if(StringUtils.isBlank(request.getEmail())) {
                return CommonResponse.simpleResponse(-1, "联系邮箱不能为空");
            }
            if(StringUtils.isBlank(request.getBelongProvinceCode())) {
                return CommonResponse.simpleResponse(-1, "所在省份编码不能为空");
            }
            if(StringUtils.isBlank(request.getBelongProvinceName())) {
                return CommonResponse.simpleResponse(-1, "所在省份不能为空");
            }
            if(StringUtils.isBlank(request.getBelongCityCode())) {
                return CommonResponse.simpleResponse(-1, "所在市编码不能为空");
            }
            if(StringUtils.isBlank(request.getBelongCityName())) {
                return CommonResponse.simpleResponse(-1, "所在市不能为空");
            }
            if(StringUtils.isBlank(request.getBelongArea())) {
                return CommonResponse.simpleResponse(-1, "详细地址不能为空");
            }
            final String bankCard = request.getBankCard();
            final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankCard);
            if (!bankCardBinOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "结算卡格式错误");
            }
            request.setBankName(bankCardBinOptional.get().getBankName());

            if(StringUtils.isBlank(request.getBankAccountName())) {
                return CommonResponse.simpleResponse(-1, "开户名称不能为空");
            }
            if(!ValidationUtil.isIdCard(request.getIdCard())){
                return CommonResponse.simpleResponse(-1, "身份证格式不正确");
            }
            if(!ValidateUtils.isMobile(request.getBankReserveMobile())) {
                return CommonResponse.simpleResponse(-1, "开户手机号格式错误");
            }
            this.dealerService.updateDealer2(request);

            //更改登录用户
            AdminUser adminUser = new AdminUser();
            adminUser.setEmail(request.getEmail());
            adminUser.setUsername(request.getLoginName());
            adminUser.setRealname(request.getBankAccountName());
            adminUser.setMobile(AdminUserSupporter.encryptMobile(request.getMobile()));
            adminUser.setIdCard(AdminUserSupporter.encryptIdenrity(request.getIdCard()));
            adminUser.setDealerId(request.getDealerId());
            adminUser.setIsMaster(EnumIsMaster.MASTER.getCode());
            this.adminUserService.updateDealerUser(adminUser);
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "修改成功")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }


    /**
     * 分配二维码
     * @param distributeQrCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeQrCodeToDealer", method = RequestMethod.POST)
    public CommonResponse distributeQrCodeToDealer (@RequestBody DistributeQrCode2Request distributeQrCodeRequest) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeQrCodeRequest.getDealerId());
        if(!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        Preconditions.checkState(dealerOptional.get().getLevel() == EnumDealerLevel.FIRST.getId(), "只能给一级代理分配二维码");
        if (StringUtils.isBlank(distributeQrCodeRequest.getSysType())) {
            return CommonResponse.simpleResponse(-1, "请选择产品");
        }
        if(distributeQrCodeRequest.getType()!= EnumQRCodeDistributeType.ENTITYCODE.getCode()
                &&distributeQrCodeRequest.getType()!= EnumQRCodeDistributeType.ELECTRONICCODE.getCode()){
            return CommonResponse.simpleResponse(-1, "请选择类型");
        }
        //判断是否有权限
        Optional<Product> productOptional = productService.selectByType(distributeQrCodeRequest.getSysType());
        long productId = productOptional.get().getId();
        List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),productId);
        if(dealerChannelRateList==null||dealerChannelRateList.size()==0){
            return CommonResponse.simpleResponse(-1, "未开通此产品");
        }
        List<DistributeQRCodeRecord> distributeQRCodeRecords = new ArrayList<DistributeQRCodeRecord>();
        if(distributeQrCodeRequest.getDistributeType()==1){//按码段
            distributeQRCodeRecords = this.adminUserService.distributeQRCodeByCode(distributeQrCodeRequest.getType(),distributeQrCodeRequest.getSysType(),
                    distributeQrCodeRequest.getDealerId(), distributeQrCodeRequest.getStartCode(),distributeQrCodeRequest.getEndCode(),super.getAdminUser().getId());
        }
        if(distributeQrCodeRequest.getDistributeType()==2){//按个数
            if (distributeQrCodeRequest.getCount() <= 0) {
                return CommonResponse.simpleResponse(-1, "分配个数不可以是0");
            }
            distributeQRCodeRecords = this.adminUserService.distributeQRCodeByCount(distributeQrCodeRequest.getType(),distributeQrCodeRequest.getSysType(),
                    distributeQrCodeRequest.getDealerId(), distributeQrCodeRequest.getCount(),super.getAdminUser().getId());
        }
        if(distributeQRCodeRecords.size()<=0){
            return CommonResponse.simpleResponse(-1, "二维码数量不足");
        }
        List<DistributeQRCodeRecordResponse> distributeQRCodeRecordResponseList = new ArrayList<DistributeQRCodeRecordResponse>();
        for(int i=0;i<distributeQRCodeRecords.size();i++){
            DistributeQRCodeRecordResponse distributeQRCodeRecordResponse = new DistributeQRCodeRecordResponse();
            distributeQRCodeRecordResponse.setDealerName(dealerOptional.get().getProxyName());
            distributeQRCodeRecordResponse.setDealerMobile(dealerOptional.get().getMobile());
            distributeQRCodeRecordResponse.setDistributeTime(distributeQRCodeRecords.get(i).getCreateTime());
            distributeQRCodeRecordResponse.setType(distributeQRCodeRecords.get(i).getType());
            distributeQRCodeRecordResponse.setCount(distributeQRCodeRecords.get(i).getCount());
            distributeQRCodeRecordResponse.setStartCode(distributeQRCodeRecords.get(i).getStartCode());
            distributeQRCodeRecordResponse.setEndCode(distributeQRCodeRecords.get(i).getEndCode());
            distributeQRCodeRecordResponseList.add(distributeQRCodeRecordResponse);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distributeQRCodeRecordResponseList);
    }

    /**
     * 剩余二维码个数
     * @param unDistributeCountRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unDistributeCount", method = RequestMethod.POST)
    public CommonResponse unDistributeCount (@RequestBody UnDistributeCountRequest unDistributeCountRequest) {
        int count = this.adminUserService.unDistributeCount(unDistributeCountRequest.getSysType());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", count);
    }

    /**
     * 分配二维码记录
     * @param distributeRecordRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeRecord", method = RequestMethod.POST)
    public CommonResponse distributeRecord (@RequestBody DistributeQrCodeRequest distributeRecordRequest) {
        PageModel<BossDistributeQRCodeRecordResponse> bossDistributeQRCodeRecordResponse = this.dealerService.distributeRecord(distributeRecordRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", bossDistributeQRCodeRecordResponse);
    }


    //    员工管理

    /**
     * 新增员工
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public CommonResponse addUser (@RequestBody AdminUserRequest adminUserRequest) {
        AdminUser adminUser = new AdminUser();
        final Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndType(adminUserRequest.getUsername(), EnumAdminType.BOSS.getCode());
        if(adminUserOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "登录名已存在");
        }
        if (!ValidateUtils.isMobile(adminUserRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (!ValidateUtils.isIDCard(adminUserRequest.getIdCard())) {
            return CommonResponse.simpleResponse(-1, "身份证号格式错误");
        }
        if (!ValidateUtils.isEmail(adminUserRequest.getEmail())) {
            return CommonResponse.simpleResponse(-1, "邮箱格式错误");
        }
        if (!(adminUserRequest.getUsername().length() >= 4 && adminUserRequest.getUsername().length() <= 16)) {
            return CommonResponse.simpleResponse(-1, "用户名长度4-16位");
        }
        if (!(adminUserRequest.getPassword().length() >= 6 && adminUserRequest.getPassword().length() <= 32)) {
            return CommonResponse.simpleResponse(-1, "密码长度6-32位");
        }
        adminUser.setUsername(adminUserRequest.getUsername());
        adminUser.setPassword(adminUserRequest.getPassword());
        adminUser.setCompanyId(adminUserRequest.getCompanyId());
        adminUser.setDeptId(adminUserRequest.getDeptId());
        adminUser.setRealname(adminUserRequest.getRealname());
        adminUser.setIdCard(adminUserRequest.getIdCard());
        adminUser.setIdentityFacePic(adminUserRequest.getIdentityFacePic());
        adminUser.setIdentityOppositePic(adminUserRequest.getIdentityOppositePic());
        adminUser.setMobile(adminUserRequest.getMobile());
        adminUser.setEmail(adminUserRequest.getEmail());
        adminUser.setIsMaster(EnumIsMaster.NOTMASTER.getCode());
        adminUser.setDealerId(0);
        adminUser.setRoleId(adminUserRequest.getRoleId());
        long userId = this.adminUserService.createUser(adminUser);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "新增成功",userId);
    }
    /**
     * 禁用用户
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableUser", method = RequestMethod.POST)
    public CommonResponse disableUser (final HttpServletResponse response,@RequestBody AdminUserRequest adminUserRequest) {
        adminUserService.disableUser(adminUserRequest.getId());
        if(super.getAdminUser().getId()==adminUserRequest.getId()){//禁用自己登出
            this.adminUserService.logout(getAdminUser().getId());
            CookieUtil.deleteCookie(response, ApplicationConsts.ADMIN_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "禁用成功");
    }
    /**
     * 启用用户
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activeUser", method = RequestMethod.POST)
    public CommonResponse activeUser (@RequestBody AdminUserRequest adminUserRequest) {
        adminUserService.activeUser(adminUserRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "启用成功");
    }
    /**
     * 修改密码
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public CommonResponse updatePwd (@RequestBody AdminUserRequest adminUserRequest) {
        if(adminUserRequest.getId()<=0){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        if(StringUtils.isEmpty(adminUserRequest.getPassword())){
            return CommonResponse.simpleResponse(-1, "请输入密码");
        }
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserById(adminUserRequest.getId());
        if(!adminUserOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        adminUserService.updatePwd(adminUserRequest.getPassword(),adminUserRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
    }

    /**
     * 编辑用户
     * @param adminUser
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public CommonResponse updateUser (@RequestBody AdminUser adminUser) {
        if(adminUser.getId()<=0){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserById(adminUser.getId());
        if(!adminUserOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        final long proxyNameCount = this.adminUserService.selectByUsernameAndTypeUnIncludeNow(adminUser.getUsername(),EnumAdminType.BOSS.getCode(), adminUser.getId());
        if (proxyNameCount > 0) {
            return CommonResponse.simpleResponse(-1, "登录名已经存在");
        }
        adminUserService.update(adminUser);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
    }
    /**
     * 用户列表
     * @param adminUserListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public CommonResponse userList (@RequestBody AdminUserListRequest adminUserListRequest) {
        adminUserListRequest.setType(EnumAdminType.BOSS.getCode());
        PageModel<AdminUserListResponse> adminUserPageModel = adminUserService.userList(adminUserListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserPageModel);
    }

    /**
     * 员工详情
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userDetail/{userId}", method = RequestMethod.GET)
    public CommonResponse userDetail (@PathVariable final long userId) {
        Optional<AdminUser> adminUserOptional = adminUserService.getAdminUserById(userId);
        if(!adminUserOptional.isPresent()){
            return  CommonResponse.simpleResponse(-1,"该员工不存在");
        }
        Date expiration = new Date(new Date().getTime() + 30*60*1000);
        AdminUserResponse adminUserResponse = new AdminUserResponse();
        if(adminUserOptional.get().getMobile()!=null&&!"".equals(adminUserOptional.get().getMobile())){
            adminUserResponse.setMobile(AdminUserSupporter.decryptMobile(userId,adminUserOptional.get().getMobile()));
        }
        if(adminUserOptional.get().getIdCard()!=null&&!"".equals(adminUserOptional.get().getIdCard())){
            adminUserResponse.setIdCard(AdminUserSupporter.decryptIdentity(adminUserOptional.get().getIdCard()));
        }
        if(adminUserOptional.get().getIdentityFacePic()!=null&&!"".equals(adminUserOptional.get().getIdentityFacePic())){
            adminUserResponse.setIdentityFacePic(adminUserOptional.get().getIdentityFacePic());
            URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), adminUserResponse.getIdentityFacePic(),expiration);
            adminUserResponse.setRealIdentityFacePic(url.toString());
        }
        if(adminUserOptional.get().getIdentityOppositePic()!=null&&!"".equals(adminUserOptional.get().getIdentityOppositePic())){
            adminUserResponse.setIdentityOppositePic(adminUserOptional.get().getIdentityOppositePic());
            URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), adminUserResponse.getIdentityOppositePic(),expiration);
            adminUserResponse.setRealIdentityOppositePic(url.toString());
        }
        adminUserResponse.setId(adminUserOptional.get().getId());
        adminUserResponse.setStatus(adminUserOptional.get().getStatus());
        adminUserResponse.setUsername(adminUserOptional.get().getUsername());
        adminUserResponse.setRealname(adminUserOptional.get().getRealname());
        adminUserResponse.setEmail(adminUserOptional.get().getEmail());
        adminUserResponse.setCompanyId(adminUserOptional.get().getCompanyId());
        adminUserResponse.setDeptId(adminUserOptional.get().getDeptId());
        adminUserResponse.setRoleId(adminUserOptional.get().getRoleId());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserResponse);
    }

    /**
     * 角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userRoleList", method = RequestMethod.POST)
    public CommonResponse userRoleList () {
        List<AdminRoleListResponse> adminRoleListResponses = adminRoleService.selectAdminRoleList(EnumAdminType.BOSS.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", adminRoleListResponses);
    }

    /**
     * 角色详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRoleDetail", method = RequestMethod.POST)
    public CommonResponse getRoleDetail (@RequestBody AdminRoleDetailRequest adminRoleDetailRequest) {
        RoleDetailResponse roleDetailResponse = new RoleDetailResponse();
        if(adminRoleDetailRequest.getId()>0){
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectById(adminRoleDetailRequest.getId());
            if(!adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色不存在");
            }
            roleDetailResponse.setRoleId(adminRoleDetailRequest.getId());
            roleDetailResponse.setRoleName(adminRoleOptional.get().getRoleName());
        }
        List<AdminMenuOptRelListResponse> adminMenuOptRelListResponses = adminRoleService.getPrivilege(EnumAdminType.BOSS.getCode(),adminRoleDetailRequest.getId());
        roleDetailResponse.setList(adminMenuOptRelListResponses);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",roleDetailResponse);
    }

    /**
     * 添加或修改角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveRole", method = RequestMethod.POST)
    public CommonResponse saveRole (@RequestBody RoleDetailRequest roleDetailRequest) {
        if(roleDetailRequest.getRoleName()==null||"".equals(roleDetailRequest.getRoleName())){
            return CommonResponse.simpleResponse(-1, "请输入角色名");
        }
        if(roleDetailRequest.getList()==null||roleDetailRequest.getList().size()<=0){
            return CommonResponse.simpleResponse(-1, "请选择菜单");
        }
        if(roleDetailRequest.getRoleId()<=0){
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectByRoleNameAndType(roleDetailRequest.getRoleName(),EnumAdminType.BOSS.getCode());
            if(adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色名已存在");
            }
        }else{
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectByRoleNameAndTypeUnIncludeNow(roleDetailRequest.getRoleName(),EnumAdminType.BOSS.getCode(),roleDetailRequest.getRoleId());
            if(adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色名已存在");
            }
        }
        roleDetailRequest.setType(EnumAdminType.BOSS.getCode());
        adminRoleService.save(roleDetailRequest);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "操作成功");
    }

    /**
     * 分页查询角色列表
     * @param adminRoleListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/roleListByPage", method = RequestMethod.POST)
    public CommonResponse roleListByPage (@RequestBody AdminRoleListRequest adminRoleListRequest) {
        adminRoleListRequest.setType(EnumAdminType.BOSS.getCode());
        PageModel<AdminRoleListPageResponse> adminUserPageModel = adminRoleService.roleListByPage(adminRoleListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserPageModel);
    }

    /**
     * 禁用角色
     * @param adminRoleDetailRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableRole", method = RequestMethod.POST)
    public CommonResponse disableRole (final HttpServletResponse response,@RequestBody AdminRoleDetailRequest adminRoleDetailRequest) {
        adminRoleService.disableRole(adminRoleDetailRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "禁用成功");
    }
    /**
     * 启用角色
     * @param adminRoleDetailRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activeRole", method = RequestMethod.POST)
    public CommonResponse activeRole (@RequestBody AdminRoleDetailRequest adminRoleDetailRequest) {
        adminRoleService.enableRole(adminRoleDetailRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "启用成功");
    }

    /**
     * 是否有权限
     * @param havePermissionRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/havePermission", method = RequestMethod.POST)
    public CommonResponse havePermission (@RequestBody HavePermissionRequest havePermissionRequest) {
        if(super.getAdminUser().getIsMaster()==EnumIsMaster.MASTER.getCode()){
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "有权限访问");
        }
        int count = this.adminUserService.hasDescr(EnumAdminType.BOSS.getCode(),havePermissionRequest.getDescr());
        if(count>0){
            int privilegeCount = this.adminUserService.getPrivilegeByContionsOfJs(super.getAdminUser().getRoleId(),EnumAdminType.BOSS.getCode(),havePermissionRequest.getDescr());
            if(privilegeCount<=0){
                return CommonResponse.simpleResponse(-1, "权限不足");
            }
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "有权限访问");
    }
}
