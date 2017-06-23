package com.jkm.hss.bill.service.impl;

import com.google.gson.*;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.dao.HsyOrderDao;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.enums.EnumBasicStatus;
import com.jkm.hss.bill.enums.EnumHsySourceType;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumServiceType;
import com.jkm.hss.bill.helper.PayParams;
import com.jkm.hss.bill.helper.PayResponse;
import com.jkm.hss.bill.service.HsyOrderScanService;
import com.jkm.hss.bill.service.TradeService;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.UserChannelPolicyDao;
import com.jkm.hsy.user.dao.UserCurrentChannelPolicyDao;
import com.jkm.hsy.user.dao.UserTradeRateDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.util.AppDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Allen on 2017/6/20.
 */
@Service("hsyOrderScanService")
public class HsyOrderScanServiceImpl implements HsyOrderScanService {

    public static String wechatPstr = "^1[0-5][0-9]{16}$";
    public static String alipayPstr = "^(2[5-9]|30)[0-9]{14,22}$";

    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyOrderDao hsyOrderDao;
    @Autowired
    private UserCurrentChannelPolicyDao userCurrentChannelPolicyDao;
    @Autowired
    private UserChannelPolicyDao userChannelPolicyDao;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private TradeService tradeService;

    /**HSY001057 主扫创建订单*/
    public String insertHsyOrder(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().create();
        /**参数转化*/
        HsyOrder hsyOrder=null;
        try{
            hsyOrder=gson.fromJson(dataParam, HsyOrder.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(hsyOrder.getShopid()!=null&&!hsyOrder.getShopid().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"店铺ID");
        if(!(hsyOrder.getAmount()!=null&&!hsyOrder.getAmount().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"交易金额");

        List<AppBizShop> shopList=hsyShopDao.findAppBizShopByID(hsyOrder.getShopid());
        AppBizShop appBizShop=shopList.get(0);
        List<AppAuUser> userList=hsyShopDao.findCorporateUserByShopID(appBizShop.getId());
        AppAuUser appAuUser=userList.get(0);

        Date date=new Date();

        hsyOrder.setOrderstatus(EnumOrderStatus.DUE_PAY.getId());
        hsyOrder.setShopname(appBizShop.getShortName());
        hsyOrder.setMerchantNo(appAuUser.getGlobalID());
        hsyOrder.setMerchantname(appBizShop.getName());
        hsyOrder.setOrdernumber("");
        hsyOrder.setSourcetype(EnumHsySourceType.SCAN.getId());
        hsyOrder.setValidationcode("");
        hsyOrder.setUid(appAuUser.getId());
        hsyOrder.setAccountid(appAuUser.getAccountID());
        hsyOrder.setDealerid(appAuUser.getDealerID());
        hsyOrderDao.insert(hsyOrder);
        DecimalFormat a=new DecimalFormat("0000000000");
        hsyOrder.setOrdernumber("SI"+ AppDateUtil.formatDate(date,AppDateUtil.TIME_FORMAT_SHORT+a.format(hsyOrder.getId())));
        hsyOrderDao.update(hsyOrder);

        HsyOrder hsyOrderReturn=new HsyOrder();
        hsyOrderReturn.setId(hsyOrder.getId());
        hsyOrderReturn.setOrdernumber(hsyOrder.getOrdernumber());
        hsyOrderReturn.setOrderstatus(hsyOrder.getOrderstatus());

        Map map=new HashMap();
        map.put("hsyOrder",hsyOrderReturn);
        return gson.toJson(map);
    }

    /**HSY001058 更新订单并支付*/
    public String updateHsyOrderPay(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().create();
        /**参数转化*/
        HsyOrder hsyOrder=null;
        try{
            hsyOrder=gson.fromJson(dataParam, HsyOrder.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(hsyOrder.getId()!=null&&!hsyOrder.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"订单ID");
        if(!(hsyOrder.getAuthCode()!=null&&!hsyOrder.getAuthCode().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"主扫付款码");

        String authCode=hsyOrder.getAuthCode();
        EnumPolicyType type=judgeType(hsyOrder.getAuthCode());
        if(type==null)
            throw new ApiHandleException(ResultCode.AUTHCODE_NOT_BELONG_BOTH);
        hsyOrder=hsyOrderDao.selectById(hsyOrder.getId());
        hsyOrder.setAuthCode(authCode);

        UserCurrentChannelPolicy userCurrentChannelPolicy=userCurrentChannelPolicyDao.selectByUserId(hsyOrder.getUid());
        if(type.getId()==EnumPolicyType.ALIPAY.getId()) {
            hsyOrder.setPaychannelsign(userCurrentChannelPolicy.getAlipayChannelTypeSign());
            hsyOrder.setPaymentChannel(EnumPaymentChannel.ALIPAY.getId());
        }else {
            hsyOrder.setPaychannelsign(userCurrentChannelPolicy.getWechatChannelTypeSign());
            hsyOrder.setPaymentChannel(EnumPaymentChannel.WECHAT_PAY.getId());
        }
        hsyOrder.setGoodsname(hsyOrder.getMerchantname());
        hsyOrder.setGoodsdescribe(EnumPolicyType.ALIPAY.getName()+"向"+hsyOrder.getMerchantname()+"支付："+hsyOrder.getAmount());

        hsyOrder.setRealAmount(hsyOrder.getAmount());
        UserChannelPolicy userChannelPolicy=userChannelPolicyDao.selectByUserIdAndChannelTypeSign(hsyOrder.getUid(),hsyOrder.getPaychannelsign());
        hsyOrder.setSettleType(userChannelPolicy.getSettleType());
        String channelCode = basicChannelService.selectCodeByChannelSign(hsyOrder.getPaychannelsign(), EnumMerchantPayType.MERCHANT_BAR);
        hsyOrder.setPaytype(channelCode);
        EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.idOf(hsyOrder.getPaychannelsign());
        hsyOrder.setPaymentChannel(enumPayChannelSign.getPaymentChannel().getId());
        hsyOrderDao.update(hsyOrder);

        PayParams payParams=new PayParams();
        payParams.setBusinessOrderNo(hsyOrder.getOrdernumber());
        payParams.setChannel(hsyOrder.getPaychannelsign());
        payParams.setMerchantPayType(EnumMerchantPayType.MERCHANT_BAR);
        payParams.setAppId(EnumAppType.HSY.getId());
        payParams.setTradeAmount(hsyOrder.getAmount());
        payParams.setRealPayAmount(hsyOrder.getRealAmount());
        payParams.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        payParams.setPayeeAccountId(hsyOrder.getAccountid());
        payParams.setGoodsName(hsyOrder.getGoodsname());
        payParams.setGoodsDescribe(hsyOrder.getGoodsdescribe());
        payParams.setWxAppId(WxConstants.APP_HSY_ID);
        payParams.setMerchantNo(hsyOrder.getMerchantNo());
        payParams.setMerchantName(hsyOrder.getMerchantname());
        payParams.setAuthCode(hsyOrder.getAuthCode());
        PayResponse payResponse=tradeService.pay(payParams);

        if(payResponse.getCode()!= EnumBasicStatus.SUCCESS.getId()) {
            hsyOrder.setOrderid(payResponse.getTradeOrderId());
            hsyOrder.setOrderno(payResponse.getTradeOrderNo());
            hsyOrder.setOrderstatus(EnumOrderStatus.PAY_FAIL.getId());
            hsyOrder.setRemark(payResponse.getMessage());
            hsyOrderDao.update(hsyOrder);
            throw new ApiHandleException(ResultCode.ORDER_TO_TRADE_FAIL,payResponse.getMessage());
        }

        hsyOrder.setOrderid(payResponse.getTradeOrderId());
        hsyOrder.setOrderno(payResponse.getTradeOrderNo());
        hsyOrder.setValidationcode(hsyOrder.getOrderno().substring(hsyOrder.getOrderno().length()-4));
        hsyOrderDao.update(hsyOrder);

        HsyOrder hsyOrderReturn=new HsyOrder();
        hsyOrderReturn.setId(hsyOrder.getId());
        hsyOrderReturn.setOrdernumber(hsyOrder.getOrdernumber());
        hsyOrderReturn.setOrderno(hsyOrder.getOrderno());
        hsyOrderReturn.setOrderstatus(hsyOrder.getOrderstatus());

        Map map=new HashMap();
        map.put("hsyOrder",hsyOrderReturn);
        return gson.toJson(map);
    }

    /**HSY001059 查找订单信息*/
    public String findHsyOrderRelateInfo(String dataParam, AppParam appParam)throws ApiHandleException{
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        HsyOrder hsyOrder=null;
        try{
            hsyOrder=gson.fromJson(dataParam, HsyOrder.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(hsyOrder.getId()!=null&&!hsyOrder.getId().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"订单ID");

        hsyOrder=hsyOrderDao.selectById(hsyOrder.getId());
        if(!(hsyOrder!=null&&hsyOrder.getOrderstatus()!=null))
            throw new ApiHandleException(ResultCode.ORDER_NOT_FOUND);

        HsyOrder hsyOrderReturn=new HsyOrder();
        hsyOrderReturn.setId(hsyOrder.getId());
        hsyOrderReturn.setOrdernumber(hsyOrder.getOrdernumber());
        hsyOrderReturn.setOrderno(hsyOrder.getOrderno());
        hsyOrderReturn.setOrderstatus(hsyOrder.getOrderstatus());
        hsyOrderReturn.setPaysuccesstime(hsyOrder.getPaysuccesstime());

        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                if(date==null)
                    return null;
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(json.getAsJsonPrimitive()==null)
                    return null;
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();

        Map map=new HashMap();
        map.put("hsyOrder",hsyOrderReturn);
        return gson.toJson(map);
    }

    public EnumPolicyType judgeType(String authPolicy){
        Pattern wechatp = Pattern.compile(wechatPstr);
        Matcher wechatm = wechatp.matcher(authPolicy);
        if(wechatm.find() )
            return EnumPolicyType.WECHAT;

        Pattern alipayp = Pattern.compile(alipayPstr);
        Matcher alipaym = alipayp.matcher(authPolicy);
        if(alipaym.find() )
            return EnumPolicyType.ALIPAY;

        return null;
    }
}
