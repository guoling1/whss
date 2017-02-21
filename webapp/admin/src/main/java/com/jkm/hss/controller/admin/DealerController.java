package com.jkm.hss.controller.admin;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumDealerRateType;
import com.jkm.hss.dealer.enums.EnumInviteBtn;
import com.jkm.hss.dealer.enums.EnumRecommendBtn;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.*;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.dealer.helper.response.SecondDealerResponse;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.DealerUpgerdeRateService;
import com.jkm.hss.helper.request.FirstLevelDealerFindRequest;
import com.jkm.hss.helper.response.*;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.entity.UpgradeRecommendRules;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/dealer")
public class DealerController extends BaseController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DealerChannelRateService dealerChannelRateService;

    @Autowired
    private DealerUpgerdeRateService dealerUpgerdeRateService;

    @Autowired
    private ProductChannelDetailService productChannelDetailService;

    @Autowired
    private BasicChannelService basicChannelService;

    @Autowired
    private UpgradeRecommendRulesService upgradeRecommendRulesService;

    /**
     * 按手机号和名称模糊匹配
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public CommonResponse find(@RequestBody final FirstLevelDealerFindRequest firstLevelDealerFindRequest) {
        final String condition = firstLevelDealerFindRequest.getCondition();
        final String _condition = StringUtils.trim(condition);
        Preconditions.checkState(!Strings.isNullOrEmpty(_condition), "查询条件不能为空");
        List<Dealer> dealerList = this.dealerService.findByCondition(_condition, 0,
                EnumDealerLevel.FIRST.getId());
        final List<FirstLevelDealerFindResponse> responses = Lists.transform(dealerList, new Function<Dealer, FirstLevelDealerFindResponse>() {
            @Override
            public FirstLevelDealerFindResponse apply(Dealer input) {
                final FirstLevelDealerFindResponse firstLevelDealerFindResponse = new FirstLevelDealerFindResponse();
                firstLevelDealerFindResponse.setDealerId(input.getId());
                firstLevelDealerFindResponse.setMobile(input.getMobile());
                firstLevelDealerFindResponse.setName(input.getProxyName());
                return firstLevelDealerFindResponse;
            }
        });
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", responses);
    }

    /**
     * 一级代理商列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listDealer", method = RequestMethod.POST)
    public CommonResponse listDealer(@RequestBody final ListDealerRequest listDealerRequest) {
//        if (EnumDealerLevel.FIRST.getId() != listDealerRequest.getLevel()
//                && EnumDealerLevel.SECOND.getId() != listDealerRequest.getLevel()) {
//            listDealerRequest.setLevel(EnumDealerLevel.FIRST.getId());
//        }
        final String mobile = listDealerRequest.getMobile();
        if (StringUtils.isEmpty(StringUtils.trim(mobile))) {
            listDealerRequest.setMobile(null);
        }
        final String name = listDealerRequest.getName();
        if(StringUtils.isEmpty(StringUtils.trim(name))) {
            listDealerRequest.setName(null);
        }
        final String belongArea = listDealerRequest.getBelongArea();
        if(StringUtils.isEmpty(StringUtils.trim(belongArea))) {
            listDealerRequest.setBelongArea(null);
        }

        final PageModel<Dealer> pageModel = this.dealerService.listDealer(listDealerRequest);
        final List<Dealer> records = pageModel.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (Dealer dealer : records) {
                dealer.setSettleBankCard(DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard()));
                dealer.setBankReserveMobile(DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile()));
            }
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", pageModel);
    }

    /**
     * 查询代理商信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "{dealerId}", method = RequestMethod.GET)
    public CommonResponse getDealer(@PathVariable final long dealerId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final List<DealerChannelRate> channelRates = this.dealerChannelRateService.selectByDealerId(dealerId);
        if (CollectionUtils.isEmpty(channelRates)) {
            return CommonResponse.simpleResponse(-1, "代理商对应产品通道不存在");
        }
        final Optional<Product> productOptional = this.productService.selectById(channelRates.get(0).getProductId());
        if (!productOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商对应产品不存在");
        }
        final Product product = productOptional.get();
        final FirstLevelDealerGetResponse firstLevelDealerGetResponse = new FirstLevelDealerGetResponse();
        firstLevelDealerGetResponse.setMobile(dealer.getMobile());
        firstLevelDealerGetResponse.setName(dealer.getProxyName());
        firstLevelDealerGetResponse.setBelongArea(dealer.getBelongArea());
        firstLevelDealerGetResponse.setBankCard(DealerSupport.decryptBankCard(dealer.getId(), dealer.getSettleBankCard()));
        firstLevelDealerGetResponse.setBankName(dealer.getBankName());
        if (dealer.getIdCard()!=null){
            firstLevelDealerGetResponse.setIdCard(DealerSupport.decryptIdentity(dealer.getId(),dealer.getIdCard()));
        }
        firstLevelDealerGetResponse.setBankAccountName(dealer.getBankAccountName());
        firstLevelDealerGetResponse.setBankReserveMobile(DealerSupport.decryptMobile(dealer.getId(), dealer.getBankReserveMobile()));
        final FirstLevelDealerGetResponse.Product productResponse = firstLevelDealerGetResponse.new Product();
        firstLevelDealerGetResponse.setProduct(productResponse);
        firstLevelDealerGetResponse.setTotalProfitSpace(dealer.getTotalProfitSpace());
        firstLevelDealerGetResponse.setRecommendBtn(dealer.getRecommendBtn());
        productResponse.setProductId(product.getId());
        productResponse.setProductName(product.getProductName());
        productResponse.setLimitPayFeeRate(product.getLimitPayFeeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
        productResponse.setLimitWithdrawFeeRate(product.getLimitWithdrawFeeRate().toPlainString());
        final List<FirstLevelDealerGetResponse.Channel> channels = new ArrayList<>();
        productResponse.setChannels(channels);
        for (DealerChannelRate dealerChannelRate : channelRates) {
            final FirstLevelDealerGetResponse.Channel channel = new FirstLevelDealerGetResponse.Channel();
            channel.setChannelType(dealerChannelRate.getChannelTypeSign());
            channel.setPaymentSettleRate(dealerChannelRate.getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
            channel.setSettleType(dealerChannelRate.getDealerBalanceType());
            channel.setWithdrawSettleFee(dealerChannelRate.getDealerWithdrawFee().toPlainString());
            channel.setMerchantSettleRate(dealerChannelRate.getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
            channel.setMerchantWithdrawFee(dealerChannelRate.getDealerMerchantWithdrawFee().toPlainString());
            channels.add(channel);
        }

        final List<FirstLevelDealerGetResponse.DealerUpgerdeRate> dealerUpgerdeRates = new ArrayList<>();
        firstLevelDealerGetResponse.setDealerUpgerdeRates(dealerUpgerdeRates);

        List<DealerUpgerdeRate> upgerdeRates = dealerUpgerdeRateService.selectByDealerIdAndProductId(dealerId,product.getId());
        if (upgerdeRates==null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        for(DealerUpgerdeRate dealerUpgerdeRate:upgerdeRates){
            final FirstLevelDealerGetResponse.DealerUpgerdeRate du = new FirstLevelDealerGetResponse.DealerUpgerdeRate();
            du.setId(dealerUpgerdeRate.getId());
            du.setProductId(dealerUpgerdeRate.getProductId());
            du.setType(dealerUpgerdeRate.getType());
            du.setDealerId(dealerUpgerdeRate.getDealerId());
            du.setFirstDealerShareProfitRate(dealerUpgerdeRate.getFirstDealerShareProfitRate().toString());
            du.setSecondDealerShareProfitRate(dealerUpgerdeRate.getSecondDealerShareProfitRate().toString());
            du.setBossDealerShareRate(dealerUpgerdeRate.getBossDealerShareRate().toString());
            dealerUpgerdeRates.add(du);
        }

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", firstLevelDealerGetResponse);
    }


//==============================此处为对代理商进行重构=============================
    /**
     * 一级代理商列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listFirstDealer", method = RequestMethod.POST)
    public CommonResponse listFirstDealer(@RequestBody final ListFirstDealerRequest listFirstDealerRequest) {
        final PageModel<FirstDealerResponse> pageModel = this.dealerService.listFirstDealer(listFirstDealerRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 二级代理商列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listSecondDealer", method = RequestMethod.POST)
    public CommonResponse listSecondDealer(@RequestBody final ListSecondDealerRequest listSecondDealerRequest) {
        final PageModel<SecondDealerResponse> pageModel = this.dealerService.listSecondDealer(listSecondDealerRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }
    /**
     * 根据代理商编码超找代理商信息
     * @param dealerId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findBydealerId/{dealerId}", method = RequestMethod.GET)
    public CommonResponse findBydealerId(@PathVariable final long dealerId) {
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
        if(dealerOptional.get().getFirstLevelDealerId()>0){
            final Optional<Dealer> firstDealerOptional = this.dealerService.getById(dealerOptional.get().getFirstLevelDealerId());
            dealerDetailResponse.setFirstDealerName(firstDealerOptional.get().getProxyName());
            dealerDetailResponse.setFirstMarkCode(firstDealerOptional.get().getMarkCode());
        }else{
            dealerDetailResponse.setFirstDealerName("金开门");
            dealerDetailResponse.setFirstMarkCode("000000000000");
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
     * hss查询代理商信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hss/{dealerId}/{productId}", method = RequestMethod.GET)
    public CommonResponse hasUpgrade(@PathVariable final long dealerId,@PathVariable final long productId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final FirstLevelDealerGet2Response firstLevelDealerGet2Response = new FirstLevelDealerGet2Response();
        if(productId>0){//修改
            Optional<Product> productOptional = this.productService.selectById(productId);
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "好收收产品配置有误");
            }
            if(!(EnumProductType.HSS.getId()).equals(productOptional.get().getType())){
                return CommonResponse.simpleResponse(-1, "该产品不属于好收收");
            }
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(productId);
            final FirstLevelDealerGet2Response.Product productResponse = firstLevelDealerGet2Response.new Product();
            productResponse.setProductId(productId);
            productResponse.setProductName(productOptional.get().getProductName());
            final List<FirstLevelDealerGet2Response.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail:detailList) {
                final FirstLevelDealerGet2Response.Channel channel = new FirstLevelDealerGet2Response.Channel();
                Optional<DealerChannelRate> dealerChannelRateOptional = this.dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(dealerId,productId,productChannelDetail.getChannelTypeSign());
                if(dealerChannelRateOptional.isPresent()){
                    channel.setPaymentSettleRate(dealerChannelRateOptional.get().getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channel.setWithdrawSettleFee(dealerChannelRateOptional.get().getDealerWithdrawFee().toPlainString());
                    channel.setMerchantSettleRate(dealerChannelRateOptional.get().getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channel.setMerchantWithdrawFee(dealerChannelRateOptional.get().getDealerMerchantWithdrawFee().toPlainString());
                }
                channel.setChannelType(productChannelDetail.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(productChannelDetail.getProductBalanceType());
                channels.add(channel);
            }
            firstLevelDealerGet2Response.setProduct(productResponse);
            firstLevelDealerGet2Response.setProductName("好收收");
            firstLevelDealerGet2Response.setRecommendBtn(dealer.getRecommendBtn());
            firstLevelDealerGet2Response.setInviteCode(dealer.getInviteCode());
            firstLevelDealerGet2Response.setInviteBtn(dealer.getInviteBtn());
            firstLevelDealerGet2Response.setTotalProfitSpace(dealer.getTotalProfitSpace());

            Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(productId);
            if(!upgradeRecommendRulesOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "请先配置升级费分润和收单分润");
            }

            List<DealerUpgerdeRate> upgerdeRates = dealerUpgerdeRateService.selectByDealerIdAndProductId(dealerId,productId);
            final List<FirstLevelDealerGet2Response.DealerUpgerdeRate> dealerUpgerdeRates = new ArrayList<>();
            if(upgerdeRates.size()==0){
                final FirstLevelDealerGet2Response.DealerUpgerdeRate dealerUpgerdeRate1 = firstLevelDealerGet2Response.new DealerUpgerdeRate();
                dealerUpgerdeRate1.setId(0);
                dealerUpgerdeRate1.setProductId(productId);
                dealerUpgerdeRate1.setDealerId(dealerId);
                dealerUpgerdeRate1.setType(EnumDealerRateType.UPGRADE.getId());
                dealerUpgerdeRate1.setBossDealerShareRate(upgradeRecommendRulesOptional.get().getUpgradeRate().toString());
                final FirstLevelDealerGet2Response.DealerUpgerdeRate dealerUpgerdeRate2 = firstLevelDealerGet2Response.new DealerUpgerdeRate();
                dealerUpgerdeRate2.setId(0);
                dealerUpgerdeRate2.setProductId(productId);
                dealerUpgerdeRate2.setDealerId(dealerId);
                dealerUpgerdeRate2.setType(EnumDealerRateType.TRADE.getId());
                dealerUpgerdeRate2.setBossDealerShareRate(upgradeRecommendRulesOptional.get().getTradeRate().toString());
                dealerUpgerdeRates.add(dealerUpgerdeRate1);
                dealerUpgerdeRates.add(dealerUpgerdeRate2);
            }
            if(upgerdeRates.size()==1){
                if(EnumDealerRateType.UPGRADE.getId()==upgerdeRates.get(0).getType()){
                    final FirstLevelDealerGet2Response.DealerUpgerdeRate du = firstLevelDealerGet2Response.new DealerUpgerdeRate();
                    du.setId(upgerdeRates.get(0).getId());
                    du.setProductId(upgerdeRates.get(0).getProductId());
                    du.setDealerId(upgerdeRates.get(0).getDealerId());
                    du.setType(upgerdeRates.get(0).getType());
                    du.setFirstDealerShareProfitRate(upgerdeRates.get(0).getFirstDealerShareProfitRate().toString());
                    du.setSecondDealerShareProfitRate(upgerdeRates.get(0).getSecondDealerShareProfitRate().toString());
                    du.setBossDealerShareRate(upgerdeRates.get(0).getBossDealerShareRate().toString());
                    dealerUpgerdeRates.add(du);
                    final FirstLevelDealerGet2Response.DealerUpgerdeRate dealerUpgerdeRate2 = firstLevelDealerGet2Response.new DealerUpgerdeRate();
                    dealerUpgerdeRate2.setId(0);
                    dealerUpgerdeRate2.setProductId(productId);
                    dealerUpgerdeRate2.setDealerId(dealerId);
                    dealerUpgerdeRate2.setType(EnumDealerRateType.TRADE.getId());
                    dealerUpgerdeRate2.setBossDealerShareRate(upgradeRecommendRulesOptional.get().getTradeRate().toString());
                    dealerUpgerdeRates.add(dealerUpgerdeRate2);
                }
                if(EnumDealerRateType.TRADE.getId()==upgerdeRates.get(0).getType()){
                    final FirstLevelDealerGet2Response.DealerUpgerdeRate dealerUpgerdeRate1 = firstLevelDealerGet2Response.new DealerUpgerdeRate();
                    dealerUpgerdeRate1.setId(0);
                    dealerUpgerdeRate1.setProductId(productId);
                    dealerUpgerdeRate1.setDealerId(dealerId);
                    dealerUpgerdeRate1.setType(EnumDealerRateType.UPGRADE.getId());
                    dealerUpgerdeRate1.setBossDealerShareRate(upgradeRecommendRulesOptional.get().getUpgradeRate().toString());
                    dealerUpgerdeRates.add(dealerUpgerdeRate1);
                    final FirstLevelDealerGet2Response.DealerUpgerdeRate du = firstLevelDealerGet2Response.new DealerUpgerdeRate();
                    du.setId(upgerdeRates.get(0).getId());
                    du.setProductId(upgerdeRates.get(0).getProductId());
                    du.setDealerId(upgerdeRates.get(0).getDealerId());
                    du.setType(upgerdeRates.get(0).getType());
                    du.setFirstDealerShareProfitRate(upgerdeRates.get(0).getFirstDealerShareProfitRate().toString());
                    du.setSecondDealerShareProfitRate(upgerdeRates.get(0).getSecondDealerShareProfitRate().toString());
                    du.setBossDealerShareRate(upgerdeRates.get(0).getBossDealerShareRate().toString());
                    dealerUpgerdeRates.add(du);
                }
            }
            if(upgerdeRates.size()==2){
                for(DealerUpgerdeRate dealerUpgerdeRate:upgerdeRates){
                    final FirstLevelDealerGet2Response.DealerUpgerdeRate du = firstLevelDealerGet2Response.new DealerUpgerdeRate();
                    du.setId(dealerUpgerdeRate.getId());
                    du.setProductId(dealerUpgerdeRate.getProductId());
                    du.setDealerId(dealerUpgerdeRate.getDealerId());
                    du.setType(dealerUpgerdeRate.getType());
                    du.setFirstDealerShareProfitRate(dealerUpgerdeRate.getFirstDealerShareProfitRate().toString());
                    du.setSecondDealerShareProfitRate(dealerUpgerdeRate.getSecondDealerShareProfitRate().toString());
                    du.setBossDealerShareRate(dealerUpgerdeRate.getBossDealerShareRate().toString());
                    dealerUpgerdeRates.add(du);
                }
            }
            firstLevelDealerGet2Response.setDealerUpgerdeRates(dealerUpgerdeRates);
        }else{//新增
            Optional<Product> productOptional = this.productService.selectByType(EnumProductType.HSS.getId());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "好收收产品不存在");
            }
            final Product product = productOptional.get();
            //根据产品查找产品详情
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(productOptional.get().getId());
            final FirstLevelDealerGet2Response.Product productResponse = firstLevelDealerGet2Response.new Product();
            productResponse.setProductId(product.getId());
            productResponse.setProductName(product.getProductName());
            final List<FirstLevelDealerGet2Response.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail : detailList) {
                final FirstLevelDealerGet2Response.Channel channel = new FirstLevelDealerGet2Response.Channel();
                channel.setChannelType(productChannelDetail.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(productChannelDetail.getProductBalanceType());
                channels.add(channel);
            }
            firstLevelDealerGet2Response.setProduct(productResponse);
            firstLevelDealerGet2Response.setProductName("好收收");
            firstLevelDealerGet2Response.setRecommendBtn(dealer.getRecommendBtn());
            firstLevelDealerGet2Response.setInviteCode(dealer.getInviteCode());
            firstLevelDealerGet2Response.setInviteBtn(dealer.getInviteBtn());
            firstLevelDealerGet2Response.setTotalProfitSpace(dealer.getTotalProfitSpace());

            Optional<UpgradeRecommendRules> upgradeRecommendRulesOptional = upgradeRecommendRulesService.selectByProductId(product.getId());
            if(!upgradeRecommendRulesOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "请先配置升级费分润和收单分润");
            }
            final FirstLevelDealerGet2Response.DealerUpgerdeRate dealerUpgerdeRate1 = firstLevelDealerGet2Response.new DealerUpgerdeRate();
            dealerUpgerdeRate1.setProductId(product.getId());
            dealerUpgerdeRate1.setDealerId(dealerId);
            dealerUpgerdeRate1.setType(EnumDealerRateType.UPGRADE.getId());
            dealerUpgerdeRate1.setBossDealerShareRate(upgradeRecommendRulesOptional.get().getUpgradeRate().toString());
            final FirstLevelDealerGet2Response.DealerUpgerdeRate dealerUpgerdeRate2 = firstLevelDealerGet2Response.new DealerUpgerdeRate();
            dealerUpgerdeRate2.setProductId(product.getId());
            dealerUpgerdeRate2.setDealerId(dealerId);
            dealerUpgerdeRate2.setType(EnumDealerRateType.TRADE.getId());
            dealerUpgerdeRate2.setBossDealerShareRate(upgradeRecommendRulesOptional.get().getTradeRate().toString());
            final List<FirstLevelDealerGet2Response.DealerUpgerdeRate> dealerUpgerdeRates = new ArrayList<>();
            dealerUpgerdeRates.add(dealerUpgerdeRate1);
            dealerUpgerdeRates.add(dealerUpgerdeRate2);
            firstLevelDealerGet2Response.setDealerUpgerdeRates(dealerUpgerdeRates);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", firstLevelDealerGet2Response);
    }

    /**
     * hsy查询代理商信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hsy/{dealerId}/{productId}", method = RequestMethod.GET)
    public CommonResponse getHsyDealerProduct(@PathVariable final long dealerId,@PathVariable final long productId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final FirstLevelDealerGet3Response firstLevelDealerGet3Response = new FirstLevelDealerGet3Response();
        if(productId>0){//修改
            Optional<Product> productOptional = this.productService.selectById(productId);
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品配置有误");
            }
            if(!(EnumProductType.HSY.getId()).equals(productOptional.get().getType())){
                return CommonResponse.simpleResponse(-1, "该产品不属于好收银");
            }
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(productId);
            final FirstLevelDealerGet3Response.Product productResponse = firstLevelDealerGet3Response.new Product();
            productResponse.setProductId(productId);
            productResponse.setProductName(productOptional.get().getProductName());
            final List<FirstLevelDealerGet3Response.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail:detailList) {
                final FirstLevelDealerGet3Response.Channel channel = new FirstLevelDealerGet3Response.Channel();
                Optional<DealerChannelRate> dealerChannelRateOptional = this.dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(dealerId,productId,productChannelDetail.getChannelTypeSign());
                if(dealerChannelRateOptional.isPresent()){
                    channel.setPaymentSettleRate(dealerChannelRateOptional.get().getDealerTradeRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channel.setWithdrawSettleFee(dealerChannelRateOptional.get().getDealerWithdrawFee().toPlainString());
                    channel.setMerchantSettleRate(dealerChannelRateOptional.get().getDealerMerchantPayRate().multiply(new BigDecimal("100")).setScale(2).toPlainString());
                    channel.setMerchantWithdrawFee(dealerChannelRateOptional.get().getDealerMerchantWithdrawFee().toPlainString());
                }
                channel.setChannelType(productChannelDetail.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(productChannelDetail.getProductBalanceType());
                channels.add(channel);
            }
            firstLevelDealerGet3Response.setProduct(productResponse);
            firstLevelDealerGet3Response.setProductName("好收银");
            firstLevelDealerGet3Response.setInviteCode(dealer.getInviteCode());
            firstLevelDealerGet3Response.setInviteBtn(dealer.getInviteBtn());
        }else{//新增
            Optional<Product> productOptional = this.productService.selectByType(EnumProductType.HSY.getId());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "好收银产品不存在");
            }
            final Product product = productOptional.get();
            //根据产品查找产品详情
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(productOptional.get().getId());
            final FirstLevelDealerGet3Response.Product productResponse = firstLevelDealerGet3Response.new Product();
            productResponse.setProductId(product.getId());
            productResponse.setProductName(product.getProductName());
            final List<FirstLevelDealerGet3Response.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail : detailList) {
                final FirstLevelDealerGet3Response.Channel channel = new FirstLevelDealerGet3Response.Channel();
                channel.setChannelType(productChannelDetail.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(productChannelDetail.getProductBalanceType());
                channels.add(channel);
            }
            firstLevelDealerGet3Response.setProduct(productResponse);
            firstLevelDealerGet3Response.setProductName("好收银");
            firstLevelDealerGet3Response.setInviteCode(dealer.getInviteCode());
            firstLevelDealerGet3Response.setInviteBtn(dealer.getInviteBtn());
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", firstLevelDealerGet3Response);
    }


    /**
     * 新增或添加代理商
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addOrUpdateHssDealer", method = RequestMethod.POST)
    public CommonResponse addOrUpdateHssDealer(@RequestBody HssDealerAddOrUpdateRequest request) {
        try{
            final Optional<Dealer> dealerOptional = this.dealerService.getById(request.getDealerId());
            if(!dealerOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            if(dealerOptional.get().getLevel()==1&&request.getRecommendBtn()==EnumRecommendBtn.ON.getId()){
                if(request.getTotalProfitSpace()==null){
                    return CommonResponse.simpleResponse(-1, "收单总分润空间不能为空");
                }
                if((request.getTotalProfitSpace()).compareTo(new BigDecimal("0.002"))>0){
                    return CommonResponse.simpleResponse(-1, "总分润空间不可高于0.2%");
                }
            }
            final HssDealerAddOrUpdateRequest.Product productParam = request.getProduct();
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
            final List<HssDealerAddOrUpdateRequest.Channel> channelParams = productParam.getChannels();
            for (HssDealerAddOrUpdateRequest.Channel channelParam : channelParams) {
                final CommonResponse commonResponse = this.checkChannel(channelParam, integerProductChannelDetailImmutableMap, product);
                if (1 != commonResponse.getCode()) {
                    return commonResponse;
                }
            }
            if(dealerOptional.get().getLevel()==1&&request.getRecommendBtn()==EnumRecommendBtn.ON.getId()){
                List<HssDealerAddOrUpdateRequest.DealerUpgradeRate> dealerUpgradeRateParams = request.getDealerUpgerdeRates();
                for (HssDealerAddOrUpdateRequest.DealerUpgradeRate dealerUpgradeRateParam : dealerUpgradeRateParams) {
                    final CommonResponse commonResponse = this.checkDealerUpgerdeRate(dealerUpgradeRateParam);
                    if (1 != commonResponse.getCode()) {
                        return commonResponse;
                    }
                }
                if(request.getRecommendBtn()!= EnumRecommendBtn.ON.getId()&&request.getRecommendBtn()!=EnumRecommendBtn.OFF.getId()){
                    return CommonResponse.simpleResponse(-1, "推荐人开关参数有误");
                }
            }
            if(request.getInviteBtn()!= EnumInviteBtn.ON.getId()&&request.getInviteBtn()!=EnumInviteBtn.OFF.getId()){
                return CommonResponse.simpleResponse(-1, "推广开关参数有误");
            }
            this.dealerService.addOrUpdateHssDealer(request);
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }

    /**
     * 新增或添加好收银代理商
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addOrUpdateHsyDealer", method = RequestMethod.POST)
    public CommonResponse addOrUpdateHsyDealer(@RequestBody HsyDealerAddOrUpdateRequest request) {
        try{
            final HsyDealerAddOrUpdateRequest.Product productParam = request.getProduct();
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
            final List<HsyDealerAddOrUpdateRequest.Channel> channelParams = productParam.getChannels();
            for (HsyDealerAddOrUpdateRequest.Channel channelParam : channelParams) {
                final CommonResponse commonResponse = this.checkChannel(channelParam, integerProductChannelDetailImmutableMap, product);
                if (1 != commonResponse.getCode()) {
                    return commonResponse;
                }
            }
            if(request.getInviteBtn()!= EnumInviteBtn.ON.getId()&&request.getInviteBtn()!=EnumInviteBtn.OFF.getId()){
                return CommonResponse.simpleResponse(-1, "推广开关参数有误");
            }
            this.dealerService.addOrUpdateHsyDealer(request);
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }

    private CommonResponse checkChannel(final HssDealerAddOrUpdateRequest.Channel paramChannel,
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

        if (paramChannel.getChannelType() == EnumPayChannelSign.YG_WEIXIN.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_WEIXIN.getId());
            final BigDecimal weixinMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weixinMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal weixinMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (weixinMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal weiXinSettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weiXinSettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || weiXinSettleRate.compareTo(weixinMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
            final BigDecimal weixinWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (weixinWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || weixinWithdrawFee.compareTo(weixinMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_ZHIFUBAO.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_ZHIFUBAO.getId());
            final BigDecimal alipayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal alipayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (alipayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal alipaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || alipaySettleRate.compareTo(alipayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal alipayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (alipayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || alipayWithdrawFee.compareTo(alipayMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }

        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_UNIONPAY.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_UNIONPAY.getId());
            final BigDecimal quickPayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal quickPayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (quickPayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal quickPaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || quickPaySettleRate.compareTo(quickPayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal quickPayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (quickPayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || quickPayWithdrawFee.compareTo(quickPayMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        }
        return CommonResponse.simpleResponse(1, "");
    }

    private CommonResponse checkChannel(final HsyDealerAddOrUpdateRequest.Channel paramChannel,
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

        if (paramChannel.getChannelType() == EnumPayChannelSign.YG_WEIXIN.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_WEIXIN.getId());
            final BigDecimal weixinMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weixinMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal weixinMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (weixinMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal weiXinSettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (weiXinSettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || weiXinSettleRate.compareTo(weixinMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
            final BigDecimal weixinWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (weixinWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || weixinWithdrawFee.compareTo(weixinMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "微信通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_ZHIFUBAO.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_ZHIFUBAO.getId());
            final BigDecimal alipayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal alipayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (alipayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal alipaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (alipaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || alipaySettleRate.compareTo(alipayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal alipayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (alipayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || alipayWithdrawFee.compareTo(alipayMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "支付宝通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }

        } else if (paramChannel.getChannelType() == EnumPayChannelSign.YG_UNIONPAY.getId()) {
            final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(EnumPayChannelSign.YG_UNIONPAY.getId());
            final BigDecimal quickPayMerchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPayMerchantSettleRate.compareTo(productChannelDetail.getProductMerchantPayRate().add(product.getLimitPayFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户支付结算费率：一级代理商的必须小于等于【产品的与支付手续费加价限额的和】");
            }
            final BigDecimal quickPayMerchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
            if (quickPayMerchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的商户提现结算费用：一级代理商的必须小于等于【产品的与提现手续费加价限额的和】");
            }
            final BigDecimal quickPaySettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                    .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            if (quickPaySettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                    || quickPaySettleRate.compareTo(quickPayMerchantSettleRate) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的支付结算费率：一级代理商的必须大于等于产品的, 小于等于商户的");

            }
            final BigDecimal quickPayWithdrawFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
            if (quickPayWithdrawFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                    || quickPayWithdrawFee.compareTo(quickPayMerchantWithdrawFee) > 0) {
                return CommonResponse.simpleResponse(-1, "快捷支付通道的提现结算费用：一级代理商的必须大于等于产品的, 小于等于商户的");
            }
        }
        return CommonResponse.simpleResponse(1, "");
    }

    private CommonResponse checkDealerUpgerdeRate(final HssDealerAddOrUpdateRequest.DealerUpgradeRate dealerUpgerdeRateParam) {
        if (org.apache.commons.lang3.StringUtils.isBlank(dealerUpgerdeRateParam.getBossDealerShareRate())) {
            return CommonResponse.simpleResponse(-1, "金开门分润比例不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(dealerUpgerdeRateParam.getFirstDealerShareProfitRate())) {
            return CommonResponse.simpleResponse(-1, "一级代理商分润比例不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(dealerUpgerdeRateParam.getSecondDealerShareProfitRate())) {
            return CommonResponse.simpleResponse(-1, "二级代理分润比例不能为空");
        }
        BigDecimal b1 = new BigDecimal(dealerUpgerdeRateParam.getBossDealerShareRate());
        BigDecimal b2 = new BigDecimal(dealerUpgerdeRateParam.getFirstDealerShareProfitRate());
        BigDecimal b3 = new BigDecimal(dealerUpgerdeRateParam.getSecondDealerShareProfitRate());
        BigDecimal b = b1.add(b2).add(b3);
        if (b.compareTo(new BigDecimal("1"))!=0) {
            return CommonResponse.simpleResponse(-1, "金开门，一级代理，二级代理的比例之和必须等于100%");
        }
        return CommonResponse.simpleResponse(1, "");
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
            request.setLoginPwd(DealerSupport.passwordDigest(request.getLoginPwd(),"JKM"));
            this.dealerService.updatePwd(request.getLoginPwd(),request.getDealerId());
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "修改成功")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }
}
