package com.jkm.hss.controller.DealerController;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumInviteBtn;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.*;
import com.jkm.hss.dealer.helper.response.SecondDealerResponse;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.response.DealerDetailResponse;
import com.jkm.hss.helper.response.SecondDealerProductDetailResponse;
import com.jkm.hss.helper.response.SecondLevelDealerAddResponse;
import com.jkm.hss.merchant.entity.BankCardBin;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    /**
     * 二级代理商列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listSecondDealer", method = RequestMethod.POST)
    public CommonResponse listSecondDealer(@RequestBody final SecondDealerSearchRequest secondDealerSearchRequest) {
        long dealerId = super.getDealerId();
        secondDealerSearchRequest.setDealerId(dealerId);
        final PageModel<SecondDealerResponse> pageModel = this.dealerService.listSecondDealer(secondDealerSearchRequest);
        long hssProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSS.getId());
        long hsyProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSY.getId());
        pageModel.setExt(hssProductId+"|"+hsyProductId);
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
            final long proxyNameCount = this.dealerService.getByProxyName(secondLevelDealerAdd2Request.getName());
            if (proxyNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "代理名称已经存在");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getLoginName())) {
                return CommonResponse.simpleResponse(-1, "登录名不能为空");
            }
            final long loginNameCount = this.dealerService.getByLoginName(secondLevelDealerAdd2Request.getLoginName());
            if (loginNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "登录名已经存在");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getLoginPwd())) {
                return CommonResponse.simpleResponse(-1, "登录密码不能为空");
            }
            if(StringUtils.isBlank(secondLevelDealerAdd2Request.getEmail())) {
                return CommonResponse.simpleResponse(-1, "联系邮箱不能为空");
            }
            if(!ValidateUtils.isEmail(secondLevelDealerAdd2Request.getEmail())) {
                return CommonResponse.simpleResponse(-1, "联系邮箱格式错误");
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
            final long dealerId = this.dealerService.createSecondDealer2(secondLevelDealerAdd2Request,super.getDealerId());
            final SecondLevelDealerAddResponse secondLevelDealerAddResponse = new SecondLevelDealerAddResponse();
            secondLevelDealerAddResponse.setDealerId(dealerId);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "添加成功", secondLevelDealerAddResponse);
        }catch (Exception e){
            log.error("错误信息时",e);
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
        final long loginNameCount = this.dealerService.getByLoginName(uniqueNameRequest.getLoginName());
        if (loginNameCount > 0) {
            return CommonResponse.simpleResponse(-1, "登录名已经存在");
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
        dealerDetailResponse.setLoginName(dealer.getLoginName());
        dealerDetailResponse.setEmail(dealer.getEmail());
        dealerDetailResponse.setMarkCode(dealer.getMarkCode());
        dealerDetailResponse.setBelongProvinceCode(dealer.getBelongProvinceCode());
        dealerDetailResponse.setBelongProvinceName(dealer.getBelongProvinceName());
        dealerDetailResponse.setBelongCityCode(dealer.getBelongCityCode());
        dealerDetailResponse.setBelongCityName(dealer.getBelongCityName());
        dealerDetailResponse.setBelongArea(dealer.getBelongArea());
        final Optional<Dealer> firstDealerOptional = this.dealerService.getById(dealerOptional.get().getFirstLevelDealerId());
        dealerDetailResponse.setFirstDealerName(firstDealerOptional.get().getProxyName());
        dealerDetailResponse.setFirstMarkCode(firstDealerOptional.get().getMarkCode());
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
            final long proxyNameCount = this.dealerService.getByProxyNameUnIncludeNow(request.getName(), request.getDealerId());
            if (proxyNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "代理名称已经存在");
            }

            if(StringUtils.isBlank(request.getLoginName())) {
                return CommonResponse.simpleResponse(-1, "登录名不能为空");
            }
            final long loginNameCount = this.dealerService.getByLoginNameUnIncludeNow(request.getLoginName(), request.getDealerId());
            if (loginNameCount > 0) {
                return CommonResponse.simpleResponse(-1, "登录名已经存在");
            }
            if(StringUtils.isBlank(request.getEmail())) {
                return CommonResponse.simpleResponse(-1, "联系邮箱不能为空");
            }
            if(!ValidateUtils.isEmail(request.getEmail())) {
                return CommonResponse.simpleResponse(-1, "联系邮箱格式错误");
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
            if(org.apache.commons.lang3.StringUtils.isBlank(request.getLoginPwd())) {
                return CommonResponse.simpleResponse(-1, "登录密码不能为空");
            }
            this.dealerService.updatePwd(request.getLoginPwd(),request.getDealerId());
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "修改成功")
                    .addParam("dealerId", request.getDealerId()).build();
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
                    channel.setMerchantSettleRate(dealerChannelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channel.setMerchantWithdrawFee(dealerChannelRate.getDealerMerchantWithdrawFee().toPlainString());
                }
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
            final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerIdAndProductId(super.getDealerId(),request.getProductId());
            final SecondDealerProductDetailResponse.Product productResponse = secondDealerProductDetailResponse.new Product();
            productResponse.setProductId(product.getId());
            productResponse.setProductName(product.getProductName());
            final List<SecondDealerProductDetailResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (DealerChannelRate dealerChannelRate : channelRates) {
                final SecondDealerProductDetailResponse.Channel channel = new SecondDealerProductDetailResponse.Channel();
                Optional<DealerChannelRate> dealerChannelRateOptional = this.dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(request.getDealerId(),request.getProductId(),dealerChannelRate.getChannelTypeSign());
                if(dealerChannelRateOptional.isPresent()){
                    channel.setMerchantSettleRate(dealerChannelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channel.setMerchantWithdrawFee(dealerChannelRate.getDealerMerchantWithdrawFee().toPlainString());
                }
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
                if(dealerChannelRateOptional.isPresent()){
                    return CommonResponse.simpleResponse(-1, "上级代理信息有误");
                }
                DealerChannelRate dealerChannelRate = dealerChannelRateOptional.get();
                if(channes.getChannelType()== EnumPayChannelSign.YG_WEIXIN.getId()){
                    final BigDecimal withdrawSettleFee = new BigDecimal(channes.getWithdrawSettleFee());
                    if (!(withdrawSettleFee.compareTo(dealerChannelRate.getDealerWithdrawFee()) > 0
                            && withdrawSettleFee.compareTo(dealerChannelRate.getDealerMerchantWithdrawFee()) < 0)) {
                        return CommonResponse.simpleResponse(-1, "微信提现结算价错误");
                    }
                    final BigDecimal weixinSettleRate = new BigDecimal(channes.getPaymentSettleRate())
                            .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                    if (!(weixinSettleRate.compareTo(dealerChannelRate.getDealerTradeRate()) > 0
                            && weixinSettleRate.compareTo(dealerChannelRate.getDealerMerchantPayRate()) < 0)) {
                        return CommonResponse.simpleResponse(-1, "微信结算费率错误");
                    }
                }
                if(channes.getChannelType()== EnumPayChannelSign.YG_ZHIFUBAO.getId()){
                    final BigDecimal withdrawSettleFee = new BigDecimal(channes.getWithdrawSettleFee());
                    if (!(withdrawSettleFee.compareTo(dealerChannelRate.getDealerWithdrawFee()) > 0
                            && withdrawSettleFee.compareTo(dealerChannelRate.getDealerMerchantWithdrawFee()) < 0)) {
                        return CommonResponse.simpleResponse(-1, "支付宝提现结算价错误");
                    }
                    final BigDecimal zhifubaoSettleRate = new BigDecimal(channes.getPaymentSettleRate())
                            .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                    if (!(zhifubaoSettleRate.compareTo(dealerChannelRate.getDealerTradeRate()) > 0
                            && zhifubaoSettleRate.compareTo(dealerChannelRate.getDealerMerchantPayRate()) < 0)) {
                        return CommonResponse.simpleResponse(-1, "支付宝结算费率错误");
                    }
                }
                if(channes.getChannelType()== EnumPayChannelSign.YG_YINLIAN.getId()){
                    final BigDecimal withdrawSettleFee = new BigDecimal(channes.getWithdrawSettleFee());
                    if (!(withdrawSettleFee.compareTo(dealerChannelRate.getDealerWithdrawFee()) > 0
                            && withdrawSettleFee.compareTo(dealerChannelRate.getDealerMerchantWithdrawFee()) < 0)) {
                        return CommonResponse.simpleResponse(-1, "快捷提现结算价错误");
                    }
                    final BigDecimal yinlianSettleRate = new BigDecimal(channes.getPaymentSettleRate())
                            .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                    if (!(yinlianSettleRate.compareTo(dealerChannelRate.getDealerTradeRate()) > 0
                            && yinlianSettleRate.compareTo(dealerChannelRate.getDealerMerchantPayRate()) < 0)) {
                        return CommonResponse.simpleResponse(-1, "快捷结算费率错误");
                    }
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
}
