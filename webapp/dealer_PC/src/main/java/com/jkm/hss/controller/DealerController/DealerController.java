package com.jkm.hss.controller.DealerController;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.enums.EnumIsMaster;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.DealerRatePolicy;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumInviteBtn;
import com.jkm.hss.dealer.enums.EnumPolicyType;
import com.jkm.hss.dealer.enums.EnumOemType;
import com.jkm.hss.dealer.enums.EnumRecommendBtn;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.*;
import com.jkm.hss.dealer.helper.response.DealerRatePolicyResponse;
import com.jkm.hss.dealer.helper.response.DealerProfitSettingResponse;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.dealer.helper.response.SecondDealerResponse;
import com.jkm.hss.dealer.service.*;
import com.jkm.hss.helper.response.DealerDetailResponse;
import com.jkm.hss.helper.response.DealerPolicyResponse;
import com.jkm.hss.helper.response.SecondDealerProductDetailResponse;
import com.jkm.hss.helper.response.SecondLevelDealerAddResponse;
import com.jkm.hss.dealer.service.*;
import com.jkm.hss.helper.response.*;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.merchant.helper.ValidationUtil;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/2/13.
 */
@Slf4j
@RestController
@RequestMapping(value = "/daili/dealer")
public class DealerController extends BaseController {
    @Autowired
    private DealerService dealerService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private DealerRateService dealerRateService;
    @Autowired
    private DealerUpgerdeRateService dealerUpgerdeRateService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private DealerProfitService dealerProfitService;
    @Autowired
    private DealerRatePolicyService dealerRatePolicyService;
    /**
     * 二级代理商列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listSecondDealer", method = RequestMethod.POST)
    public CommonResponse listSecondDealer(@RequestBody final SecondDealerSearchRequest secondDealerSearchRequest) {
        long dealerId = super.getDealerId();
        if(super.getDealer().get().getOemType()==EnumOemType.OEM.getId()){//分公司
            secondDealerSearchRequest.setDealerId(dealerId);
            final PageModel<SecondDealerResponse> pageModel = this.dealerService.listSecondOem(secondDealerSearchRequest);
            Long hssProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSS.getId());
            Integer hsyProductId = dealerRatePolicyService.selectCountByDealerId(dealerId);
            long hssProduct = 0;
            long hsyProduct = 0;
            if(hssProductId!=null){
                hssProduct = hssProductId;
            }
            if(hsyProductId!=null){
                hsyProduct = hsyProductId;
            }
            pageModel.setExt(hssProduct+"|"+hsyProduct);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
        }else{
            secondDealerSearchRequest.setDealerId(dealerId);
            final PageModel<SecondDealerResponse> pageModel = this.dealerService.listSecondDealer(secondDealerSearchRequest);
            Long hssProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSS.getId());
            Integer hsyProductId = dealerRatePolicyService.selectCountByDealerId(dealerId);
            long hssProduct = 0;
            long hsyProduct = 0;
            if(hssProductId!=null){
                hssProduct = hssProductId;
            }
            if(hsyProductId!=null){
                hsyProduct = hsyProductId;
            }
            pageModel.setExt(hssProduct+"|"+hsyProduct);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
        }
    }
    /**
     * 一级代理商列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listFirstDealer", method = RequestMethod.POST)
    public CommonResponse listFirstDealer(@RequestBody final FirstDealerSearchRequest firstDealerSearchRequest) {
        long dealerId = super.getDealerId();
        firstDealerSearchRequest.setOemId(dealerId);
        final PageModel<FirstDealerResponse> pageModel = this.dealerService.listFirstDealer(firstDealerSearchRequest);
        Long hssProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSS.getId());
        Integer hsyProductId = dealerRatePolicyService.selectCountByDealerId(dealerId);
        long hssProduct = 0;
        long hsyProduct = 0;
        if(hssProductId!=null){
            hssProduct = hssProductId;
        }
        if(hsyProductId!=null){
            hsyProduct = hsyProductId;
        }
        pageModel.setExt(hssProduct+"|"+hsyProduct);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }
    /**
     * 新增二级代理商
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addSecondDealer", method = RequestMethod.POST)
    public CommonResponse addSecondDealer(@RequestBody final SecondLevelDealerAdd2Request secondLevelDealerAdd2Request) {
        try{
            secondLevelDealerAdd2Request.setOemType(EnumOemType.DEALER.getId());
            Preconditions.checkState(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理不可以添加二级代理");
            if(!ValidateUtils.isMobile(secondLevelDealerAdd2Request.getMobile())) {
                return CommonResponse.simpleResponse(-1, "代理商手机号格式错误");
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getByMobile(secondLevelDealerAdd2Request.getMobile());
            if (dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "代理商手机号已经注册");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getName())) {
                return CommonResponse.simpleResponse(-1, "代理名称不能为空");
            }
            final long proxyNameCount = this.dealerService.selectByProxyNameAndOemType(secondLevelDealerAdd2Request.getName(),EnumOemType.DEALER.getId());
            if (proxyNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "代理名称已经存在");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getLoginName())) {
                return CommonResponse.simpleResponse(-1, "登录名不能为空");
            }
            Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndType(secondLevelDealerAdd2Request.getLoginName(),EnumAdminType.SECONDDEALER.getCode());
            if (adminUserOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "登录名已经存在");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getLoginPwd())) {
                return CommonResponse.simpleResponse(-1, "登录密码不能为空");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getEmail())) {
                return CommonResponse.simpleResponse(-1, "联系邮箱不能为空");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getBelongProvinceCode())) {
                return CommonResponse.simpleResponse(-1, "所在省份编码不能为空");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getBelongProvinceName())) {
                return CommonResponse.simpleResponse(-1, "所在省份不能为空");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getBelongCityCode())) {
                return CommonResponse.simpleResponse(-1, "所在市编码不能为空");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getBelongCityName())) {
                return CommonResponse.simpleResponse(-1, "所在市不能为空");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getBelongArea())) {
                return CommonResponse.simpleResponse(-1, "详细地址不能为空");
            }
            final String bankCard = secondLevelDealerAdd2Request.getBankCard();
            final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankCard);
            if (!bankCardBinOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "结算卡格式错误");
            }
            secondLevelDealerAdd2Request.setBankName(bankCardBinOptional.get().getBankName());
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getBankAccountName())) {
                return CommonResponse.simpleResponse(-1, "开户名称不能为空");
            }
            if(!ValidationUtil.isIdCard(secondLevelDealerAdd2Request.getIdCard())){
                return CommonResponse.simpleResponse(-1, "身份证格式不正确");
            }
            if(!ValidateUtils.isMobile(secondLevelDealerAdd2Request.getBankReserveMobile())) {
                return CommonResponse.simpleResponse(-1, "开户手机号格式错误");
            }

            secondLevelDealerAdd2Request.setOemId(super.getDealer().get().getOemId());
            final long dealerId = this.dealerService.createSecondDealer2(secondLevelDealerAdd2Request,super.getDealerId());

            //创建登录用户
            AdminUser adminUser = new AdminUser();
            adminUser.setUsername(secondLevelDealerAdd2Request.getLoginName());
            adminUser.setSalt("200000");
            adminUser.setPassword(DealerSupport.passwordDigest(secondLevelDealerAdd2Request.getLoginPwd(),"JKM"));
            adminUser.setRealname(secondLevelDealerAdd2Request.getBankAccountName());
            adminUser.setEmail(secondLevelDealerAdd2Request.getEmail());
            adminUser.setMobile(AdminUserSupporter.encryptMobile(secondLevelDealerAdd2Request.getMobile()));
            adminUser.setCompanyId("");
            adminUser.setDeptId("");
            adminUser.setIdCard(AdminUserSupporter.encryptIdenrity(secondLevelDealerAdd2Request.getIdCard()));
            adminUser.setRoleId(0l);
            adminUser.setIdentityFacePic("");
            adminUser.setIdentityOppositePic("");
            adminUser.setType(EnumAdminType.SECONDDEALER.getCode());
            adminUser.setDealerId(dealerId);
            adminUser.setIsMaster(EnumIsMaster.MASTER.getCode());
            adminUser.setStatus(EnumAdminUserStatus.NORMAL.getCode());
            this.adminUserService.createSecondDealerUser(adminUser);

            final SecondLevelDealerAddResponse secondLevelDealerAddResponse = new SecondLevelDealerAddResponse();
            secondLevelDealerAddResponse.setDealerId(dealerId);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "添加成功", secondLevelDealerAddResponse);
        }catch (Exception e){
            log.error("错误信息时",e);
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }

    /**
     * 添加一级代理商
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addFirstDealer", method = RequestMethod.POST)
    public CommonResponse addFirstDealer(@RequestBody final FirstLevelDealerAdd2Request firstLevelDealerAdd2Request) {
        try{
            firstLevelDealerAdd2Request.setOemType(EnumOemType.DEALER.getId());
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
            firstLevelDealerAdd2Request.setOemId(super.getDealerId());

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
            adminUser.setType(EnumAdminType.FIRSTDEALER.getCode());
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
     * 判断登录名是否重复
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uniqueName", method = RequestMethod.POST)
    public CommonResponse uniqueName(@RequestBody final UniqueNameRequest uniqueNameRequest) {
        if(StringUtils.isBlank(uniqueNameRequest.getLoginName())) {
            return CommonResponse.simpleResponse(-1, "登录名不能为空");
        }
        final Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndType(uniqueNameRequest.getLoginName(), EnumAdminType.SECONDDEALER.getCode());
        if(adminUserOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "登录名已存在");
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "登录名不存在");
    }

    /**
     * 代理商详情
     * @param dealerId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{dealerId}", method = RequestMethod.GET)
    public CommonResponse dealerDetail(@PathVariable final long dealerId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final DealerDetailResponse dealerDetailResponse = new DealerDetailResponse();
        dealerDetailResponse.setId(dealer.getId());
        dealerDetailResponse.setMobile(dealer.getMobile());
        dealerDetailResponse.setName(dealer.getProxyName());
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByDealerIdAndIsMaster(dealerId,EnumIsMaster.MASTER.getCode());
        if(adminUserOptional.isPresent()){
            dealerDetailResponse.setLoginName(adminUserOptional.get().getUsername());
        }
        dealerDetailResponse.setEmail(dealer.getEmail());
        dealerDetailResponse.setMarkCode(dealer.getMarkCode());
        dealerDetailResponse.setBelongProvinceCode(dealer.getBelongProvinceCode());
        dealerDetailResponse.setBelongProvinceName(dealer.getBelongProvinceName());
        dealerDetailResponse.setBelongCityCode(dealer.getBelongCityCode());
        dealerDetailResponse.setBelongCityName(dealer.getBelongCityName());
        dealerDetailResponse.setBelongArea(dealer.getBelongArea());
        final Optional<Dealer> firstDealerOptional = this.dealerService.getById(dealerOptional.get().getFirstLevelDealerId());
        if(firstDealerOptional.isPresent()){
            dealerDetailResponse.setFirstDealerName(firstDealerOptional.get().getProxyName());
            dealerDetailResponse.setFirstMarkCode(firstDealerOptional.get().getMarkCode());
        }
        dealerDetailResponse.setBankCard(DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard()));
        dealerDetailResponse.setBankAccountName(dealer.getBankAccountName());
        dealerDetailResponse.setBankReserveMobile(DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile()));
        if (dealer.getIdCard()!=null){
            dealerDetailResponse.setIdCard(DealerSupport.decryptIdentity(dealer.getId(),dealer.getIdCard()));
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerDetailResponse);
    }

    /**
     * 更新二级代理商
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateDealer", method = RequestMethod.POST)
    public CommonResponse updateDealer(@RequestBody SecondLevelDealerUpdate2Request request) {
        try{
            if(!ValidateUtils.isMobile(request.getMobile())) {
                return CommonResponse.simpleResponse(-1, "代理商手机号格式错误");
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getByMobileUnIncludeNow(request.getMobile(), request.getDealerId());
            if (dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "代理商手机号已经注册");
            }
            if(StringUtils.isBlank(request.getName())) {
                return CommonResponse.simpleResponse(-1, "代理名称不能为空");
            }
            final long proxyNameCount = this.dealerService.getByProxyNameUnIncludeNow(request.getName(), EnumOemType.DEALER.getId(), request.getDealerId());
            if (proxyNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "代理名称已经存在");
            }

            if(StringUtils.isBlank(request.getLoginName())) {
                return CommonResponse.simpleResponse(-1, "登录名不能为空");
            }
            Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndTypeUnIncludeNow(request.getLoginName(),EnumAdminType.SECONDDEALER.getCode(),request.getDealerId());
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
            this.dealerService.updateSecondDealer(request);
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
     * 更新一级代理商
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateFirstDealer", method = RequestMethod.POST)
    public CommonResponse updateFirstDealer(@RequestBody FirstLevelDealerUpdate2Request request) {
        try{
            if(!ValidateUtils.isMobile(request.getMobile())) {
                return CommonResponse.simpleResponse(-1, "代理商手机号格式错误");
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getByMobileUnIncludeNow(request.getMobile(), request.getDealerId());
            if (dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "代理商手机号已经注册");
            }
            if(StringUtils.isBlank(request.getName())) {
                return CommonResponse.simpleResponse(-1, "代理名称不能为空");
            }
            final long proxyNameCount = this.dealerService.getByProxyNameUnIncludeNow(request.getName(), EnumOemType.DEALER.getId(), request.getDealerId());
            if (proxyNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "代理名称已经存在");
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
     * 修改密码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    public CommonResponse updatePwd(@RequestBody DealerPwdRequest request) {
        try{
            final Optional<Dealer> dealerOptional = this.dealerService.getById(request.getDealerId());
            if(!dealerOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            if(StringUtils.isBlank(request.getLoginPwd())) {
                return CommonResponse.simpleResponse(-1, "登录密码不能为空");
            }
            request.setLoginPwd(DealerSupport.passwordDigest(request.getLoginPwd(),"JKM"));
            this.dealerService.updatePwd(request.getLoginPwd(),request.getDealerId());
            adminUserService.updateDealerUserPwd(request.getLoginPwd(),request.getDealerId());
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "修改成功")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }

    /**
     * 修改新密码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateNewPwd", method = RequestMethod.POST)
    public CommonResponse updateNewPwd(@RequestBody DealerNewPwdRequest request) {
        try{
            if(StringUtils.isBlank(request.getPwd())){
                return CommonResponse.simpleResponse(-1, "原密码不能为空");
            }
            if(StringUtils.isBlank(request.getNewPwd())){
                return CommonResponse.simpleResponse(-1, "新密码不能为空");
            }
            if(StringUtils.isBlank(request.getRepeatNewPwd())){
                return CommonResponse.simpleResponse(-1, "重复新密码不能为空");
            }
            if(!request.getNewPwd().equals(request.getRepeatNewPwd())){
                return CommonResponse.simpleResponse(-1, "新密码和重复密码不一致");
            }
            if(request.getNewPwd().equals(request.getPwd())){
                return CommonResponse.simpleResponse(-1, "新密码不能和原密码一致");
            }
            String password = this.adminUserService.getPwd(super.getAdminUserId());
            if(!password.equals(DealerSupport.passwordDigest(request.getPwd(),"JKM"))){
                return CommonResponse.simpleResponse(-1, "原密码错误");
            }
            request.setNewPwd(DealerSupport.passwordDigest(request.getNewPwd(),"JKM"));
            this.dealerService.updatePwd(request.getNewPwd(),super.getDealerId());
            adminUserService.updateDealerUserPwdById(request.getNewPwd(),super.getAdminUserId());
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }
    /**
     * 查询产品信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDealerProduct", method = RequestMethod.POST)
    public CommonResponse getDealerProduct(@RequestBody DealerProductDetailRequest request) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(request.getDealerId());
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final SecondDealerProductDetailResponse secondDealerProductDetailResponse = new SecondDealerProductDetailResponse();
        if(request.getProductId()>0){//修改
            Optional<Product> productOptional = this.productService.selectById(request.getProductId());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerIdAndProductId(super.getDealerId(),request.getProductId());
            final SecondDealerProductDetailResponse.Product productResponse = secondDealerProductDetailResponse.new Product();
            productResponse.setProductId(productOptional.get().getId());
            productResponse.setProductName(productOptional.get().getProductName());
            final List<SecondDealerProductDetailResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (DealerChannelRate dealerChannelRate:channelRates) {
                final SecondDealerProductDetailResponse.Channel channel = new SecondDealerProductDetailResponse.Channel();
                Optional<DealerChannelRate> dealerChannelRateOptional = this.dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(request.getDealerId(),request.getProductId(),dealerChannelRate.getChannelTypeSign());
                if(dealerChannelRateOptional.isPresent()){
                    channel.setPaymentSettleRate(dealerChannelRateOptional.get().getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channel.setWithdrawSettleFee(dealerChannelRateOptional.get().getDealerWithdrawFee().toPlainString());
                }
                channel.setMerchantSettleRate(dealerChannelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMerchantWithdrawFee(dealerChannelRate.getDealerMerchantWithdrawFee().toPlainString());
                channel.setMinPaymentSettleRate(dealerChannelRate.getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMinWithdrawSettleFee(dealerChannelRate.getDealerWithdrawFee().toPlainString());
                channel.setChannelType(dealerChannelRate.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(dealerChannelRate.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(dealerChannelRate.getDealerBalanceType());
                channels.add(channel);
            }
            secondDealerProductDetailResponse.setProduct(productResponse);
            String tempTypeName="好收收";
            if("hsy".equals(request.getSysType())){
                tempTypeName = "好收银";
            }
            secondDealerProductDetailResponse.setProductName(tempTypeName);
            secondDealerProductDetailResponse.setInviteCode(dealer.getInviteCode());
            secondDealerProductDetailResponse.setInviteBtn(dealer.getInviteBtn());
        }else{//新增
            Optional<Product> productOptional = this.productService.selectByType(request.getSysType());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final Product product = productOptional.get();
            //根据产品查找产品详情
            final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerIdAndProductId(super.getDealerId(),product.getId());
            if(channelRates.size()==0){
                return CommonResponse.simpleResponse(-1, "您的产品信息尚未完善");
            }
            final SecondDealerProductDetailResponse.Product productResponse = secondDealerProductDetailResponse.new Product();
            productResponse.setProductId(product.getId());
            productResponse.setProductName(product.getProductName());
            final List<SecondDealerProductDetailResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (DealerChannelRate dealerChannelRate : channelRates) {
                final SecondDealerProductDetailResponse.Channel channel = new SecondDealerProductDetailResponse.Channel();
                channel.setMerchantSettleRate(dealerChannelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMerchantWithdrawFee(dealerChannelRate.getDealerMerchantWithdrawFee().toPlainString());
                channel.setMinPaymentSettleRate(dealerChannelRate.getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMinWithdrawSettleFee(dealerChannelRate.getDealerWithdrawFee().toPlainString());
                channel.setChannelType(dealerChannelRate.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(dealerChannelRate.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(dealerChannelRate.getDealerBalanceType());
                channels.add(channel);
            }
            secondDealerProductDetailResponse.setProduct(productResponse);
            String tempTypeName="好收收";
            if("hsy".equals(request.getSysType())){
                tempTypeName = "好收银";
            }
            secondDealerProductDetailResponse.setProductName(tempTypeName);
            secondDealerProductDetailResponse.setInviteCode(dealer.getInviteCode());
            secondDealerProductDetailResponse.setInviteBtn(dealer.getInviteBtn());
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", secondDealerProductDetailResponse);
    }

    /**
     * 获取一级代理配置
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getFirstDealerProduct", method = RequestMethod.POST)
    public CommonResponse getFirstDealerProduct(@RequestBody DealerProductDetailRequest request) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(request.getDealerId());
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final FirstDealerProductDetailResponse firstDealerProductDetailResponse = new FirstDealerProductDetailResponse();
        if(request.getProductId()>0){//修改
            Optional<Product> productOptional = this.productService.selectById(request.getProductId());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final Product product = productOptional.get();
            final List<ProductChannelDetail> channelRates = this.productChannelDetailService.selectByProductId(product.getId());
            if(channelRates.size()==0){
                return CommonResponse.simpleResponse(-1, "您的产品信息尚未完善");
            }
            final FirstDealerProductDetailResponse.Product productResponse = firstDealerProductDetailResponse.new Product();
            productResponse.setProductId(productOptional.get().getId());
            productResponse.setProductName(productOptional.get().getProductName());
            final List<FirstDealerProductDetailResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail:channelRates) {
                final FirstDealerProductDetailResponse.Channel channel = new FirstDealerProductDetailResponse.Channel();
                Optional<DealerChannelRate> dealerChannelRateOptional = this.dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(request.getDealerId(),product.getId(),productChannelDetail.getChannelTypeSign());
                channel.setPaymentSettleRate(dealerChannelRateOptional.get().getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setWithdrawSettleFee(dealerChannelRateOptional.get().getDealerWithdrawFee().toPlainString());
                channel.setMerchantSettleRate(dealerChannelRateOptional.get().getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMerchantWithdrawFee(dealerChannelRateOptional.get().getDealerMerchantWithdrawFee().toPlainString());
                channel.setChannelType(productChannelDetail.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(productChannelDetail.getProductBalanceType());
                channel.setMinPaymentSettleRate(productChannelDetail.getProductTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMinWithdrawSettleFee(productChannelDetail.getProductWithdrawFee().toPlainString());
                channel.setMaxMerchantSettleRate(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate()).multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMaxMerchantWithdrawFee(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate()).toPlainString());

                channels.add(channel);
            }
            firstDealerProductDetailResponse.setProduct(productResponse);
            String tempTypeName="好收收";
            if("hsy".equals(request.getSysType())){
                tempTypeName = "好收银";
            }
            firstDealerProductDetailResponse.setProductName(tempTypeName);
//            firstDealerProductDetailResponse.setInviteCode(dealer.getInviteCode());
//            firstDealerProductDetailResponse.setInviteBtn(dealer.getInviteBtn());
        }else{//新增
            Optional<Product> productOptional = this.productService.selectByType(request.getSysType());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final Product product = productOptional.get();
            //根据产品查找产品详情
            final List<ProductChannelDetail> channelRates = this.productChannelDetailService.selectByProductId(product.getId());
            if(channelRates.size()==0){
                return CommonResponse.simpleResponse(-1, "您的产品信息尚未完善");
            }
            final FirstDealerProductDetailResponse.Product productResponse = firstDealerProductDetailResponse.new Product();
            productResponse.setProductId(product.getId());
            productResponse.setProductName(product.getProductName());
            final List<FirstDealerProductDetailResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail : channelRates) {
                final FirstDealerProductDetailResponse.Channel channel = new FirstDealerProductDetailResponse.Channel();
                channel.setChannelType(productChannelDetail.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(productChannelDetail.getProductBalanceType());
                channel.setMinPaymentSettleRate(productChannelDetail.getProductTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMinWithdrawSettleFee(productChannelDetail.getProductWithdrawFee().toPlainString());
                channel.setMaxMerchantSettleRate(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate()).multiply(new BigDecimal("100")).setScale(2).toPlainString());
                channel.setMaxMerchantWithdrawFee(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate()).toPlainString());
                channels.add(channel);
            }
            firstDealerProductDetailResponse.setProduct(productResponse);
            String tempTypeName="好收收";
            if("hsy".equals(request.getSysType())){
                tempTypeName = "好收银";
            }
            firstDealerProductDetailResponse.setProductName(tempTypeName);
//            firstDealerProductDetailResponse.setInviteCode(dealer.getInviteCode());
//            firstDealerProductDetailResponse.setInviteBtn(dealer.getInviteBtn());
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", firstDealerProductDetailResponse);
    }
    /**
     * 新增或添加二级代理商产品
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addOrUpdateDealerProduct", method = RequestMethod.POST)
    public CommonResponse addOrUpdateDealerProduct(@RequestBody DealerAddOrUpdateRequest request) {
        try{
            final DealerAddOrUpdateRequest.Product productParam = request.getProduct();
            final long productId = productParam.getProductId();
            final Optional<Product> productOptional = this.productService.selectById(productId);
            if (!productOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final Product product = productOptional.get();
            final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerIdAndProductId(super.getDealerId(),product.getId());
            List<ProductChannelDetail> productChannelDetails = this.productChannelDetailService.selectByProductId(product.getId());
            if(channelRates.size()!=productChannelDetails.size()){
                return CommonResponse.simpleResponse(-1, "上级代理信息有误");
            }
            for(DealerAddOrUpdateRequest.Channel channes:request.getProduct().getChannels()){
                if (StringUtils.isBlank(channes.getPaymentSettleRate())) {
                    return CommonResponse.simpleResponse(-1, "支付结算手续费不能为空");
                }
                if (StringUtils.isBlank(channes.getWithdrawSettleFee())) {
                    return CommonResponse.simpleResponse(-1, "提现结算费不能为空");
                }
                Optional<DealerChannelRate> dealerChannelRateOptional = this.dealerRateService.getByDealerIdAndProductIdAndChannelType(super.getDealerId(), productId, channes.getChannelType());
                if(!dealerChannelRateOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "上级代理信息有误");
                }
                DealerChannelRate dealerChannelRate = dealerChannelRateOptional.get();

                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(channes.getChannelType());
                if(!basicChannelOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "没有"+channes.getChannelType()+"通道基础配置");
                }
                final BigDecimal withdrawSettleFee = new BigDecimal(channes.getWithdrawSettleFee());
                if (!(withdrawSettleFee.compareTo(dealerChannelRate.getDealerWithdrawFee()) >= 0
                        && withdrawSettleFee.compareTo(dealerChannelRate.getDealerMerchantWithdrawFee()) <= 0)) {
                    return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"提现结算价错误:必须大于一级的小于商户的");
                }
                final BigDecimal paymentSettleRate = new BigDecimal(channes.getPaymentSettleRate())
                        .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                if (!(paymentSettleRate.compareTo(dealerChannelRate.getDealerTradeRate()) >= 0
                        && paymentSettleRate.compareTo(dealerChannelRate.getDealerMerchantPayRate()) <= 0)) {
                    return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"结算费率错误:必须大于一级的小于商户的");
                }
            }
            if(request.getInviteBtn()!= EnumInviteBtn.ON.getId()&&request.getInviteBtn()!=EnumInviteBtn.OFF.getId()){
                return CommonResponse.simpleResponse(-1, "推广开关参数有误");
            }
            this.dealerService.addOrUpdateDealerProduct(request,super.getDealerId());
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "操作成功")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }

    /**
     * 新增或添加分公司好收收配置
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addOrUpdateHssDealer", method = RequestMethod.POST)
    public CommonResponse addOrUpdateHssDealer(@RequestBody OemAddOrUpdateRequest request) {
        try{
            final Optional<Dealer> dealerOptional = this.dealerService.getById(request.getDealerId());
            if(!dealerOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            final OemAddOrUpdateRequest.Product productParam = request.getProduct();
            final long productId = productParam.getProductId();
            final Optional<Product> productOptional = this.productService.selectById(productId);
            if (!productOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final Product product = productOptional.get();
            final List<ProductChannelDetail> productChannelDetails = this.productChannelDetailService.selectByProductId(productId);
            final Map<Integer, ProductChannelDetail> integerProductChannelDetailImmutableMap =
                    Maps.uniqueIndex(productChannelDetails, new Function<ProductChannelDetail, Integer>() {
                        @Override
                        public Integer apply(ProductChannelDetail input) {
                            return input.getChannelTypeSign();
                        }
                    });
            final List<OemAddOrUpdateRequest.Channel> channelParams = productParam.getChannels();
            for (OemAddOrUpdateRequest.Channel channelParam : channelParams) {
                final CommonResponse commonResponse = this.checkChannel(channelParam, integerProductChannelDetailImmutableMap, product);
                if (1 != commonResponse.getCode()) {
                    return commonResponse;
                }
            }
            this.dealerService.addOrUpdateHssOem(request);
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "操作成功")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }

    private CommonResponse checkChannel(final OemAddOrUpdateRequest.Channel paramChannel,
                                        final Map<Integer, ProductChannelDetail> integerProductChannelDetailImmutableMap,
                                        final Product product) {
        if (org.apache.commons.lang3.StringUtils.isBlank(paramChannel.getMerchantSettleRate())) {
            return CommonResponse.simpleResponse(-1, "商户支付手续费不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(paramChannel.getMerchantWithdrawFee())) {
            return CommonResponse.simpleResponse(-1, "商户提现手续费不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(paramChannel.getPaymentSettleRate())) {
            return CommonResponse.simpleResponse(-1, "支付结算手续费不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(paramChannel.getWithdrawSettleFee())) {
            return CommonResponse.simpleResponse(-1, "提现结算费不能为空");
        }
        final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(paramChannel.getChannelType());
        final BigDecimal merchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
        Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(paramChannel.getChannelType());
        if(!basicChannelOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "没有"+paramChannel.getChannelType()+"通道基础配置");
        }
        if (merchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
        }
        final BigDecimal merchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
        if (merchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
        }
        final BigDecimal paymentSettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
        if (paymentSettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                || paymentSettleRate.compareTo(merchantSettleRate) > 0) {
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");
        }
        final BigDecimal withdrawSettleFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
        if (withdrawSettleFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                || withdrawSettleFee.compareTo(merchantWithdrawFee) > 0) {
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
        }
        return CommonResponse.simpleResponse(1, "");
    }
    /**
     * 好收银代理商政策
     * @param dealerId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/dealerRatePolicyDetail/{dealerId}", method = RequestMethod.GET)
    public CommonResponse dealerRatePolicyDetail (@PathVariable long dealerId) {
        List<DealerRatePolicy> dealerRatePolicies = dealerRatePolicyService.selectByDealerId(dealerId);
        List<DealerRatePolicyResponse> dealerRatePolicyResponses = new ArrayList<DealerRatePolicyResponse>();
        if(dealerRatePolicies.size()>0){
            for(int i=0;i<dealerRatePolicies.size();i++){
                DealerRatePolicyResponse dealerRatePolicyResponse = new DealerRatePolicyResponse();
                dealerRatePolicyResponse.setId(dealerRatePolicies.get(i).getId());
                dealerRatePolicyResponse.setDealerId(dealerRatePolicies.get(i).getDealerId());
                dealerRatePolicyResponse.setPolicyType(dealerRatePolicies.get(i).getPolicyType());
                dealerRatePolicyResponse.setSn(dealerRatePolicies.get(i).getSn());
                dealerRatePolicyResponse.setStatus(dealerRatePolicies.get(i).getStatus());
                dealerRatePolicyResponse.setCreateTime(dealerRatePolicies.get(i).getCreateTime());
                dealerRatePolicyResponse.setUpdateTime(dealerRatePolicies.get(i).getUpdateTime());
                if((EnumPolicyType.WITHDRAW.getId()).equals(dealerRatePolicies.get(i).getPolicyType())){
                    DealerRatePolicyResponse.FixedArr fixedArr = new DealerRatePolicyResponse.FixedArr();
                    DealerRatePolicyResponse.ChangeArr changeArr = new DealerRatePolicyResponse.ChangeArr();
                    changeArr.setDealerTradeRateT1(dealerRatePolicies.get(i).getDealerTradeRateT1().setScale(2));
                    fixedArr.setMerchantMinRateT1(dealerRatePolicies.get(i).getMerchantMinRateT1().setScale(2));
                    fixedArr.setMerchantMaxRateT1(dealerRatePolicies.get(i).getMerchantMaxRateT1().setScale(2));
                    changeArr.setDealerTradeRateD1(dealerRatePolicies.get(i).getDealerTradeRateD1().setScale(2));
                    fixedArr.setMerchantMinRateD1(dealerRatePolicies.get(i).getMerchantMinRateD1().setScale(2));
                    fixedArr.setMerchantMaxRateD1(dealerRatePolicies.get(i).getMerchantMaxRateD1().setScale(2));
                    changeArr.setDealerTradeRateD0(dealerRatePolicies.get(i).getDealerTradeRateD0().setScale(2));
                    fixedArr.setMerchantMinRateD0(dealerRatePolicies.get(i).getMerchantMinRateD0().setScale(2));
                    fixedArr.setMerchantMaxRateD0(dealerRatePolicies.get(i).getMerchantMaxRateD0().setScale(2));
                    dealerRatePolicyResponse.setFixedArr(fixedArr);
                    dealerRatePolicyResponse.setChangeArr(changeArr);
                }else{
                    DealerRatePolicyResponse.FixedArr fixedArr = new DealerRatePolicyResponse.FixedArr();
                    DealerRatePolicyResponse.ChangeArr changeArr = new DealerRatePolicyResponse.ChangeArr();
                    changeArr.setDealerTradeRateT1(dealerRatePolicies.get(i).getDealerTradeRateT1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMinRateT1(dealerRatePolicies.get(i).getMerchantMinRateT1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMaxRateT1(dealerRatePolicies.get(i).getMerchantMaxRateT1().multiply(new BigDecimal("100")).setScale(2));
                    changeArr.setDealerTradeRateD1(dealerRatePolicies.get(i).getDealerTradeRateD1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMinRateD1(dealerRatePolicies.get(i).getMerchantMinRateD1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMaxRateD1(dealerRatePolicies.get(i).getMerchantMaxRateD1().multiply(new BigDecimal("100")).setScale(2));
                    changeArr.setDealerTradeRateD0(dealerRatePolicies.get(i).getDealerTradeRateD0().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMinRateD0(dealerRatePolicies.get(i).getMerchantMinRateD0().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMaxRateD0(dealerRatePolicies.get(i).getMerchantMaxRateD0().multiply(new BigDecimal("100")).setScale(2));
                    dealerRatePolicyResponse.setFixedArr(fixedArr);
                    dealerRatePolicyResponse.setChangeArr(changeArr);
                }
                dealerRatePolicyResponses.add(dealerRatePolicyResponse);
            }
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerRatePolicyResponses);
    }

    /**
     * 登录代理商好收银政策
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getProductRatePolicyDetail", method = RequestMethod.POST)
    public CommonResponse getProductRatePolicyDetail () {
        List<DealerRatePolicy> dealerRatePolicies = dealerRatePolicyService.selectByDealerId(super.getDealerId());
        List<DealerRatePolicyResponse> dealerRatePolicyResponses = new ArrayList<DealerRatePolicyResponse>();
        if(dealerRatePolicies.size()>0){
            for(int i=0;i<dealerRatePolicies.size();i++){
                DealerRatePolicyResponse dealerRatePolicyResponse = new DealerRatePolicyResponse();
                dealerRatePolicyResponse.setId(dealerRatePolicies.get(i).getId());
                dealerRatePolicyResponse.setDealerId(dealerRatePolicies.get(i).getDealerId());
                dealerRatePolicyResponse.setPolicyType(dealerRatePolicies.get(i).getPolicyType());
                dealerRatePolicyResponse.setSn(dealerRatePolicies.get(i).getSn());
                dealerRatePolicyResponse.setStatus(dealerRatePolicies.get(i).getStatus());
                dealerRatePolicyResponse.setCreateTime(dealerRatePolicies.get(i).getCreateTime());
                dealerRatePolicyResponse.setUpdateTime(dealerRatePolicies.get(i).getUpdateTime());
                if(!(EnumPolicyType.WITHDRAW.getId()).equals(dealerRatePolicies.get(i).getPolicyType())){
                    DealerRatePolicyResponse.FixedArr fixedArr = new DealerRatePolicyResponse.FixedArr();
                    DealerRatePolicyResponse.ChangeArr changeArr = new DealerRatePolicyResponse.ChangeArr();
                    changeArr.setDealerTradeRateT1(dealerRatePolicies.get(i).getDealerTradeRateT1().multiply(new BigDecimal("100")).setScale(2));
                    changeArr.setDealerTradeRateD1(dealerRatePolicies.get(i).getDealerTradeRateD1().multiply(new BigDecimal("100")).setScale(2));
                    changeArr.setDealerTradeRateD0(dealerRatePolicies.get(i).getDealerTradeRateD0().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMinRateT1(dealerRatePolicies.get(i).getMerchantMinRateT1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMaxRateT1(dealerRatePolicies.get(i).getMerchantMaxRateT1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMinRateD1(dealerRatePolicies.get(i).getMerchantMinRateD1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMaxRateD1(dealerRatePolicies.get(i).getMerchantMaxRateD1().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMinRateD0(dealerRatePolicies.get(i).getMerchantMinRateD0().multiply(new BigDecimal("100")).setScale(2));
                    fixedArr.setMerchantMaxRateD0(dealerRatePolicies.get(i).getMerchantMaxRateD0().multiply(new BigDecimal("100")).setScale(2));
                    dealerRatePolicyResponse.setFixedArr(fixedArr);
                    dealerRatePolicyResponse.setChangeArr(changeArr);
                }else{
                    DealerRatePolicyResponse.FixedArr fixedArr = new DealerRatePolicyResponse.FixedArr();
                    DealerRatePolicyResponse.ChangeArr changeArr = new DealerRatePolicyResponse.ChangeArr();
                    changeArr.setDealerTradeRateT1(dealerRatePolicies.get(i).getDealerTradeRateT1().setScale(2));
                    changeArr.setDealerTradeRateD1(dealerRatePolicies.get(i).getDealerTradeRateD1().setScale(2));
                    changeArr.setDealerTradeRateD0(dealerRatePolicies.get(i).getDealerTradeRateD0().setScale(2));
                    fixedArr.setMerchantMinRateT1(dealerRatePolicies.get(i).getMerchantMinRateT1().setScale(2));
                    fixedArr.setMerchantMaxRateT1(dealerRatePolicies.get(i).getMerchantMaxRateT1().setScale(2));
                    fixedArr.setMerchantMinRateD1(dealerRatePolicies.get(i).getMerchantMinRateD1().setScale(2));
                    fixedArr.setMerchantMaxRateD1(dealerRatePolicies.get(i).getMerchantMaxRateD1().setScale(2));
                    fixedArr.setMerchantMinRateD0(dealerRatePolicies.get(i).getMerchantMinRateD0().setScale(2));
                    fixedArr.setMerchantMaxRateD0(dealerRatePolicies.get(i).getMerchantMaxRateD0().setScale(2));
                    dealerRatePolicyResponse.setFixedArr(fixedArr);
                    dealerRatePolicyResponse.setChangeArr(changeArr);
                }
                dealerRatePolicyResponses.add(dealerRatePolicyResponse);
            }
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerRatePolicyResponses);
    }
    /**
     * 保存或修改好收银代理商政策
     * @param dealerRatePolicySaveOrUpdateRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdateDealerRatePolicy", method = RequestMethod.POST)
    public CommonResponse saveOrUpdateDealerRatePolicy (@RequestBody DealerRatePolicySaveOrUpdateRequest dealerRatePolicySaveOrUpdateRequest) {
        List<DealerRatePolicy> dealerRatePolicies = dealerRatePolicyService.selectByDealerId(dealerRatePolicySaveOrUpdateRequest.getDealerId());
        List<DealerRatePolicyResponse> dealerRatePolicyList = dealerRatePolicySaveOrUpdateRequest.getDealerRatePolicies();
        if(dealerRatePolicyList.size()<=0){
            return CommonResponse.simpleResponse(-1, "政策不能为空");
        }
        final CommonResponse commonResponse = this.checkRatePolicy(dealerRatePolicyList);
        if (1 != commonResponse.getCode()) {
            return commonResponse;
        }
        if(dealerRatePolicies.size()>0){
            for(int i=0;i<dealerRatePolicyList.size();i++){
                DealerRatePolicy dealerRatePolicy = new DealerRatePolicy();
                dealerRatePolicy.setId(dealerRatePolicyList.get(i).getId());
                dealerRatePolicy.setStatus(EnumStatus.NORMAL.getId());
                dealerRatePolicy.setDealerId(dealerRatePolicySaveOrUpdateRequest.getDealerId());
                dealerRatePolicy.setPolicyType(dealerRatePolicyList.get(i).getPolicyType());
                dealerRatePolicy.setSn(dealerRatePolicyList.get(i).getSn());
                if(!(EnumPolicyType.WITHDRAW.getId()).equals(dealerRatePolicyList.get(i).getPolicyType())){
                    dealerRatePolicy.setDealerTradeRateT1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateT1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMinRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateT1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMaxRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateT1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));

                    dealerRatePolicy.setDealerTradeRateD1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMinRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMaxRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));

                    dealerRatePolicy.setDealerTradeRateD0(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD0().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMinRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD0().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMaxRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD0().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                }else{
                    dealerRatePolicy.setDealerTradeRateT1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateT1());
                    dealerRatePolicy.setMerchantMinRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateT1());
                    dealerRatePolicy.setMerchantMaxRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateT1());

                    dealerRatePolicy.setDealerTradeRateD1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD1());
                    dealerRatePolicy.setMerchantMinRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD1());
                    dealerRatePolicy.setMerchantMaxRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD1());

                    dealerRatePolicy.setDealerTradeRateD0(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD0());
                    dealerRatePolicy.setMerchantMinRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD0());
                    dealerRatePolicy.setMerchantMaxRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD0());
                }
                dealerRatePolicyService.update(dealerRatePolicy);
            }
        }else{
            for(int i=0;i<dealerRatePolicyList.size();i++){
                DealerRatePolicy dealerRatePolicy = new DealerRatePolicy();
                dealerRatePolicy.setStatus(EnumStatus.NORMAL.getId());
                dealerRatePolicy.setDealerId(dealerRatePolicySaveOrUpdateRequest.getDealerId());
                dealerRatePolicy.setPolicyType(dealerRatePolicyList.get(i).getPolicyType());
                dealerRatePolicy.setSn(dealerRatePolicyList.get(i).getSn());
                if(!(EnumPolicyType.WITHDRAW.getId()).equals(dealerRatePolicyList.get(i).getPolicyType())){
                    dealerRatePolicy.setDealerTradeRateT1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateT1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMinRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateT1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMaxRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateT1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));

                    dealerRatePolicy.setDealerTradeRateD1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMinRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMaxRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD1().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));

                    dealerRatePolicy.setDealerTradeRateD0(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD0().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMinRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD0().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                    dealerRatePolicy.setMerchantMaxRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD0().
                            divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
                }else{
                    dealerRatePolicy.setDealerTradeRateT1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateT1());
                    dealerRatePolicy.setMerchantMinRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateT1());
                    dealerRatePolicy.setMerchantMaxRateT1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateT1());

                    dealerRatePolicy.setDealerTradeRateD1(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD1());
                    dealerRatePolicy.setMerchantMinRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD1());
                    dealerRatePolicy.setMerchantMaxRateD1(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD1());

                    dealerRatePolicy.setDealerTradeRateD0(dealerRatePolicyList.get(i).getChangeArr().getDealerTradeRateD0());
                    dealerRatePolicy.setMerchantMinRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMinRateD0());
                    dealerRatePolicy.setMerchantMaxRateD0(dealerRatePolicyList.get(i).getFixedArr().getMerchantMaxRateD0());
                }
                dealerRatePolicyService.insert(dealerRatePolicy);
            }
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "操作成功");
    }

    private CommonResponse checkRatePolicy(List<DealerRatePolicyResponse> dealerRatePolicyList){
        if(dealerRatePolicyList.size()!=3){
            return CommonResponse.simpleResponse(-1, "尚有未填写选项");
        }
        for(int i=0;i<dealerRatePolicyList.size();i++){
            DealerRatePolicyResponse dealerRatePolicy = dealerRatePolicyList.get(i);
            if((EnumPolicyType.WECHAT.getId()).equals(dealerRatePolicy.getPolicyType())){
                Optional<DealerRatePolicy> dealerRatePolicyOptional = dealerRatePolicyService.selectByDealerIdAndPolicyType(super.getDealerId(),EnumPolicyType.WECHAT.getId());
                if(!dealerRatePolicyOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "boss尚未给一代设置微信T1代理政策");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "微信T1代理商结算价不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "微信T1商户最小费率不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "微信T1商户最大费率不能为空");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "微信D1代理商结算价不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "微信D1商户最小费率不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "微信D1商户最大费率不能为空");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "微信D0代理商结算价不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "微信D0商户最小费率不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "微信D0商户最大费率不能为空");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateT1().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateT1().divide(new BigDecimal("100")))>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代微信T1代理商结算价");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateD1().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateD1().divide(new BigDecimal("100")))>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代微信D1代理商结算价");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateD0().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateD0().divide(new BigDecimal("100")))>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代微信D0代理商结算价");
                }
            }
            if((EnumPolicyType.ALIPAY.getId()).equals(dealerRatePolicy.getPolicyType())){
                Optional<DealerRatePolicy> dealerRatePolicyOptional = dealerRatePolicyService.selectByDealerIdAndPolicyType(super.getDealerId(),EnumPolicyType.ALIPAY.getId());
                if(!dealerRatePolicyOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "boss尚未给一代设置支付宝T1代理政策");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝T1代理商结算价不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝T1商户最小费率不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝T1商户最大费率不能为空");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝D1代理商结算价不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝D1商户最小费率不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝D1商户最大费率不能为空");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝D0代理商结算价不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝D0商户最小费率不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "支付宝D0商户最大费率不能为空");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateT1().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateT1().divide(new BigDecimal("100")))>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代支付宝T1代理商结算价");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateD1().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateD1().divide(new BigDecimal("100")))>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代支付宝D1代理商结算价");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateD0().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateD0().divide(new BigDecimal("100")))>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代支付宝D0代理商结算价");
                }
            }
            if((EnumPolicyType.WITHDRAW.getId()).equals(dealerRatePolicy.getPolicyType())){
                Optional<DealerRatePolicy> dealerRatePolicyOptional = dealerRatePolicyService.selectByDealerIdAndPolicyType(super.getDealerId(),EnumPolicyType.WITHDRAW.getId());
                if(!dealerRatePolicyOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "boss尚未给一代设置提现手续费T1代理政策");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费T1代理商手续费不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费T1商户最小手续费不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateT1()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费T1商户最大手续费不能为空");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费D1代理商手续费不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费D1商户最小手续费不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateD1()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费D1商户最大手续费不能为空");
                }
                if(dealerRatePolicy.getChangeArr().getDealerTradeRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费D0代理商手续费不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMinRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费D0商户最小手续费不能为空");
                }
                if(dealerRatePolicy.getFixedArr().getMerchantMaxRateD0()==null){
                    return CommonResponse.simpleResponse(-1, "提现手续费D0商户最大手续费不能为空");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateT1().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateT1())>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代T1提现手续费");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateD1().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateD1())>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代D1提现手续费");
                }
                if(dealerRatePolicyOptional.get().getDealerTradeRateD0().compareTo(dealerRatePolicy.getChangeArr().getDealerTradeRateD0())>0){
                    return CommonResponse.simpleResponse(-1, "二代必须大于等于一代D0提现手续费");
                }
            }
        }
        return CommonResponse.simpleResponse(1, "");
    }
    /**
     * 注册信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/dealerDetails", method = RequestMethod.GET)
    public CommonResponse dealerDetails() {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(super.getDealerId());
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final DealerDetailResponse dealerDetailResponse = new DealerDetailResponse();
        dealerDetailResponse.setId(dealer.getId());
        dealerDetailResponse.setMobile(dealer.getMobile());
        dealerDetailResponse.setName(dealer.getProxyName());
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByDealerIdAndIsMaster(super.getDealerId(),EnumIsMaster.MASTER.getCode());
        if(adminUserOptional.isPresent()){
            dealerDetailResponse.setLoginName(adminUserOptional.get().getUsername());
        }
        dealerDetailResponse.setEmail(dealer.getEmail());
        dealerDetailResponse.setMarkCode(dealer.getMarkCode());
        dealerDetailResponse.setBelongProvinceCode(dealer.getBelongProvinceCode());
        dealerDetailResponse.setBelongProvinceName(dealer.getBelongProvinceName());
        dealerDetailResponse.setBelongCityCode(dealer.getBelongCityCode());
        dealerDetailResponse.setBelongCityName(dealer.getBelongCityName());
        dealerDetailResponse.setBelongArea(dealer.getBelongArea());
        if(dealer.getFirstLevelDealerId()>0){
            final Optional<Dealer> firstDealerOptional = this.dealerService.getById(dealerOptional.get().getFirstLevelDealerId());
            if (!firstDealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "上级代理信息有误");
            }
            dealerDetailResponse.setFirstDealerName(firstDealerOptional.get().getProxyName());
            dealerDetailResponse.setFirstMarkCode(firstDealerOptional.get().getMarkCode());
            dealerDetailResponse.setBankCard(DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard()));
            dealerDetailResponse.setBankAccountName(dealer.getBankAccountName());
            dealerDetailResponse.setBankReserveMobile(DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile()));
            if (dealer.getIdCard()!=null){
                dealerDetailResponse.setIdCard(DealerSupport.decryptIdentity(dealer.getId(),dealer.getIdCard()));
            }
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerDetailResponse);
        }else{
            dealerDetailResponse.setFirstDealerName("金开门");
            dealerDetailResponse.setFirstMarkCode("000000000000");
            dealerDetailResponse.setBankCard(DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard()));
            dealerDetailResponse.setBankAccountName(dealer.getBankAccountName());
            dealerDetailResponse.setBankReserveMobile(DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile()));
            if (dealer.getIdCard()!=null){
                dealerDetailResponse.setIdCard(DealerSupport.decryptIdentity(dealer.getId(),dealer.getIdCard()));
            }
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerDetailResponse);
        }

    }

    /**
     * 代理商政策
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/dealerPolicy", method = RequestMethod.POST)
    public CommonResponse dealerPolicy(@RequestBody DealerPolicyRequest request) {
        DealerPolicyResponse dealerPolicyResponse = new DealerPolicyResponse();
        dealerPolicyResponse.setIsShow("2");
        if((request.getSysType()).equals(EnumProductType.HSS.getId())){
            Optional<Product> productOptional = this.productService.selectByType(request.getSysType());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getById(super.getDealerId());
            if (!dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            dealerPolicyResponse.setInviteBtn(dealerOptional.get().getInviteBtn());
            dealerPolicyResponse.setInviteCode(dealerOptional.get().getInviteCode());
            final DealerPolicyResponse.Product productResponse = dealerPolicyResponse.new Product();
            productResponse.setProductId(productOptional.get().getId());
            productResponse.setProductName(productOptional.get().getProductName());
            final List<DealerPolicyResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            List<DealerChannelRate> dealerChannelRates = this.dealerChannelRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),productOptional.get().getId());
            if(dealerChannelRates.size()>0){
                for(int i=0;i<dealerChannelRates.size();i++){
                    final DealerPolicyResponse.Channel channelResponse = new DealerPolicyResponse.Channel();
                    channelResponse.setChannelType(dealerChannelRates.get(i).getChannelTypeSign());
                    Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(dealerChannelRates.get(i).getChannelTypeSign());
                    channelResponse.setChannelName(basicChannelOptional.get().getChannelName());
                    channelResponse.setPaymentSettleRate(dealerChannelRates.get(i).getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channelResponse.setSettleType(dealerChannelRates.get(i).getDealerBalanceType());
                    channelResponse.setWithdrawSettleFee(dealerChannelRates.get(i).getDealerWithdrawFee().toPlainString());
                    channelResponse.setMerchantSettleRate(dealerChannelRates.get(i).getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channelResponse.setMerchantWithdrawFee(dealerChannelRates.get(i).getDealerMerchantWithdrawFee().toPlainString());
                    channels.add(channelResponse);
                }
            }
            dealerPolicyResponse.setProduct(productResponse);

            if(dealerOptional.get().getLevel()==EnumDealerLevel.FIRST.getId()&&dealerOptional.get().getOemType()== EnumOemType.DEALER.getId()){
                final List<DealerPolicyResponse.DealerUpgerdeRate> dealerUpgerdeRates = new ArrayList<>();
                List<DealerUpgerdeRate> upgerdeRates = dealerUpgerdeRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),productOptional.get().getId());
                for(DealerUpgerdeRate dealerUpgerdeRate:upgerdeRates){
                    final DealerPolicyResponse.DealerUpgerdeRate du = dealerPolicyResponse.new DealerUpgerdeRate();
                    du.setId(dealerUpgerdeRate.getId());
                    du.setType(dealerUpgerdeRate.getType());
                    du.setFirstDealerShareProfitRate(dealerUpgerdeRate.getFirstDealerShareProfitRate().toString());
                    du.setSecondDealerShareProfitRate(dealerUpgerdeRate.getSecondDealerShareProfitRate().toString());
                    du.setBossDealerShareRate(dealerUpgerdeRate.getBossDealerShareRate().toString());
                    dealerUpgerdeRates.add(du);
                }
                dealerPolicyResponse.setDealerUpgerdeRates(dealerUpgerdeRates);
                List<DealerProfitSettingResponse> dealerProfitSettingResponses = dealerProfitService.selectDealerByDealerIdAndProductId(super.getDealerId(),productOptional.get().getId());
                dealerPolicyResponse.setDealerProfits(dealerProfitSettingResponses);
                if(dealerUpgerdeRates!=null&&dealerChannelRates.size()>0){
                    dealerPolicyResponse.setIsShow("1");
                }
            }
        }
        if((request.getSysType()).equals(EnumProductType.HSY.getId())){
            Optional<Product> productOptional = this.productService.selectByType(request.getSysType());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品不存在");
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getById(super.getDealerId());
            if (!dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            dealerPolicyResponse.setInviteBtn(dealerOptional.get().getInviteBtn());
            dealerPolicyResponse.setInviteCode(dealerOptional.get().getInviteCode());
            final DealerPolicyResponse.Product productResponse = dealerPolicyResponse.new Product();
            productResponse.setProductId(productOptional.get().getId());
            productResponse.setProductName(productOptional.get().getProductName());
            final List<DealerPolicyResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            List<DealerChannelRate> dealerChannelRates = this.dealerChannelRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),productOptional.get().getId());
            if(dealerChannelRates.size()>0){
                for(int i=0;i<dealerChannelRates.size();i++){
                    final DealerPolicyResponse.Channel channelResponse = new DealerPolicyResponse.Channel();
                    channelResponse.setChannelType(dealerChannelRates.get(i).getChannelTypeSign());
                    Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(dealerChannelRates.get(i).getChannelTypeSign());
                    channelResponse.setChannelName(basicChannelOptional.get().getChannelName());
                    channelResponse.setPaymentSettleRate(dealerChannelRates.get(i).getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channelResponse.setSettleType(dealerChannelRates.get(i).getDealerBalanceType());
                    channelResponse.setWithdrawSettleFee(dealerChannelRates.get(i).getDealerWithdrawFee().toPlainString());
                    channelResponse.setMerchantSettleRate(dealerChannelRates.get(i).getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channelResponse.setMerchantWithdrawFee(dealerChannelRates.get(i).getDealerMerchantWithdrawFee().toPlainString());
                    channels.add(channelResponse);
                }
            }
            dealerPolicyResponse.setProduct(productResponse);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerPolicyResponse);

    }
}
