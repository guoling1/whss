package com.jkm.hss.controller.merApi;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.base.common.spring.alipay.service.AlipayOauthService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.service.*;
import com.jkm.hss.bill.service.impl.BaseHSYTransactionService;
import com.jkm.hss.bill.service.impl.BasePushAndSendService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.controller.userShop.BaseApiController;
import com.jkm.hss.helper.JKMTradeServiceException;
import com.jkm.hss.helper.JkmApiContants;
import com.jkm.hss.helper.JkmApiErrorCode;
import com.jkm.hss.helper.request.CreateApiOrderRequest;
import com.jkm.hss.helper.request.CreateOrderRequest;
import com.jkm.hss.helper.response.CreateApiOrderResponse;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.UserCurrentChannelPolicy;
import com.jkm.hsy.user.service.HsyShopService;
import com.jkm.hsy.user.service.UserCurrentChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by yuxiang on 2017-08-13.
 */
@Slf4j
@Controller
@RequestMapping(value = "/merchantApi")
public class MerchantApiController extends BaseApiController {

    @Autowired
    private HSYTradeService hsyTradeService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private HSYTransactionService hsyTransactionService;
    @Autowired
    private HsyBalanceAccountEmailService hsyBalanceAccountEmailService;
    @Autowired
    private PushService pushService;
    @Autowired
    private HSYOrderService hsyOrderService;
    @Autowired
    private BaseHSYTransactionService baseHSYTransactionService;
    @Autowired
    private BasePushAndSendService basePushAndSendService;
    @Autowired
    private HsyShopService hsyShopService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private UserCurrentChannelPolicyService userCurrentChannelPolicyService;
    @Autowired
    private AlipayOauthService alipayOauthService;

    @RequestMapping(value = "toJsp", method = RequestMethod.GET)
    public String toJsp() {

        return "/api";
    }
    /**
     * 商户API 下单
     *
     * @param createApiOrderRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/code/jsapi", method = RequestMethod.POST)
    public String createApiOrder(@RequestBody final CreateApiOrderRequest createApiOrderRequest) {
        log.info("【商户[{}]API下单】开始", createApiOrderRequest.getMerchantNo());
        Long startTime = System.currentTimeMillis();
        CreateApiOrderResponse createApiOrderResponse = new CreateApiOrderResponse();
        try{
           //校验签名
           final boolean signTrue = createApiOrderRequest.isSignTrue(JkmApiContants.DEALER_SIGN_KEY);
           if (!signTrue){
               //签名错误
               createApiOrderResponse.setReturnCode(JkmApiErrorCode.FAIL.getErrorCode());
               createApiOrderResponse.setReturnMsg(JkmApiErrorCode.FAIL.getErrorMessage());
               return JSONObject.toJSONString(createApiOrderResponse);
           }
           //参数校验
           createApiOrderRequest.validate();

            log.info("#【API下单】--merchantNo:" + createApiOrderRequest.getMerchantNo() + ",merchantOrderNo:" + createApiOrderRequest.getOrderNum() +",startLong:" + startTime);
            //下业务订单
                //参数转换
            AppAuUser appAuUser =  this.hsyShopService.findUserByGlobalId(createApiOrderRequest.getMerchantNo());
            final AppBizShop appBizShop = new AppBizShop();
            appBizShop.setUid(appAuUser.getId());
            appBizShop.setType(1);
            final AppBizShop priShop = this.hsyShopDao.findPrimaryAppBizShopByUserID(appBizShop).get(0);
            final UserCurrentChannelPolicy userCurrentChannelPolicy = this.userCurrentChannelPolicyService.selectByUserId(appAuUser.getId()).get();
            int channel = 0;
            if (createApiOrderRequest.getTrxType().equals("WX_SCANCODE_JSAPI")){
                channel = userCurrentChannelPolicy.getWechatChannelTypeSign();
            }else if (createApiOrderRequest.getTrxType().equals("Alipay_SCANCODE_JSAPI")){
                channel = userCurrentChannelPolicy.getAlipayChannelTypeSign();
            }
            final long hsyOrderId = this.hsyTransactionService.
                    createOrderToApi(channel , priShop.getId(), createApiOrderRequest.getOrderNum(),createApiOrderRequest.getAmount(),createApiOrderRequest.getGoodsName(),createApiOrderRequest.getCallbackUrl());

            final HsyOrder hsyOrder = this.hsyOrderService.getById(hsyOrderId).get();
            //("payUrl", URLDecoder.decode(resultPair.getMiddle(), "UTF-8"))
                //下单成功
                createApiOrderResponse.setAmount(hsyOrder.getAmount().toString());
                createApiOrderResponse.setOrderNum(hsyOrder.getOrdernumber());
                createApiOrderResponse.setTradeOrderNo(hsyOrder.getOrderno());
                createApiOrderResponse.setQrCode("http://hsy.qianbaojiajia.com/sqb/codeapi?payInfo=oCSt1wQtuV7G_GQ_qCyWf0qN"+ "&hsyOrderId=" + hsyOrder.getId());
                createApiOrderResponse.setReturnCode(JkmApiErrorCode.SUCCESS.getErrorCode());
                createApiOrderResponse.setReturnMsg("下单成功");
        } catch (JKMTradeServiceException e) {
            log.error("#【API下单】controller.createApiOrder.JKMTradeServiceException", e);
            createApiOrderResponse.setResponse(e.getJKMTradeErrorCode());
        } catch (Exception e) {
            log.error("#【API下单】controller.createApiOrder.Exception", e);
            createApiOrderResponse.setResponse(JkmApiErrorCode.SYS_ERROR);
        }
        //结果返回
        //createApiOrderResponse = afterComplete();
        Long endTime = System.currentTimeMillis();
        log.info("#【API下单】merchantNo:" + createApiOrderRequest.getMerchantNo() + ",merchantOrderNo:" + createApiOrderRequest.getOrderNum() + ",endTime:" + endTime + ",totalTime:" + (endTime - startTime) + "ms");
		return JSONObject.toJSONString(createApiOrderResponse);
    }


    /**
     * userid
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userIdBack", method = RequestMethod.GET)
    public CreateApiOrderResponse payUserIdOrder(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws UnsupportedEncodingException {
        Long startTime = System.currentTimeMillis();
        log.info("支付宝回调获取USERID");
        CreateApiOrderResponse createApiOrderResponse = new CreateApiOrderResponse();
        try{
            Preconditions.checkState(request.getQueryString() != null, "支付宝授权失败");
            String getQueryString = "";
            if(request.getQueryString() == null){
                getQueryString="";
            }else{
                getQueryString = request.getQueryString();
            }
            String[] arr = getQueryString.split("&");
            String authcode="";
            String state = "";
            for(int i =0;i<arr.length;i++){
                if("auth_code".equals(arr[i].split("=")[0])){
                    authcode = arr[i].split("=")[1];
                    log.info("authcode是:{}",authcode);
                }
                if("state".equals(arr[i].split("=")[0])){
                    state = arr[i].split("=")[2];
                    log.info("state参数是:{}",state);
                }
            }
            String userId = alipayOauthService.getUserId(authcode);
            final HsyOrder hsyOrder = this.hsyOrderService.getById(Integer.valueOf(state)).get();
            hsyOrder.setMemberId(userId);
            this.hsyOrderService.update(hsyOrder);
            final Triple<Integer, String, String> resultPair = this.hsyTransactionService.placeOrder(hsyOrder.getAmount().toString(), hsyOrder.getId(),hsyOrder.getAmount(),null,null,null,null);
            if (0 == resultPair.getLeft()) {
                //("payUrl", URLDecoder.decode(resultPair.getMiddle(), "UTF-8"))
                //下单成功
                createApiOrderResponse.setAmount(hsyOrder.getAmount().toString());
                createApiOrderResponse.setOrderNum(hsyOrder.getOrdernumber());
                createApiOrderResponse.setTradeOrderNo(hsyOrder.getOrderno());
                createApiOrderResponse.setQrCode("http://hsy.qianbaojiajia.com/sqb/wxapi?payInfo="+URLDecoder.decode(resultPair.getMiddle(), "UTF-8")+ "&hsyOrderId=" + hsyOrder.getId());
                createApiOrderResponse.setReturnCode(JkmApiErrorCode.SUCCESS.getErrorCode());
                createApiOrderResponse.setReturnMsg("下单成功");
            }else{
                //下单失败
                createApiOrderResponse.setReturnCode(JkmApiErrorCode.FAIL.getErrorCode());
                createApiOrderResponse.setReturnMsg("下单失败");
            }

        } catch (JKMTradeServiceException e) {
            log.error("#【微信回调获取OPENID并下单】controller.payOpenIdOrder.JKMTradeServiceException", e);
            createApiOrderResponse.setResponse(e.getJKMTradeErrorCode());
        } catch (Exception e) {
            log.error("#【微信回调获取OPENID并下单】controller.payOpenIdOrder.Exception", e);
            createApiOrderResponse.setResponse(JkmApiErrorCode.SYS_ERROR);
        }
        //结果返回
        createApiOrderResponse = afterComplete();
        Long endTime = System.currentTimeMillis();
        log.info("#【微信回调获取OPENID并下单】merchantOrderNo:" + createApiOrderResponse.getOrderNum() + ",endTime:" + endTime + ",totalTime:" + (endTime - startTime) + "ms");
        return createApiOrderResponse;
    }

}
