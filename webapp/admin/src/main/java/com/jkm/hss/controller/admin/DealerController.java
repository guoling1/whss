package com.jkm.hss.controller.admin;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.ListDealerRequest;
import com.jkm.hss.dealer.helper.requestparam.ListFirstDealerRequest;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.DealerUpgerdeRateService;
import com.jkm.hss.helper.request.FirstLevelDealerFindRequest;
import com.jkm.hss.helper.response.DealerDetailResponse;
import com.jkm.hss.helper.response.FirstLevelDealerFindResponse;
import com.jkm.hss.helper.response.FirstLevelDealerGetResponse;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.servcie.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
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

}
