package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.enums.EnumIsMaster;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.admin.helper.requestparam.*;
import com.jkm.hss.admin.helper.responseparam.*;
import com.jkm.hss.admin.service.AdminRoleService;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.entity.OemInfo;
import com.jkm.hss.dealer.enums.*;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.*;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.dealer.helper.response.OemDetailResponse;
import com.jkm.hss.dealer.helper.response.SecondDealerResponse;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.DealerUpgerdeRateService;
import com.jkm.hss.dealer.service.OemInfoService;
import com.jkm.hss.helper.ApplicationConsts;
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

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private OemInfoService oemInfoService;

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
     * 根据代理商编码查找代理商信息
     * @param dealerId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findBydealerId/{dealerId}", method = RequestMethod.GET)
    public CommonResponse findBydealerId(@PathVariable final long dealerId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商或分公司不存在");
        }
        final Dealer dealer = dealerOptional.get();
        final DealerDetailResponse dealerDetailResponse = new DealerDetailResponse();
        dealerDetailResponse.setId(dealer.getId());
        dealerDetailResponse.setMobile(dealer.getMobile());
        dealerDetailResponse.setName(dealer.getProxyName());
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByDealerIdAndIsMaster(dealerId, EnumIsMaster.MASTER.getCode());
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
    public CommonResponse dealerDetail(@PathVariable final long dealerId,@PathVariable final long productId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        if (dealerOptional.get().getOemType()!= EnumOemType.DEALER.getId()) {
            return CommonResponse.simpleResponse(-1, "不属于代理商，属于分公司");
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
     * hss查询分公司信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/oem/{dealerId}/{productId}", method = RequestMethod.GET)
    public CommonResponse oemDetail(@PathVariable final long dealerId,@PathVariable final long productId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        if (!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "分公司不存在");
        }
        if (dealerOptional.get().getOemType()!= EnumOemType.OEM.getId()) {
            return CommonResponse.simpleResponse(-1, "不属于分公司，属于代理商");
        }
        final OemHssResponse oemHssResponse = new OemHssResponse();
        if(productId>0){//修改
            Optional<Product> productOptional = this.productService.selectById(productId);
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "好收收产品配置有误");
            }
            if(!(EnumProductType.HSS.getId()).equals(productOptional.get().getType())){
                return CommonResponse.simpleResponse(-1, "该产品不属于好收收");
            }
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(productId);
            final OemHssResponse.Product productResponse = oemHssResponse.new Product();
            productResponse.setProductId(productId);
            productResponse.setProductName(productOptional.get().getProductName());
            final List<OemHssResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail:detailList) {
                final OemHssResponse.Channel channel = new OemHssResponse.Channel();
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
            oemHssResponse.setProduct(productResponse);
            oemHssResponse.setProductName("好收收");
        }else{//新增
            Optional<Product> productOptional = this.productService.selectByType(EnumProductType.HSS.getId());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "好收收产品不存在");
            }
            final Product product = productOptional.get();
            //根据产品查找产品详情
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(productOptional.get().getId());
            final OemHssResponse.Product productResponse = oemHssResponse.new Product();
            productResponse.setProductId(product.getId());
            productResponse.setProductName(product.getProductName());
            final List<OemHssResponse.Channel> channels = new ArrayList<>();
            productResponse.setChannels(channels);
            for (ProductChannelDetail productChannelDetail : detailList) {
                final OemHssResponse.Channel channel = new OemHssResponse.Channel();
                channel.setChannelType(productChannelDetail.getChannelTypeSign());
                Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
                channel.setChannelName(basicChannelOptional.get().getChannelName());
                channel.setSettleType(productChannelDetail.getProductBalanceType());
                channels.add(channel);
            }
            oemHssResponse.setProduct(productResponse);
            oemHssResponse.setProductName("好收收");
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", oemHssResponse);
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
     * 新增或添加好收收配置
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
     * 新增或添加分公司配置
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addOrUpdateHssOem", method = RequestMethod.POST)
    public CommonResponse addOrUpdateHssOem(@RequestBody HssOemAddOrUpdateRequest request) {
        try{
            final Optional<Dealer> dealerOptional = this.dealerService.getById(request.getDealerId());
            if(!dealerOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }

            final HssOemAddOrUpdateRequest.Product productParam = request.getProduct();
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
            final List<HssOemAddOrUpdateRequest.Channel> channelParams = productParam.getChannels();
            for (HssOemAddOrUpdateRequest.Channel channelParam : channelParams) {
                final CommonResponse commonResponse = this.checkChannel(channelParam, integerProductChannelDetailImmutableMap, product);
                if (1 != commonResponse.getCode()) {
                    return commonResponse;
                }
            }
            this.dealerService.addOrUpdateHssOem(request);
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息是",e.getStackTrace());
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

    private CommonResponse checkChannel(final HssOemAddOrUpdateRequest.Channel paramChannel,
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
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的商户支付结算费率：分公司的必须小于等于【产品的与支付手续费加价限额的和】");
        }
        final BigDecimal merchantWithdrawFee = new BigDecimal(paramChannel.getMerchantWithdrawFee());
        if (merchantWithdrawFee.compareTo(productChannelDetail.getProductMerchantWithdrawFee().add(product.getLimitWithdrawFeeRate())) > 0) {
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的商户提现结算费用：分公司的必须小于等于【产品的与提现手续费加价限额的和】");
        }
        final BigDecimal paymentSettleRate = new BigDecimal(paramChannel.getPaymentSettleRate())
                .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
        if (paymentSettleRate.compareTo(productChannelDetail.getProductTradeRate()) < 0
                || paymentSettleRate.compareTo(merchantSettleRate) > 0) {
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的支付结算费率：分公司的必须大于等于产品的, 小于等于商户的");
        }
        final BigDecimal withdrawSettleFee = new BigDecimal(paramChannel.getWithdrawSettleFee());
        if (withdrawSettleFee.compareTo(productChannelDetail.getProductWithdrawFee()) < 0
                || withdrawSettleFee.compareTo(merchantWithdrawFee) > 0) {
            return CommonResponse.simpleResponse(-1, basicChannelOptional.get().getChannelName()+"通道的提现结算费用：分公司的的必须大于等于产品的, 小于等于商户的");
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

        Optional<BasicChannel> basicChannelOptional = basicChannelService.selectByChannelTypeSign(paramChannel.getChannelType());
        if(!basicChannelOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "没有"+paramChannel.getChannelType()+"通道基础配置");
        }

        final ProductChannelDetail productChannelDetail = integerProductChannelDetailImmutableMap.get(paramChannel.getChannelType());
        final BigDecimal merchantSettleRate = new BigDecimal(paramChannel.getMerchantSettleRate())
                .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
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
                return CommonResponse.simpleResponse(-1, "登录用户不存在");
            }
            if(org.apache.commons.lang3.StringUtils.isBlank(request.getLoginPwd())) {
                return CommonResponse.simpleResponse(-1, "登录密码不能为空");
            }
            request.setLoginPwd(DealerSupport.passwordDigest(request.getLoginPwd(),"JKM"));
            adminUserService.updateDealerUserPwd(request.getLoginPwd(),request.getDealerId());
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "修改成功")
                    .addParam("dealerId", request.getDealerId()).build();
        }catch (Exception e){
            log.error("错误信息时",e.getStackTrace());
            return CommonResponse.simpleResponse(-1, e.getMessage());
        }
    }


    /**
     * 根据代理商编码查询代理商信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDealerByMarkCode", method = RequestMethod.POST)
    public CommonResponse getDealerByMarkCode(@RequestBody DealerMarkCodeRequest request) {
        Optional<Dealer> dealerOptional = dealerService.getDealerByMarkCode(request.getMarkCode());
        if(dealerOptional.isPresent()){
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerOptional.get().getProxyName());
        }else{
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", "");
        }
    }







    //用户和权限管理
    /**
     * 用户列表
     * @param adminDealerUserListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public CommonResponse userList (@RequestBody AdminDealerUserListRequest adminDealerUserListRequest) {
        PageModel<AdminUserDealerListResponse> adminUserPageModel = adminUserService.userDealerList(adminDealerUserListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserPageModel);
    }

    /**
     * 禁用用户
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableUser", method = RequestMethod.POST)
    public CommonResponse disableUser (final HttpServletResponse response, @RequestBody AdminUserRequest adminUserRequest) {
        adminUserService.disableUser(adminUserRequest.getId());
        Optional<AdminUser> adminUserOptional = adminUserService.getAdminUserById(adminUserRequest.getId());
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
        Optional<Dealer> dealerOptional = dealerService.getById(adminUserOptional.get().getDealerId());
        if(!dealerOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }

        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(dealerOptional.get().getLevel()==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(dealerOptional.get().getLevel()==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        final long proxyNameCount = this.adminUserService.selectByUsernameAndTypeUnIncludeNow(adminUser.getUsername(),type, adminUser.getId());
        if (proxyNameCount > 0) {
            return CommonResponse.simpleResponse(-1, "登录名已经存在");
        }
        adminUserService.update(adminUser);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
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
        adminUserResponse.setType(adminUserOptional.get().getType());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserResponse);
    }


    /**
     * 角色详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRoleDetail", method = RequestMethod.POST)
    public CommonResponse getRoleDetail (@RequestBody AdminRoleAndTypeDetailRequest adminRoleAndTypeDetailRequest) {
        RoleDetailResponse roleDetailResponse = new RoleDetailResponse();
        if(adminRoleAndTypeDetailRequest.getId()>0){
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectById(adminRoleAndTypeDetailRequest.getId());
            if(!adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色不存在");
            }
            roleDetailResponse.setRoleId(adminRoleAndTypeDetailRequest.getId());
            roleDetailResponse.setRoleName(adminRoleOptional.get().getRoleName());
        }
        List<AdminMenuOptRelListResponse> adminMenuOptRelListResponses = adminRoleService.getPrivilege(adminRoleAndTypeDetailRequest.getType(),adminRoleAndTypeDetailRequest.getId());
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
        if(roleDetailRequest.getType()<=1){
            return CommonResponse.simpleResponse(-1, "请选择代理商类型");
        }
        if(roleDetailRequest.getRoleName()==null||"".equals(roleDetailRequest.getRoleName())){
            return CommonResponse.simpleResponse(-1, "请输入角色名");
        }
        if(roleDetailRequest.getList()==null||roleDetailRequest.getList().size()<=0){
            return CommonResponse.simpleResponse(-1, "请选择菜单");
        }
        if(roleDetailRequest.getRoleId()<=0){
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectByRoleNameAndType(roleDetailRequest.getRoleName(),roleDetailRequest.getType());
            if(adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色名已存在");
            }
        }else{
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectByRoleNameAndTypeUnIncludeNow(roleDetailRequest.getRoleName(),roleDetailRequest.getType(),roleDetailRequest.getRoleId());
            if(adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色名已存在");
            }
        }
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
        PageModel<AdminRoleListPageResponse> adminUserPageModel = adminRoleService.roleDealerListByPage(adminRoleListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserPageModel);
    }

    /**
     * 禁用角色
     * @param adminRoleDetailRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableRole", method = RequestMethod.POST)
    public CommonResponse disableRole (final HttpServletResponse response, @RequestBody AdminRoleDetailRequest adminRoleDetailRequest) {
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
     * 角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userRoleList/{type}", method = RequestMethod.POST)
    public CommonResponse userRoleList (@PathVariable final int type) {
        List<AdminRoleListResponse> adminRoleListResponses = adminRoleService.selectAdminRoleList(type);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminRoleListResponses);
    }

    /**
     * 代理商用户修改密码
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatePwdById", method = RequestMethod.POST)
    public CommonResponse updatePwdById (@RequestBody AdminUserRequest adminUserRequest) {
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
        adminUserService.updateDealerUserPwdById(DealerSupport.passwordDigest(adminUserRequest.getPassword(),"JKM"),adminUserRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
    }


    /**
     * hss查询分公司信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/oemDetail/{dealerId}", method = RequestMethod.GET)
    public CommonResponse oemDetail(@PathVariable final long dealerId) {
        OemDetailResponse oemDetailResponse = oemInfoService.selectByDealerId(dealerId);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", oemDetailResponse);
    }
    /**
     * 配置O单
     * @param addOrUpdateOemRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addOrUpdateOem", method = RequestMethod.POST)
    public CommonResponse addOrUpdateOem (@RequestBody AddOrUpdateOemRequest addOrUpdateOemRequest) {
        if(addOrUpdateOemRequest.getDealerId()<=0){
            return CommonResponse.simpleResponse(-1, "分公司不存在");
        }
        if(StringUtils.isEmpty(addOrUpdateOemRequest.getBrandName())){
            return CommonResponse.simpleResponse(-1, "品牌名称不能为空");
        }
        if(addOrUpdateOemRequest.getBrandName().length()>4){
            return CommonResponse.simpleResponse(-1, "品牌名称不能大于4个字");
        }
        if(StringUtils.isEmpty(addOrUpdateOemRequest.getWechatName())){
            return CommonResponse.simpleResponse(-1, "微信公众号名称不能为空");
        }
        if(StringUtils.isEmpty(addOrUpdateOemRequest.getWechatCode())){
            return CommonResponse.simpleResponse(-1, "微信号不能为空");
        }
        if(StringUtils.isEmpty(addOrUpdateOemRequest.getAppId())){
            return CommonResponse.simpleResponse(-1, "微信AppID不能为空");
        }
        if(StringUtils.isEmpty(addOrUpdateOemRequest.getAppSecret())){
            return CommonResponse.simpleResponse(-1, "微信AppSecret不能为空");
        }
        if(StringUtils.isEmpty(addOrUpdateOemRequest.getQrCode())){
            return CommonResponse.simpleResponse(-1, "公众号二维码不能为空");
        }
        if(addOrUpdateOemRequest.getTemplateInfos().size()<=0){
            return CommonResponse.simpleResponse(-1, "消息模板不能为空");
        }
        for(int i=0;i<addOrUpdateOemRequest.getTemplateInfos().size();i++){
            if(StringUtils.isEmpty(addOrUpdateOemRequest.getTemplateInfos().get(i).getTemplateId())){
                return CommonResponse.simpleResponse(-1, "模板ID不能为空");
            }
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "配置成功");
    }
}
