package com.jkm.hss.controller.app;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.DateUtil;
import com.jkm.hss.account.entity.SplitAccountRecord;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.PartnerShallProfitDetailService;
import com.jkm.hss.helper.request.DynamicCodePayRequest;
import com.jkm.hss.helper.request.PartnerShallRequest;
import com.jkm.hss.helper.response.MerchantChannelResponse;
import com.jkm.hss.helper.response.PartnerShallResponse;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.request.CheckMerchantInfoRequest;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.product.entity.*;
import com.jkm.hss.product.enums.EnumGatewayType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ChannelSupportDebitCardService;
import com.jkm.hss.product.servcie.ProductChannelGatewayService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-08-10.
 */
@Slf4j
@Controller
@RequestMapping(value = "appAccount")
public class AppAccountController extends BaseController {

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private ProductChannelGatewayService productChannelGatewayService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private ChannelSupportDebitCardService channelSupportDebitCardService;
    @Autowired
    private PartnerShallProfitDetailService partnerShallProfitDetailService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
    @Autowired
    private OrderService orderService;
    /**
     * HSSH5001020  网关通道列表
     * @param payRequest
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/channelList", method = RequestMethod.POST)
    public CommonResponse getChannelList(@RequestBody final DynamicCodePayRequest payRequest,
                                         final HttpServletRequest request){
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());

        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }

        //获取该商户对应的产品通道网关
        final Product product = this.productService.selectByType(EnumProductType.HSS.getId()).get();
        final List<ProductChannelGateway> productChannelGatewayList =
                this.productChannelGatewayService.selectByProductTypeAndGatewayAndProductIdAndDealerId(
                        EnumProductType.HSS, EnumGatewayType.PRODUCT, product.getId(), merchantInfo.getOemId());

        final List<MerchantChannelRate> merchantChannelRateList = this.merchantChannelRateService.selectByMerchantId(merchantInfo.getId());
        Map<Integer, MerchantChannelRate> merchantChannelRateMap = Maps.uniqueIndex(merchantChannelRateList, new Function<MerchantChannelRate, Integer>() {
            @Override
            public Integer apply(MerchantChannelRate input) {
                return input.getChannelTypeSign();
            }
        });

        final List<MerchantChannelResponse> merchantChannelResponseList = new ArrayList<>();
        for (ProductChannelGateway productChannelGateway : productChannelGatewayList){

            final int channelSign = productChannelGateway.getChannelSign();
            final MerchantChannelRate merchantChannelRate = merchantChannelRateMap.get(channelSign);
            if (merchantChannelRate == null){
                continue;
            }
            final BasicChannel parentChannel = this.basicChannelService.selectParentChannel(channelSign);
            final MerchantChannelResponse merchantChannelResponse = new MerchantChannelResponse();
            //log.info("》》》》》》》》》》》》》》" + parentChannel.toString());
            merchantChannelResponse.setPayMethod(EnumPayChannelSign.idOf(parentChannel.getChannelTypeSign()).getPaymentChannel().getValue());
            merchantChannelResponse.setPayMethodCode(EnumPayChannelSign.idOf(parentChannel.getChannelTypeSign()).getPaymentChannel().getId());
            merchantChannelResponse.setChannelName(productChannelGateway.getViewChannelName());
            merchantChannelResponse.setChannelRate(merchantChannelRate.getMerchantPayRate().toString());
            merchantChannelResponse.setFee(merchantChannelRate.getMerchantWithdrawFee().toString());
            merchantChannelResponse.setChannelSign(channelSign);
            merchantChannelResponse.setLimitAmount(parentChannel.getLimitAmount().toString());
            merchantChannelResponse.setSettleType(merchantChannelRate.getMerchantBalanceType());
            merchantChannelResponse.setRecommend(productChannelGateway.getRecommend());
            merchantChannelResponseList.add(merchantChannelResponse);
        }

        return CommonResponse.objectResponse(1,"SUCCESS", merchantChannelResponseList);
    }

    /**
     * 查询通道是否可用 HSSH5001021
     * @param request
     * @param response
     * @param checkMerchantInfoRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkMerchantInfo1", method = RequestMethod.POST)
    public CommonResponse checkMerchantInfo1(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final CheckMerchantInfoRequest checkMerchantInfoRequest) {
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());

        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        if(checkMerchantInfoRequest.getChannelTypeSign()==EnumPayChannelSign.MB_UNIONPAY_DO.getId()){
            boolean b = DateUtil.isInDate(new Date(),"09:00:00","22:25:00");
            if(!b)return CommonResponse.simpleResponse(-1, "本通道只可在09:00至22:25使用");
        }

        MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
        merchantChannelRateRequest.setMerchantId(merchantInfo.get().getId());
        merchantChannelRateRequest.setProductId(merchantInfo.get().getProductId());
        merchantChannelRateRequest.setChannelTypeSign(checkMerchantInfoRequest.getChannelTypeSign());
        Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
        if(!merchantChannelRateOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "通道信息配置有误");
        }
        if(merchantChannelRateOptional.get().getChannelCompany()==null||"".equals(merchantChannelRateOptional.get().getChannelCompany())){
            log.info("上游通道公司为空");
            return CommonResponse.simpleResponse(-1, "通道信息配置有误");
        }
        MerchantChannelRate merchantChannelRate = merchantChannelRateOptional.get();
        final AccountBank accountBank = this.accountBankService.getDefault(merchantInfo.get().getAccountId());
        //hlb通道结算卡拦截
        if (checkMerchantInfoRequest.getChannelTypeSign() == EnumPayChannelSign.HE_LI_UNIONPAY.getId()){
            final Optional<ChannelSupportDebitCard> channelSupportDebitCardOptional = this.channelSupportDebitCardService.selectByBankCode(accountBank.getBankBin());
            if (!channelSupportDebitCardOptional.isPresent()){
                //通道结算卡不可用
                return CommonResponse.simpleResponse(-1, "该通道仅支持结算到大型银行，请联系客服更改结算卡再使用");
            }
        }
        //通道限额拦截，通道可用拦截，
        final BasicChannel basicChannel =
                this.basicChannelService.selectByChannelTypeSign(checkMerchantInfoRequest.getChannelTypeSign()).get();
        if (basicChannel.getIsUse() == EnumBoolean.FALSE.getCode()){
            //通道不可用
            return CommonResponse.simpleResponse(-1, "通道维护中");
        }
        if (checkMerchantInfoRequest.getAmount().compareTo(basicChannel.getLimitMinAmount()) == -1){
            return CommonResponse.simpleResponse(-1, "支付金额至少" + basicChannel.getLimitMinAmount() + "元");
        }
        if (checkMerchantInfoRequest.getAmount().compareTo(basicChannel.getLimitAmount()) == 1){
            return CommonResponse.simpleResponse(-1, "通道单笔限额" + basicChannel.getLimitAmount() + "元");
        }

        if(merchantChannelRate.getEnterNet()== EnumEnterNet.UNSUPPORT.getId()){
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "无需入网");
        }

        if(accountBank.getBranchCode()==null||"".equals(accountBank.getBranchCode())){
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "支行信息不完善",accountBank.getId());
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.ENTING.getId()){
            log.info("商户入网中");
            return CommonResponse.simpleResponse(-1, "入网申请中");
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.ENT_FAIL.getId()){
            log.info("商户入网失败");
            if(merchantChannelRate.getRemarks()!=null&&merchantChannelRate.getRemarks().contains("重复")){
                return CommonResponse.simpleResponse(-1, "底层通道检测到您为重复入网，请使用其他通道");
            }else{
                return CommonResponse.simpleResponse(-1, "入网失败，请使用其他通道");
            }

        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.HASENT.getId()){
            log.info("商户已入网");
            // 活动通道卡盟需更新一次上游结算费率
            if ( (merchantChannelRate.getRemarks() == null) || (! merchantChannelRate.getRemarks().equals("已同步"))){
                log.info("去卡盟上游同步费率");
                final JSONObject jo = this.merchantChannelRateService.updateKmMerchantRateInfo(merchantInfo.get().getAccountId(), merchantInfo.get().getId(), merchantInfo.get().getProductId(), checkMerchantInfoRequest.getChannelTypeSign());
                if(jo.getInt("code")==-1){
                    return CommonResponse.simpleResponse(-1, "请稍后再试");
                }else{
                    return CommonResponse.simpleResponse(jo.getInt("code"), jo.getString("msg"));
                }
            }
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "商户已入网");
        }
        if(merchantChannelRate.getEnterNet()==EnumEnterNet.UNENT.getId()) {
            log.info("商户需入网");
            JSONObject jo = merchantChannelRateService.enterInterNet1(merchantInfo.get().getAccountId(),merchantInfo.get().getProductId(),merchantInfo.get().getId(),merchantChannelRateOptional.get().getChannelCompany());
            if(jo.getInt("code")==-1&&jo.getString("msg")!=null&&!"".equals(jo.getString("msg"))&&jo.getString("msg").contains("重复")){
                return CommonResponse.simpleResponse(-1, "底层通道检测到您为重复入网，请使用其他通道");
            }else{
                return CommonResponse.simpleResponse(jo.getInt("code"), jo.getString("msg"));
            }
        }
        return null;
    }

    /**
     * HSSH5001025 获取合伙人分润
     * @param request
     * @param shallRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryShall", method = RequestMethod.POST)
    public CommonResponse queryShall(final HttpServletRequest request, @RequestBody final PartnerShallRequest shallRequest){

        final Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        //final Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(72);
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!=EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }

        if (shallRequest.getType() == 1){
            //推荐人分润
            shallRequest.setMerchantId(merchantInfo.get().getId());
            PageModel<PartnerShallProfitDetail> pageModel = this.partnerShallProfitDetailService.
                    getPartnerShallProfitList(shallRequest.getMerchantId(), shallRequest.getShallId(),shallRequest.getPageNo() ,shallRequest.getPageSize());
            final BigDecimal totalProfit = this.partnerShallProfitDetailService.selectTotalProfitByMerchantId(shallRequest.getMerchantId());

            final List<PartnerShallProfitDetail> records = pageModel.getRecords();

            List<JSONObject> list = Lists.transform(records, new Function<PartnerShallProfitDetail, JSONObject>() {
                @Override
                public JSONObject apply(PartnerShallProfitDetail input) {
                    JSONObject jsonObject = new JSONObject();
                    if (input.getFirstMerchantId() == shallRequest.getMerchantId()){
                        jsonObject.put("type","1");
                        jsonObject.put("name",input.getMerchantName());
                        jsonObject.put("date", input.getCreateTime().getTime());
                        jsonObject.put("money", input.getFirstMerchantShallAmount());
                        jsonObject.put("shallId", input.getId());
                    }else{
                        jsonObject.put("type","2");
                        jsonObject.put("name",getInDirectName(input.getMerchantName()));
                        jsonObject.put("date", input.getCreateTime().getTime());
                        jsonObject.put("money", input.getSecondMerchantShallAmount());
                        jsonObject.put("shallId", input.getId());
                    }

                    return jsonObject;
                }
            });
            PageModel<JSONObject> model = new PageModel<>(shallRequest.getPageNo(),shallRequest.getPageSize());
            model.setRecords(list);
            model.setCount(pageModel.getCount());
            model.setHasNextPage(pageModel.isHasNextPage());
            model.setPageSize(pageModel.getPageSize());
            PartnerShallResponse response = new PartnerShallResponse();
            response.setPageModel(model);
            response.setTotalShall(String.valueOf(totalProfit));
            response.setType(1);

            return CommonResponse.objectResponse(1,"success", response);
        }else if (shallRequest.getType() == 2){
            final Long dealerId = merchantInfo.get().getSuperDealerId();
            final Dealer dealer = this.dealerService.getById(dealerId).get();
            //合伙人分润
            if ( dealerId == null){
                return CommonResponse.objectResponse(-1,"未开通合伙人",null);
            }
            PageModel<SplitAccountRecord> pageModel = this.splitAccountRecordService.
                    getDealerShallProfitList(dealer.getAccountId(), shallRequest.getShallId(),shallRequest.getPageNo() ,shallRequest.getPageSize());

            final List<SplitAccountRecord> records = pageModel.getRecords();

            List<JSONObject> list = Lists.transform(records, new Function<SplitAccountRecord, JSONObject>() {
                @Override
                public JSONObject apply(SplitAccountRecord input) {
                    final String orderNo = input.getOrderNo();
                    final Order order = orderService.getByOrderNo(orderNo).get();
                    final MerchantInfo payMerchant = merchantInfoService.getByAccountId(order.getPayee()).get();
                    JSONObject jsonObject = new JSONObject();
                    if (payMerchant.getFirstMerchantId() == 0){
                        //直属商户
                        jsonObject.put("type","1");
                        jsonObject.put("name",payMerchant.getMerchantName());
                        jsonObject.put("date", input.getCreateTime().getTime());
                        jsonObject.put("money", input.getSplitAmount());
                        jsonObject.put("shallId", input.getId());
                    }else{
                        jsonObject.put("type","2");
                        jsonObject.put("name",getInDirectName(payMerchant.getMerchantName()));
                        jsonObject.put("date", input.getCreateTime().getTime());
                        jsonObject.put("money", input.getSplitAmount());
                        jsonObject.put("shallId", input.getId());
                    }

                    return jsonObject;
                }
            });
            PageModel<JSONObject> model = new PageModel<>(shallRequest.getPageNo(),shallRequest.getPageSize());
            model.setRecords(list);
            model.setCount(pageModel.getCount());
            model.setHasNextPage(pageModel.isHasNextPage());
            model.setPageSize(pageModel.getPageSize());
            PartnerShallResponse response = new PartnerShallResponse();
            response.setPageModel(model);
            response.setTotalShall("");
            response.setType(2);
            return CommonResponse.objectResponse(1,"success", response);
        }else{
            PartnerShallResponse response = new PartnerShallResponse();
            response.setPageModel(new PageModel<JSONObject>());
            response.setTotalShall("");
            response.setType(3);
            return CommonResponse.objectResponse(1,"success", response);
         }
    }

    private String getInDirectName(String name){
        final int length = name.length();
        if (length <= 2){
            return "*" + name.charAt(1);
        }else if (length == 3){

            return "**" + name.charAt(2);
        }
        return "**" + name.charAt(length - 1);
    }
}
