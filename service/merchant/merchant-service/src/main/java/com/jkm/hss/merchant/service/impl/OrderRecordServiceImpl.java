package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.dealer.enums.EnumSettlementPeriodType;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.merchant.dao.OrderRecordDao;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.*;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.request.*;
import com.jkm.hss.merchant.helper.response.DfQueryResponse;
import com.jkm.hss.merchant.helper.response.DfmResponse;
import com.jkm.hss.merchant.helper.response.OrderQueryResponse;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-25 20:20
 */
@Slf4j
@Service
public class OrderRecordServiceImpl implements OrderRecordService {
    @Autowired
    private OrderRecordDao orderRecordDao;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private TradeFeeRecordService tradeFeeRecordService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private PastRecordService pastRecordService;
    @Autowired
    private JkmAccountService jkmAccountService;
    @Autowired
    private PayExceptionRecordService payExceptionRecordService;
    @Autowired
    private SendMsgService sendMsgService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ShallProfitDetailService shallProfitDetailService;

    private  Pair<String,String> payOf(int payWay,String status) {
        String result="";
        String message = "";
        if(payWay==0){//交易
            if("N".equals(status)){
                result="N";
                message="待支付";
            }
            if("H".equals(status)||"A".equals(status)||"E".equals(status)){
                result="H";
                message="支付中";
            }
            if("S".equals(status)){
                result="S";
                message="支付成功";
            }
            if("F".equals(status)){
                result="S";
                message="支付失败";
            }
        }
        if(payWay==1){//提现
            if("N".equals(status)){
                result="N";
                message="待审核";
            }
            if("H".equals(status)||"W".equals(status)||"E".equals(status)||"A".equals(status)){
                result="H";
                message="审核中";
            }
            if("S".equals(status)){
                result="S";
                message="提现成功";
            }
            if("F".equals(status)||"D".equals(status)){
                result="F";
                message="提现失败";
            }
            if("O".equals(status)){
                result="O";
                message="审核未通过";
            }
        }

        return Pair.of(result,message);
    }

    /**
     * 交易
     * N 待支付
     * H 支付中
     * S 支付成功
     * F 支付失败
     *
     * 提现
     * N 待审核
     * H 审核中
     * S 提现成功
     * F 提现失败
     * O 审核未通过
     * @param req
     * @return
     */
    @Override
    public List<OrderRecord> selectAllOrderRecordByPage(RequestOrderRecord req) {
        List<OrderRecord> list = orderRecordDao.selectAllOrderRecordByPage(req);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                Pair<String,String>  result = payOf(list.get(i).getTradeType(),list.get(i).getPayResult());
                list.get(i).setPayResult(result.getLeft());
                list.get(i).setErrorMessage(result.getRight());
            }
        }
        return list;
    }
    @Override
    public long selectAllOrderRecordCount(RequestOrderRecord req) {
        return orderRecordDao.selectAllOrderRecordCount(req);
    }

    @Override
    public List<OrderRecord> selectBalanceByPage(RequestOrderRecord req) {
        return orderRecordDao.selectBalanceByPage(req);
    }

    @Override
    public long selectBalanceCount(long merchantId) {
        return orderRecordDao.selectBalanceCount(merchantId);
    }

    @Override
    public Optional<OrderRecord> selectOrderId(String orderId,int tradeType) {
        return Optional.fromNullable(orderRecordDao.selectOrderId(orderId,tradeType));
    }


    @Override
    public JSONObject dealerOtherPay(DfmRequest req) {
        BigDecimal serviceFeeTemp = new BigDecimal("2");//服务费（金开门收商户的钱）
        BigDecimal channelFeeTemp = new BigDecimal("0.5");//通道费（支付平台收金开门的钱）
        JSONObject resultJo = new JSONObject();
        if(req.getAccountId()==0){
            resultJo.put("code",-1);
            resultJo.put("message","代理商账户信息为空");
            return resultJo;
        }
        AccountInfo accountInfo = accountInfoService.selectByPrimaryKey(req.getAccountId());
        int compareResult = accountInfo.getAvailable().compareTo(serviceFeeTemp);
        if(compareResult!=1){
            resultJo.put("code",-1);
            resultJo.put("message","账户可提现金额小于两元");
            return resultJo;
        }
        OrderRecord orderRecord = new OrderRecord();
        String orderId = SnGenerator.generate();
        orderRecord.setOrderId(orderId);
        orderRecord.setMerchantId(req.getDealerId());
        orderRecord.setMerchantType(EnumMerchantType.AGENT.getId());
        orderRecord.setProductName("提现");
        orderRecord.setSubName(req.getAccountName());
        orderRecord.setPayChannel(EnumPayChannelSign.YG_YINLIAN.getId());
        orderRecord.setTotalFee(accountInfo.getAvailable());
        orderRecord.setRealFee(accountInfo.getAvailable().subtract(serviceFeeTemp));
        orderRecord.setServiceFee(serviceFeeTemp);
        orderRecord.setChannelFee(channelFeeTemp);
        orderRecord.setPayResult(EnumPayResult.UNPAY.getId());
        orderRecord.setSettlePeriod(EnumSettlePeriodType.D0.getId());
        orderRecord.setSettleStatus(EnumSettleStatus.SETTLE.getId());
        orderRecord.setPayParams(JSONObject.fromObject(req).toString());
        orderRecord.setBody("提现");
        orderRecord.setTradeType(EnumTradeType.DEPOSITOR.getId());
        orderRecordDao.insertSelective(orderRecord);

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("amount", orderRecord.getRealFee().toString());
        paramsMap.put("orderId", orderId);
        paramsMap.put("bitchNo", orderId);
        paramsMap.put("phoneNo", req.getPhoneNo());
        paramsMap.put("bankName", req.getBankName());
        paramsMap.put("accountName", req.getAccountName());
        paramsMap.put("accountNumber", req.getAccountNumber());
        paramsMap.put("note", "提现");
        paramsMap.put("appId","wap_hss");
        paramsMap.put("idCard", req.getIdCard());
        paramsMap.put("isCompay", EnumIsCompay.ToPrivate.getId());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().domain()+MerchantConsts.getMerchantConfig().otherPay(),paramsMap);

        if(result!=null&&!"".equals(result)){
            JSONObject jo = JSONObject.fromObject(result);
            if(jo.getInt("code")==1){
                //冻结资金
                accountInfoService.frozenMoney(accountInfo.getAvailable(),accountInfo.getId());
                updateParam(result,jo.getJSONObject("data").getString("outTradeNo"),orderRecord.getId());//更新返回结果参数
                resultJo.put("code",1);
                resultJo.put("message",jo.getString("message"));
                resultJo.put("data",jo.getJSONObject("data"));
            }else{
                resultJo.put("code",-1);
                resultJo.put("message",jo.getString("message"));
            }
        }else{
            resultJo.put("code",-1);
            resultJo.put("message","请求异常");
        }
        return resultJo;
    }



    /**
     * 商户提现
     * @param merchantInfo
     * @return
     */
    @Override
    public JSONObject otherPay(MerchantInfo merchantInfo) {
//        Pair<BigDecimal, BigDecimal> pair = shallProfitDetailService.withdrawParams(merchantInfo.getId());
//        BigDecimal serviceFeeTemp = pair.getLeft();//服务费（金开门收商户的钱）
//        BigDecimal channelFeeTemp = pair.getRight();//通道费（支付平台收金开门的钱）
        JSONObject resultJo = new JSONObject();
//        if(merchantInfo.getAccountId()==0){
//            resultJo.put("code",-1);
//            resultJo.put("message","商家账户信息为空");
//            return resultJo;
//        }
//        AccountInfo accountInfo = accountInfoService.selectByPrimaryKey(merchantInfo.getAccountId());
//        if(accountInfo==null){
//            resultJo.put("code",-1);
//            resultJo.put("message","没有此账户");
//            return resultJo;
//        }
//        int compareResult = accountInfo.getAvailable().compareTo(serviceFeeTemp);
//        if(compareResult!=1){
//            resultJo.put("code",-1);
//            resultJo.put("message","提现金额至少两元");
//            return resultJo;
//        }
//        log.info("开始冻结账户资金。。。");
//        BigDecimal availableAccount = accountInfo.getAvailable();
//        accountInfoService.frozenMoney(availableAccount,accountInfo.getId());
//        log.info("结束冻结账户资金。。。");
//        log.info("开始下单。。。");
//        log.info("开始创建订单。。。");
//        OrderRecord orderRecord = new OrderRecord();
//        String orderId = SnGenerator.generate();
//        orderRecord.setOrderId(orderId);
//        orderRecord.setMerchantId(merchantInfo.getId());
//        orderRecord.setMerchantType(EnumMerchantType.MERCHANT.getId());
//        orderRecord.setProductName("提现");
//        orderRecord.setSubName(merchantInfo.getMerchantName());
//        orderRecord.setPayChannel(EnumPayChannelSign.YG_YINLIAN.getId());
//        orderRecord.setTotalFee(availableAccount);
//        orderRecord.setRealFee(availableAccount.subtract(serviceFeeTemp));
//        orderRecord.setServiceFee(serviceFeeTemp);
//        orderRecord.setChannelFee(channelFeeTemp);
//        orderRecord.setPayResult(EnumPayResult.UNPAY.getId());
//        orderRecord.setSettlePeriod(EnumSettlePeriodType.D0.getId());
//        orderRecord.setSettleStatus(EnumSettleStatus.SETTLE.getId());
//        orderRecord.setPayParams("");
//        orderRecord.setStatus(EnumCommonStatus.NORMAL.getId());
//        orderRecord.setBody("提现");
//        orderRecord.setTradeType(EnumTradeType.DEPOSITOR.getId());
//        try{
//            orderRecordDao.insertSelective(orderRecord);
//            log.info("结束创建订单。。。");
//            resultJo.put("code",1);
//            resultJo.put("message","订单提交成功");
//        }catch(Exception e){
//            accountInfoService.backMoney(availableAccount,accountInfo.getId());
//            log.error("创建订单异常{}",e);
//            log.error("创建订单失败。。。");
//            resultJo.put("code",-1);
//            resultJo.put("message","提现请求失败");
//        }
//        log.info("下单结束。。。");
//        log.info("开始组装数据。。。");
//        Map<String, String> paramsMap = new HashMap<String, String>();
//        paramsMap.put("amount", orderRecord.getRealFee().toString());
//        paramsMap.put("orderId", orderId);
//        paramsMap.put("bitchNo", orderId);
//        paramsMap.put("phoneNo", MerchantSupport.decryptMobile(merchantInfo.getReserveMobile()));
//        paramsMap.put("bankName", merchantInfo.getBankName());
//        paramsMap.put("accountName", merchantInfo.getName());
//        paramsMap.put("accountNumber", MerchantSupport.decryptBankCard(merchantInfo.getBankNo()));
//        paramsMap.put("note", "提现");
//        paramsMap.put("appId","wap_hsy");
//        paramsMap.put("idCard", MerchantSupport.decryptIdentity(merchantInfo.getIdentity()));
//        paramsMap.put("isCompay", EnumIsCompay.ToPrivate.getId());
//        log.info("组装数据结束。。。,数据为{}",JSONObject.fromObject(paramsMap).toString());
//        log.info("开始向支付中心发请求。。。");
//        log.info("更该订单状态为【处理中】。。。");
//        orderRecord.setPayResult(EnumPayResult.HAND.getId());
//        orderRecordDao.updateByPrimaryKeySelective(orderRecord);
//        log.info("更该订单状态为【处理中】结束。。。");
//        String result = SmPost.post(MerchantConsts.getMerchantConfig().domain()+MerchantConsts.getMerchantConfig().otherPay(),paramsMap);
//        log.info("向支付中心发请求结束。。。，结果是{}",result);
//        if(result!=null&&!"".equals(result)){
//            log.info("支付中心返回代付结果是{}",result);
//            JSONObject jo = JSONObject.fromObject(result);
//            DfmResponse dfmResponse = (DfmResponse)JSONObject.toBean(jo, DfmResponse.class);
//            if("success".equals(dfmResponse.getError_code())){
//                log.info("代付success，订单号是{}",orderRecord.getOrderId());
//                orderRecord.setPayResult(EnumPayResult.ACCEPT.getId());
//                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
//                orderRecord.setErrorMessage(dfmResponse.getMessage());
//                orderRecord.setPayTime(new Date());
//                orderRecord.setResultParams(result);
//                try{
//                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
//                    log.info("代付success,更改订单状态成功，订单号是{}",orderRecord.getOrderId());
//                }catch (Throwable e){
//                    log.error("代付success,更改订单状态异常，订单号是{}",orderRecord.getOrderId());
//                    log.error("代付success,更改订单状态异常{}",e.getStackTrace());
//                }
//                try{
//                    log.info("商户号{}",orderRecord.getMerchantId());
//                    Optional<MerchantInfo> mr = merchantInfoService.selectById(orderRecord.getMerchantId());
//                    if(mr.isPresent()){
//                        Optional<UserInfo> ui = userInfoService.selectByMerchantId(mr.get().getId());
//                        if(ui.isPresent()){
//                            log.info("用户号",ui.get().getId());
//                            log.info("openId",ui.get().getOpenId());
//                            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//                            String total = decimalFormat.format(orderRecord.getTotalFee());
//                            sendMsgService.sendPushMessage(total,mr.get().getBankName(),mr.get().getBankNoShort(),ui.get().getOpenId());
//                        }
//                    }
//                }catch (Exception e){
//                    log.error("公众号通知提现成功消息发送失败");
//                }
//                resultJo.put("code",1);
//                resultJo.put("message","提现成功");
//            }else if("fail".equals(dfmResponse.getError_code())){
//                log.info("代付fail，订单号是{}",orderRecord.getOrderId());
//                try{
//                    log.info("代付fail，开始解冻资金。。。");
//                    accountInfoService.backMoney(availableAccount,accountInfo.getId());
//                    log.info("代付fail，解冻资金结束。。。");
//                }catch (Throwable e){
//                    log.error("代付fail,解冻异常{}",e);
//                    log.error("代付fail,解冻异常,订单号是{}",orderRecord.getOrderId());
//                }
//                log.info("代付fail，更改订单信息，订单号是{}",orderRecord.getOrderId());
//                orderRecord.setPayResult(EnumPayResult.FAIL.getId());
//                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
//                orderRecord.setErrorMessage(dfmResponse.getMessage());
//                orderRecord.setPayTime(new Date());
//                orderRecord.setResultParams(result);
//                try{
//                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
//                    log.info("代付fail，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
//                }catch (Throwable e){
//                    log.error("代付fail，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
//                    log.error("代付fail，更该订单信息异常{}",e.getStackTrace());
//                }
//                resultJo.put("code",-1);
//                resultJo.put("message",dfmResponse.getMessage());
//            }else if("invalid".equals(dfmResponse.getError_code())){//签名失败
//                log.info("代付invalid，订单号是{}",orderRecord.getOrderId());
//                try{
//                    log.info("代付invalid，开始解冻资金。。。");
//                    accountInfoService.backMoney(availableAccount,accountInfo.getId());
//                    log.info("代付invalid，解冻资金结束。。。");
//                }catch (Throwable e){
//                    log.error("代付invalid,解冻异常{}",e);
//                    log.error("代付invalid,解冻异常,订单号是{}",orderRecord.getOrderId());
//                }
//                log.info("代付invalid，更改订单信息，订单号是{}",orderRecord.getOrderId());
//                orderRecord.setPayResult(EnumPayResult.FAIL.getId());
//                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
//                orderRecord.setErrorMessage(dfmResponse.getMessage());
//                orderRecord.setPayTime(new Date());
//                orderRecord.setResultParams(result);
//                try{
//                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
//                    log.info("代付invalid，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
//                }catch (Throwable e){
//                    log.error("代付invalid，更该订单信息异常，订单号是{}",orderRecord.getOrderId());
//                    log.error("代付invalid，更该订单信息异常{}",e.getStackTrace());
//                }
//                resultJo.put("code",-1);
//                resultJo.put("message","签名失败");
//            }else if("exception".equals(dfmResponse.getError_code())){
//                log.info("代付exception，更改订单信息，订单号是{}",orderRecord.getOrderId());
//                orderRecord.setPayResult(EnumPayResult.EXCEPTION.getId());
//                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
//                orderRecord.setErrorMessage(dfmResponse.getMessage());
//                orderRecord.setPayTime(new Date());
//                orderRecord.setResultParams(result);
//                //结算
//                try{
//                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
//                    log.info("代付exception，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
//                }catch (Throwable e){
//                    log.error("代付exception，更该订单信息异常，订单号是{}",orderRecord.getOrderId());
//                    log.error("代付exception，更该订单信息异常{}",e.getStackTrace());
//                }
//                try{
//                    log.info("商户号{}",orderRecord.getMerchantId());
//                    Optional<MerchantInfo> mr = merchantInfoService.selectById(orderRecord.getMerchantId());
//                    if(mr.isPresent()){
//                        Optional<UserInfo> ui = userInfoService.selectByMerchantId(mr.get().getId());
//                        if(ui.isPresent()){
//                            log.info("用户号",ui.get().getId());
//                            log.info("openId",ui.get().getOpenId());
//                            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//                            String total = decimalFormat.format(orderRecord.getTotalFee());
//                            sendMsgService.sendPushMessage(total,mr.get().getBankName(),mr.get().getBankNoShort(),ui.get().getOpenId());
//                        }
//                    }
//                }catch (Exception e){
//                    log.error("公众号通知提现成功消息发送失败");
//                }
//                resultJo.put("code",1);
//                resultJo.put("message","代付异常");
//            }else if("wait".equals(dfmResponse.getError_code())){
//                log.info("代付wait，更改订单信息，订单号是{}",orderRecord.getOrderId());
//                orderRecord.setPayResult(EnumPayResult.WAIT.getId());
//                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
//                orderRecord.setErrorMessage(dfmResponse.getMessage());
//                orderRecord.setPayTime(new Date());
//                orderRecord.setResultParams(result);
//                try{
//                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
//                    log.info("代付wait，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
//                }catch (Throwable e){
//                    log.error("代付wait，更该订单信息异常，订单号是{}",orderRecord.getOrderId());
//                    log.error("代付wait，更该订单信息异常{}",e.getStackTrace());
//                }
//                try{
//                    log.info("商户号{}",orderRecord.getMerchantId());
//                    Optional<MerchantInfo> mr = merchantInfoService.selectById(orderRecord.getMerchantId());
//                    if(mr.isPresent()){
//                        Optional<UserInfo> ui = userInfoService.selectByMerchantId(mr.get().getId());
//                        if(ui.isPresent()){
//                            log.info("用户号",ui.get().getId());
//                            log.info("openId",ui.get().getOpenId());
//                            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//                            String total = decimalFormat.format(orderRecord.getTotalFee());
//                            sendMsgService.sendPushMessage(total,mr.get().getBankName(),mr.get().getBankNoShort(),ui.get().getOpenId());
//                        }
//                    }
//                }catch (Exception e){
//                    log.error("公众号通知提现成功消息发送失败");
//                }
//                resultJo.put("code",1);
//                resultJo.put("message","等待系统确认");
//            }
//        }else{
//            log.error("提现异常,订单号是{}",orderRecord.getId());
//            orderRecord.setPayResult(EnumPayResult.EXCEPTION.getId());
//            orderRecordDao.updateByPrimaryKeySelective(orderRecord);
//            resultJo.put("code",-1);
//            resultJo.put("message","请求异常");
//        }
        return resultJo;
    }

    /**
     * 提现审核
     * @param req
     * @return
     */
    @Override
    public JSONObject checkWithdraw(WithDrawRequest req) {
        JSONObject resultJo = new JSONObject();
        OrderRecord orderRecord = orderRecordDao.selectByPrimaryKey(req.getId());
        if(orderRecord==null){
            resultJo.put("code",-1);
            resultJo.put("message","没有查到此订单");
            return resultJo;
        }
        if(orderRecord.getPayResult()==EnumPayResult.SUCCESS.getId()){
            resultJo.put("code",-1);
            resultJo.put("message","已提现成功，不能多次提现");
            return resultJo;
        }
        log.info("开始组装数据。。。");
        Optional<MerchantInfo>  merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
        if(!merchantInfoOptional.isPresent()){
            resultJo.put("code",-1);
            resultJo.put("message","没有此商户");
            return resultJo;
        }
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("amount", orderRecord.getRealFee().toString());
        paramsMap.put("orderId", orderRecord.getOrderId());
        paramsMap.put("bitchNo", orderRecord.getOrderId());
        paramsMap.put("phoneNo", MerchantSupport.decryptMobile(merchantInfoOptional.get().getReserveMobile()));
        paramsMap.put("bankName", merchantInfoOptional.get().getBankName());
        paramsMap.put("accountName", merchantInfoOptional.get().getName());
        paramsMap.put("accountNumber", MerchantSupport.decryptBankCard(merchantInfoOptional.get().getBankNo()));
        paramsMap.put("note", "提现");
        paramsMap.put("appId","wap_hss");
        paramsMap.put("notifyUrl", WxConstants.DOMAIN+"/call/otherPayBack");     //后台通知url
        paramsMap.put("idCard", MerchantSupport.decryptIdentity(merchantInfoOptional.get().getIdentity()));
        paramsMap.put("isCompay", EnumIsCompay.ToPrivate.getId());
        log.info("组装数据结束。。。,数据为{}",JSONObject.fromObject(paramsMap).toString());
        log.info("开始向支付中心发请求。。。");
        log.info("更该订单状态为【处理中】。。。");
        orderRecord.setPayResult(EnumPayResult.HAND.getId());
        orderRecordDao.updateByPrimaryKeySelective(orderRecord);
        log.info("更该订单状态为【处理中】结束。。。");
        String result = SmPost.post(MerchantConsts.getMerchantConfig().domain()+MerchantConsts.getMerchantConfig().otherPay(),paramsMap);
        log.info("向支付中心发请求结束。。。，结果是{}",result);
        if(result!=null&&!"".equals(result)){
            log.info("支付中心返回代付结果是{}",result);
            JSONObject jo = JSONObject.fromObject(result);
            DfmResponse dfmResponse = (DfmResponse)JSONObject.toBean(jo, DfmResponse.class);
            if("success".equals(dfmResponse.getError_code())){
                log.info("代付success，订单号是{}",orderRecord.getOrderId());
                orderRecord.setPayResult(EnumPayResult.ACCEPT.getId());
                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
                orderRecord.setErrorMessage(dfmResponse.getMessage());
                orderRecord.setPayTime(new Date());
                orderRecord.setResultParams(result);
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("代付success,更改订单状态成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("代付success,更改订单状态异常，订单号是{}",orderRecord.getOrderId());
                    log.error("代付success,更改订单状态异常{}",e.getStackTrace());
                }
                resultJo.put("code",1);
                resultJo.put("message","退款请求已受理");
            }else if("fail".equals(dfmResponse.getError_code())){
                log.info("代付fail，更改订单信息，订单号是{}",orderRecord.getOrderId());
                orderRecord.setPayResult(EnumPayResult.FAIL.getId());
                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
                orderRecord.setErrorMessage(dfmResponse.getMessage());
                orderRecord.setPayTime(new Date());
                orderRecord.setResultParams(result);
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("代付fail，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("代付fail，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
                    log.error("代付fail，更该订单信息异常{}",e.getStackTrace());
                }
                resultJo.put("code",-1);
                resultJo.put("message",dfmResponse.getMessage());
            }else if("invalid".equals(dfmResponse.getError_code())){//签名失败
                log.info("代付invalid，更改订单信息，订单号是{}",orderRecord.getOrderId());
                orderRecord.setPayResult(EnumPayResult.FAIL.getId());
                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
                orderRecord.setErrorMessage(dfmResponse.getMessage());
                orderRecord.setPayTime(new Date());
                orderRecord.setResultParams(result);
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("代付invalid，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("代付invalid，更该订单信息异常，订单号是{}",orderRecord.getOrderId());
                    log.error("代付invalid，更该订单信息异常{}",e.getStackTrace());
                }
                resultJo.put("code",-1);
                resultJo.put("message","签名失败");
            }else if("exception".equals(dfmResponse.getError_code())){
                log.info("代付exception，更改订单信息，订单号是{}",orderRecord.getOrderId());
                orderRecord.setPayResult(EnumPayResult.FAIL.getId());
                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
                orderRecord.setErrorMessage(dfmResponse.getMessage());
                orderRecord.setPayTime(new Date());
                orderRecord.setResultParams(result);
                //结算
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("代付exception，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("代付exception，更该订单信息异常，订单号是{}",orderRecord.getOrderId());
                    log.error("代付exception，更该订单信息异常{}",e.getStackTrace());
                }
                resultJo.put("code",1);
                resultJo.put("message","返回结果，代付异常");
            }else if("wait".equals(dfmResponse.getError_code())){
                log.info("代付wait，更改订单信息，订单号是{}",orderRecord.getOrderId());
                orderRecord.setPayResult(EnumPayResult.WAIT.getId());
                orderRecord.setOutTradeNo(dfmResponse.getSys_order_id());
                orderRecord.setErrorMessage(dfmResponse.getMessage());
                orderRecord.setPayTime(new Date());
                orderRecord.setResultParams(result);
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("代付wait，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("代付wait，更该订单信息异常，订单号是{}",orderRecord.getOrderId());
                    log.error("代付wait，更该订单信息异常{}",e.getStackTrace());
                }
                resultJo.put("code",1);
                resultJo.put("message","等待系统确认");
            }
        }else{
            log.error("提现异常,订单号是{}",orderRecord.getId());
            orderRecord.setPayResult(EnumPayResult.EXCEPTION.getId());
            orderRecord.setErrorMessage("请求异常");
            orderRecordDao.updateByPrimaryKeySelective(orderRecord);
            resultJo.put("code",-1);
            resultJo.put("message","请求异常");
        }
        return resultJo;
    }

    /**
     * 商户交易
     * @param req
     * @return
     */
    @Override
    public JSONObject PayOrder(TradeRequest req) {
        log.info("交易参数"+JSONObject.fromObject(req).toString());
        req.setBody("收款");
        req.setNotifyUrl("http://"+WxConstants.DOMAIN+"/wx/payResult");
        JSONObject resultJo = new JSONObject();
        log.info("开始下单。。。");
        log.info("开始创建订单。。。");
        OrderRecord orderRecord = new OrderRecord();
        String orderId = SnGenerator.generate();
        orderRecord.setStatus(EnumCommonStatus.NORMAL.getId());
        orderRecord.setMerchantId(req.getMerchantId());
        orderRecord.setMerchantType(EnumMerchantType.MERCHANT.getId());
        orderRecord.setProductName(req.getBody());
        orderRecord.setSubName(req.getSubMerName());
        orderRecord.setPayChannel(req.getPayChannel());
        orderRecord.setTotalFee(new BigDecimal(req.getTotalFee()));
        orderRecord.setPayResult(EnumPayResult.UNPAY.getId());
        orderRecord.setSettlePeriod(EnumSettlePeriodType.D0.getId());
        orderRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
        orderRecord.setPayParams(JSONObject.fromObject(req).toString());
        orderRecord.setBody(req.getBody());
        orderRecord.setTradeType(EnumTradeType.DEAL.getId());
        orderRecord.setOrderId(orderId);
        req.setReturnUrl("http://"+WxConstants.DOMAIN+"/sqb/success/"+new BigDecimal(req.getTotalFee())+"/"+orderId);
        try{
            orderRecordDao.insertSelective(orderRecord);
            log.info("结束创建订单。。。");
        }catch(Exception e){
            log.error("创建订单异常{}",e);
            log.error("创建订单失败。。。");
        }
        log.info("订单参数"+JSONObject.fromObject(orderRecord).toString());
        log.info("下单结束。。。");
        log.info("开始组装数据。。。");
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("body", orderRecord.getBody());     //商品描述
        paramsMap.put("notifyUrl", req.getNotifyUrl());     //后台通知url
        paramsMap.put("payNum", orderId);       //支付流水号
        paramsMap.put("returnUrl", req.getReturnUrl());     //前台回调URL
        paramsMap.put("subMerName", req.getSubMerName());       //下游商户名称
        paramsMap.put("subMerNo", req.getSubMerNo());       //下游商户号
        paramsMap.put("appId","wap_hss");
        paramsMap.put("totalFee", orderRecord.getTotalFee().toString());         //总金额
        if(req.getPayChannel()==EnumPayChannelSign.YG_WEIXIN.getId()||req.getPayChannel()==EnumPayChannelSign.YG_ZHIFUBAO.getId()){
            paramsMap.put("tradeType", "JSAPI");       //交易类型   JSAPI，NATIVE，APP，WAP,EPOS
        }
        if(req.getPayChannel()==EnumPayChannelSign.YG_YINLIAN.getId()){
            paramsMap.put("tradeType", "EPOS");       //交易类型   JSAPI，NATIVE，APP，WAP,EPOS
        }
        log.info("组装数据结束。。。,数据为{}"+JSONObject.fromObject(paramsMap).toString());
        log.info("开始向支付中心发支付请求。。。");
        log.info("更该订单状态为【处理中】。。。");
        orderRecord.setPayResult(EnumPayResult.HAND.getId());
        orderRecordDao.updateByPrimaryKeySelective(orderRecord);
        log.info("更该订单状态为【处理中】结束。。。");

        try{
            String result = SmPost.post(MerchantConsts.getMerchantConfig().domain()+MerchantConsts.getMerchantConfig().trade(),paramsMap);
            log.info("支付交易结果{}",JSONObject.fromObject(paramsMap).toString());
            //更新参数
            if(result!=null&&!"".equals(result)){
                JSONObject jo = JSONObject.fromObject(result);
                if(jo.getInt("code")==1){
                    log.info("支付成功，订单号是{}",orderRecord.getOrderId());
                    orderRecord.setPayResult(EnumPayResult.ACCEPT.getId());
                    orderRecord.setOutTradeNo(jo.getJSONObject("data").getString("outTradeNo"));
                    orderRecord.setErrorMessage(jo.getString("message"));
                    orderRecord.setPayTime(new Date());
                    orderRecord.setResultParams(result);
                    try{
                        orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                        log.info("支付成功,更改订单状态成功，订单号是{}",orderRecord.getOrderId());
                    }catch (Throwable e){
                        log.error("支付成功,更改订单状态异常，订单号是{}",orderRecord.getOrderId());
                        log.error("支付成功,更改订单状态异常{}",e.getStackTrace());
                    }
                    resultJo.put("code",1);
                    resultJo.put("message",jo.getString("message"));
                    resultJo.put("data",jo.getJSONObject("data"));
                }else{
                    orderRecord.setPayResult(EnumPayResult.FAIL.getId());
                    orderRecord.setOutTradeNo("");
                    orderRecord.setErrorMessage(jo.getString("message"));
                    orderRecord.setPayTime(new Date());
                    orderRecord.setResultParams(result);
                    try{
                        orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                        log.info("支付失败，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                    }catch (Throwable e){
                        log.error("支付失败，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
                        log.error("支付失败，更该订单信息异常{}",e.getStackTrace());
                    }
                    resultJo.put("code",-1);
                    resultJo.put("message",jo.getString("message"));
                }
            }else{
                orderRecord.setPayResult(EnumPayResult.EXCEPTION.getId());
                orderRecord.setErrorMessage("请求异常");
                orderRecord.setPayTime(new Date());
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("支付成功,更改订单状态成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("支付成功,更改订单状态异常，订单号是{}",orderRecord.getOrderId());
                    log.error("支付成功,更改订单状态异常{}",e.getStackTrace());
                }
                resultJo.put("code",1);
                resultJo.put("message","交易处理中");
            }
        }catch (Exception e){
            orderRecord.setPayResult(EnumPayResult.EXCEPTION.getId());
            orderRecord.setErrorMessage("系统异常");
            orderRecord.setPayTime(new Date());
            try{
                orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                log.info("支付系统异常,更改订单状态成功，订单号是{}",orderRecord.getOrderId());
            }catch (Throwable e1){
                log.error("支付系统异常,更改订单状态异常，订单号是{}",orderRecord.getOrderId());
                log.error("支付系统异常,更改订单状态异常{}",e1.getStackTrace());
            }
            resultJo.put("code",-1);
            resultJo.put("message","请求异常");
        }
        return resultJo;
    }

    /**
     * 交易回调
     * @param payResult
     */
    @Transactional
    @Override
    public void payResult(Map payResult) {
        log.info("回调开始，进入回调接口");
        if(!"1".equals(payResult.get("tradeResult").toString())){
            Optional<OrderRecord> orderRecord = this.selectOrderId(payResult.get("orderId").toString(),EnumTradeType.DEAL.getId());
            if(orderRecord.isPresent()){
                orderRecord.get().setErrorMessage(payResult.get("returnMsg").toString());
                orderRecord.get().setPayResult(EnumPayResult.FAIL.getId());
                orderRecordDao.updateByPrimaryKeySelective(orderRecord.get());
            }else{
                log.error("支付回调异常，查不到订单号为{}的订单",payResult.get("orderId").toString());
            }
        }else{
            Optional<OrderRecord> orderRecord = this.selectOrderId(payResult.get("orderId").toString(),EnumTradeType.DEAL.getId());
            if(!orderRecord.isPresent()){
                log.error("查找不到订单号为{}的单子",payResult.get("orderId").toString());
            }else{
                try{
                    log.info("支付完成，商户还是入账");
                    /**
                     * ①商户入账
                     * realFee 实得金额
                     * serviceFee 金开门收商户的钱
                     * channelFee 通道成本
                     */
                    Map<String, Pair<Long,BigDecimal>> merchantMap = dealerService.merchantAmount(orderRecord.get());
                    //商户实际所得钱
                    BigDecimal realFee = merchantMap.get("realFee").getRight();
                    //通道的费（支付平台收金开门的钱）
                    BigDecimal channelFee = merchantMap.get("channelFee").getRight();
                    //服务费（金开门收商户的钱）
                    BigDecimal serviceFee = orderRecord.get().getTotalFee().subtract(realFee);
                    orderRecord.get().setRealFee(realFee);
                    orderRecord.get().setServiceFee(serviceFee);
                    orderRecord.get().setChannelFee(channelFee);
                    orderRecord.get().setSettleStatus(EnumSettleStatus.SETTLE.getId());
                    orderRecord.get().setPayResult(EnumPayResult.SUCCESS.getId());
                    orderRecord.get().setOutTradeNo(payResult.get("outTradeNo").toString());
                    orderRecord.get().setErrorMessage(payResult.get("returnMsg")==null?"":payResult.get("returnMsg").toString());
                    orderRecord.get().setPayTime(new Date());
                    try{
                        orderRecordDao.updateByPrimaryKeySelective(orderRecord.get());
                    }catch (Throwable e){
                        log.error("更改订单{}失败",payResult.get("orderId").toString());
                        log.error("更改订单失败{}",e);
                        log.error("更改订单失败"+e.getMessage());
                    }

                    if(merchantMap.get("realFee").getLeft()!=null){
                        try{
                            accountInfoService.addAvailableMoney(merchantMap.get("realFee").getLeft(),orderRecord.get().getRealFee());
                        }catch (Throwable e){
                            log.error("订单号为{}，【入账】失败",payResult.get("orderId").toString());
                            log.error("【入账】失败{}",e);
                            log.error("【入账】失败"+e.getMessage());
                            payExceptionRecordService.insertByCondition(payResult.get("orderId").toString(),payResult.get("outTradeNo").toString(), EnumPayResult.EXCEPTION.getId());
                        }
                    }else{
                        log.error("订单为{}的商户收益为空，调用收益接口错误",payResult.get("orderId").toString());
                    }

                }catch(Exception e){
                    log.error("订单号为{}，商户计算入账失败",payResult.get("orderId").toString());
                }

                try{
                    log.info("支付完成，开始计算收益");
                    /**
                     * ①计算收益
                     *
                     * dealerService.shallProfit(orderRecord.get());
                     */
                    Map<String, Triple<Long, BigDecimal, String>> map = null;
                    log.info("分佣返回参数为{}",JSONObject.fromObject(map).toString());

                    /**
                     * ②总入账
                     */
                    try{
                        log.info("支付完成，开始总入账");
                        jkmAccountService.income(orderRecord.get().getTotalFee(),map.get("merchantAndProfit").getMiddle(),1);
                    }catch(Throwable e){
                        log.error("订单为{}的【总入账】错误",orderRecord.get().getOrderId());
                        log.error("【总入账】异常{}",e);
                        log.error("【总入账】异常"+e.getMessage());
                    }
                    /**
                     * ③分佣
                     */
                    //手续费容器
                    try{
                        log.info("支付完成，开始分佣");
                        BigDecimal profit = (orderRecord.get().getServiceFee()).subtract(orderRecord.get().getChannelFee());
                        TradeFeeRecord tradeFeeRecord = new TradeFeeRecord();
                        tradeFeeRecord.setMerchantId(orderRecord.get().getMerchantId());
                        tradeFeeRecord.setOrderId(orderRecord.get().getId());
                        tradeFeeRecord.setTradeType(EnumTradeType.DEAL.getId());
                        tradeFeeRecord.setTotalFee(profit);
                        tradeFeeRecord.setStatus(0);
                        tradeFeeRecordService.insertSelective(tradeFeeRecord);
                        //4.给一级代理商账号加钱，并记录
                        if(map.get("firstMoney")!=null){
                            accountInfoService.addUnsettledMoney(map.get("firstMoney").getLeft(),map.get("firstMoney").getMiddle());
                            PastRecord pastRecord = new PastRecord();
                            pastRecord.setPastNo(SnGenerator.generate());
                            pastRecord.setAccounId(map.get("firstMoney").getLeft());
                            pastRecord.setTradeId(tradeFeeRecord.getId());
                            pastRecord.setOrderId(orderRecord.get().getId());
                            pastRecord.setTotalFee(map.get("firstMoney").getMiddle());
                            pastRecord.setSettlePeriod(map.get("firstMoney").getRight());
                            pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                            pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                            pastRecordService.insertSelective(pastRecord);
                        }
                        //5.给二级代理商账号加钱，并记录
                        if(map.get("secondMoney")!=null){
                            accountInfoService.addUnsettledMoney(map.get("secondMoney").getLeft(),map.get("secondMoney").getMiddle());
                            PastRecord pastRecord = new PastRecord();
                            pastRecord.setPastNo(SnGenerator.generate());
                            pastRecord.setAccounId(map.get("secondMoney").getLeft());
                            pastRecord.setTradeId(tradeFeeRecord.getId());
                            pastRecord.setOrderId(orderRecord.get().getId());
                            pastRecord.setTotalFee(map.get("secondMoney").getMiddle());
                            pastRecord.setSettlePeriod(map.get("secondMoney").getRight());
                            pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                            pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                            pastRecordService.insertSelective(pastRecord);
                        }
                        //6.给通道账号加钱，并记录
                        if(map.get("channelMoney")!=null){
                            accountInfoService.addUnsettledMoney(map.get("channelMoney").getLeft(),map.get("channelMoney").getMiddle());
                            PastRecord pastRecord = new PastRecord();
                            pastRecord.setPastNo(SnGenerator.generate());
                            pastRecord.setAccounId(map.get("channelMoney").getLeft());
                            pastRecord.setTradeId(tradeFeeRecord.getId());
                            pastRecord.setOrderId(orderRecord.get().getId());
                            pastRecord.setTotalFee(map.get("channelMoney").getMiddle());
                            pastRecord.setSettlePeriod(map.get("channelMoney").getRight());
                            pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                            pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                            pastRecordService.insertSelective(pastRecord);
                        }
                        //7.给产品账号加钱，并记录
                        if(map.get("productMoney")!=null){
                            accountInfoService.addUnsettledMoney(map.get("productMoney").getLeft(),map.get("productMoney").getMiddle());
                            PastRecord pastRecord = new PastRecord();
                            pastRecord.setPastNo(SnGenerator.generate());
                            pastRecord.setAccounId(map.get("productMoney").getLeft());
                            pastRecord.setTradeId(tradeFeeRecord.getId());
                            pastRecord.setOrderId(orderRecord.get().getId());
                            pastRecord.setTotalFee(map.get("productMoney").getMiddle());
                            pastRecord.setSettlePeriod(map.get("productMoney").getRight());
                            pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                            pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                            pastRecordService.insertSelective(pastRecord);
                        }
                    }catch (Throwable e){
                        log.error("订单号为{}，【分佣】失败",payResult.get("orderId").toString());
                        log.error("【分佣】失败{}",e);
                        log.error("【分佣】失败"+e.getMessage());
                        payExceptionRecordService.insertByCondition(payResult.get("orderId").toString(),payResult.get("outTradeNo").toString(), EnumPayResult.EXCEPTION.getId());
                    }

                }catch (Throwable e){
                    log.error("订单为{}的【收益计算】错误",orderRecord.get().getOrderId());
                    log.error("【收益计算】异常{}",e);
                    log.error("【收益计算】异常"+e.getMessage());
                }
                /**
                 * ④通知商户
                 */
                try{
                    log.info("支付完成，开始通知商户");
                    log.info("商户号{}",orderRecord.get().getMerchantId());
                    Optional<MerchantInfo> mr = merchantInfoService.selectById(orderRecord.get().getMerchantId());
                    if(mr.isPresent()){
                        Optional<UserInfo> ui = userInfoService.selectByMerchantId(mr.get().getId());
                        if(ui.isPresent()){
                            log.info("用户号",ui.get().getId());
                            log.info("openId",ui.get().getOpenId());
                            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            String total = decimalFormat.format(orderRecord.get().getTotalFee());
                            sendMsgService.sendMessage(total,orderRecord.get().getOrderId(),payResult.get("outTradeNo").toString(),mr.get().getMerchantName(),mr.get().getMerchantName(),ui.get().getOpenId());
                        }
                    }
                }catch (Exception e){
                    log.error("公众号通知消息发送失败");
                }

                /**
                 * ⑤开始提现
                 */
                try{
                    //⑤.开始提现申请
                    Pair<BigDecimal, BigDecimal> pair = shallProfitDetailService.withdrawParams(orderRecord.get().getMerchantId(),orderRecord.get().getPayChannel());
                    BigDecimal serviceFeeTemp = pair.getLeft();//金开门收钱
                    BigDecimal channelFeeTemp = pair.getRight();//上游收钱
                    //查询
                    Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.get().getMerchantId());
                    if(!merchantInfoOptional.isPresent()){
                        log.info("提现商户不存在");
                    }
                    OrderRecord or = new OrderRecord();
                    or.setOrderId(orderRecord.get().getOrderId());
                    or.setMerchantId(orderRecord.get().getMerchantId());
                    or.setMerchantType(EnumMerchantType.MERCHANT.getId());
                    or.setProductName("提现");
                    or.setSubName(merchantInfoOptional.get().getMerchantName());
                    or.setPayChannel(orderRecord.get().getPayChannel());
                    or.setTotalFee(orderRecord.get().getRealFee());
                    or.setRealFee((orderRecord.get().getRealFee()).subtract(serviceFeeTemp));
                    or.setServiceFee(serviceFeeTemp);
                    or.setChannelFee(channelFeeTemp);
                    or.setPayResult(EnumPayResult.UNPAY.getId());
                    or.setSettlePeriod(EnumSettlePeriodType.D0.getId());
                    or.setSettleStatus(EnumSettleStatus.SETTLE.getId());
                    or.setPayParams("");
                    or.setStatus(EnumCommonStatus.NORMAL.getId());
                    or.setBody("提现");
                    or.setTradeType(EnumTradeType.DEPOSITOR.getId());
                    try{
                        orderRecordDao.insertSelective(or);
                        log.info("结束创建订单。。。");
                    }catch(Exception e){
                        log.error("创建订单异常{}",e);
                        log.error("创建订单失败。。。");
                    }
                    log.info("提现下单结束。。。");


                    log.info("开始组装数据。。。");
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    paramsMap.put("amount", decimalFormat.format(or.getRealFee()));
                    paramsMap.put("orderId", or.getOrderId());
                    paramsMap.put("bitchNo", or.getOrderId());
                    paramsMap.put("payType", EnumSettlementPeriodType.D0.getId());
                    paramsMap.put("phoneNo", MerchantSupport.decryptMobile(merchantInfoOptional.get().getReserveMobile()));
                    paramsMap.put("bankName", merchantInfoOptional.get().getBankName());
                    paramsMap.put("accountName", merchantInfoOptional.get().getName());
                    paramsMap.put("accountNumber", MerchantSupport.decryptBankCard(merchantInfoOptional.get().getBankNo()));
                    paramsMap.put("note", "提现");
                    paramsMap.put("appId","wap_hss");
                    paramsMap.put("notifyUrl", "http://"+WxConstants.DOMAIN+"/call/otherPayBack");     //后台通知url
                    paramsMap.put("idCard", MerchantSupport.decryptIdentity(merchantInfoOptional.get().getIdentity()));
                    paramsMap.put("isCompay", EnumIsCompay.ToPrivate.getId());
                    log.info("组装数据结束。。。,数据为{}",JSONObject.fromObject(paramsMap).toString());
                    log.info("开始向支付中心发请求。。。");
                    log.info("更该订单状态为【处理中】。。。");
                    or.setPayResult(EnumPayResult.HAND.getId());
                    try{
                        orderRecordDao.updateByPrimaryKeySelective(or);
                        log.info("更该订单状态为【处理中】结束。。。");
                    }catch (Exception e){
                        log.info("交易成功后代付更改更该订单信息失败{}",e);
                    }
                    //请求支付中心
                    try{
                        String result = SmPost.post(MerchantConsts.getMerchantConfig().domain()+MerchantConsts.getMerchantConfig().otherPay(),paramsMap);
                        log.info("向支付中心发请求结束。。。，结果是{}",result);
                        if(result!=null&&!"".equals(result)){
                            log.info("支付中心返回代付结果是{}",result);
                            JSONObject jo = JSONObject.fromObject(result);
                            DfmResponse dfmResponse = (DfmResponse)JSONObject.toBean(jo, DfmResponse.class);
                            if("success".equals(dfmResponse.getError_code())){
                                log.info("代付success，订单号是{}",or.getOrderId());
                                or.setPayResult(EnumPayResult.ACCEPT.getId());
                                or.setOutTradeNo(dfmResponse.getSys_order_id());
                                or.setErrorMessage(dfmResponse.getMessage());
                                or.setPayTime(new Date());
                                or.setResultParams(result);
                                try{
                                    orderRecordDao.updateByPrimaryKeySelective(or);
                                    log.info("代付success,更改订单状态成功，订单号是{}",or.getOrderId());
                                }catch (Throwable e){
                                    log.error("代付success,更改订单状态异常，订单号是{}",or.getOrderId());
                                    log.error("代付success,更改订单状态异常{}",e.getStackTrace());
                                }
                            }else if("fail".equals(dfmResponse.getError_code())){
                                log.info("代付fail，更改订单信息，订单号是{}",or.getOrderId());
                                or.setPayResult(EnumPayResult.FAIL.getId());
                                or.setOutTradeNo(dfmResponse.getSys_order_id());
                                or.setErrorMessage(dfmResponse.getMessage());
                                or.setPayTime(new Date());
                                or.setResultParams(result);
                                try{
                                    orderRecordDao.updateByPrimaryKeySelective(or);
                                    log.info("代付fail，更改订单信息成功，订单号是{}",or.getOrderId());
                                }catch (Throwable e){
                                    log.error("代付fail，更改订单信息异常，订单号是{}",or.getOrderId());
                                    log.error("代付fail，更该订单信息异常{}",e.getStackTrace());
                                }
                            }else if("invalid".equals(dfmResponse.getError_code())){//签名失败
                                log.info("代付invalid，更改订单信息，订单号是{}",or.getOrderId());
                                or.setPayResult(EnumPayResult.FAIL.getId());
                                or.setOutTradeNo(dfmResponse.getSys_order_id());
                                or.setErrorMessage(dfmResponse.getMessage());
                                or.setPayTime(new Date());
                                or.setResultParams(result);
                                try{
                                    orderRecordDao.updateByPrimaryKeySelective(or);
                                    log.info("代付invalid，更改订单信息成功，订单号是{}",or.getOrderId());
                                }catch (Throwable e){
                                    log.error("代付invalid，更该订单信息异常，订单号是{}",or.getOrderId());
                                    log.error("代付invalid，更该订单信息异常{}",e.getStackTrace());
                                }
                            }else if("exception".equals(dfmResponse.getError_code())){
                                log.info("代付exception，更改订单信息，订单号是{}",or.getOrderId());
                                or.setPayResult(EnumPayResult.EXCEPTION.getId());
                                or.setOutTradeNo(dfmResponse.getSys_order_id());
                                or.setErrorMessage(dfmResponse.getMessage());
                                or.setPayTime(new Date());
                                or.setResultParams(result);
                                //结算
                                try{
                                    orderRecordDao.updateByPrimaryKeySelective(or);
                                    log.info("代付exception，更改订单信息成功，订单号是{}",or.getOrderId());
                                }catch (Throwable e){
                                    log.error("代付exception，更该订单信息异常，订单号是{}",or.getOrderId());
                                    log.error("代付exception，更该订单信息异常{}",e.getStackTrace());
                                }
                            }else if("wait".equals(dfmResponse.getError_code())){
                                log.info("代付wait，更改订单信息，订单号是{}",or.getOrderId());
                                or.setPayResult(EnumPayResult.WAIT.getId());
                                or.setOutTradeNo(dfmResponse.getSys_order_id());
                                or.setErrorMessage(dfmResponse.getMessage());
                                or.setPayTime(new Date());
                                or.setResultParams(result);
                                try{
                                    orderRecordDao.updateByPrimaryKeySelective(or);
                                    log.info("代付wait，更改订单信息成功，订单号是{}",or.getOrderId());
                                }catch (Throwable e){
                                    log.error("代付wait，更该订单信息异常，订单号是{}",or.getOrderId());
                                    log.error("代付wait，更该订单信息异常{}",e.getStackTrace());
                                }
                            }
                        }else{
                            log.error("提现异常,订单号是{}",or.getId());
                            or.setPayResult(EnumPayResult.EXCEPTION.getId());
                            or.setErrorMessage("请求异常");
                            orderRecordDao.updateByPrimaryKeySelective(or);;
                        }

                    }catch (Exception e){
                        log.info("请求支付中心异常，订单号是{}",or.getOrderId());
                        or.setPayResult(EnumPayResult.EXCEPTION.getId());
                        or.setErrorMessage("系统异常");
                        orderRecordDao.updateByPrimaryKeySelective(or);
                    }
                }catch(Exception e){
                    log.info("代付异常{}，订单号是{}",e,orderRecord.get().getOrderId());
                }

            }

        }
    }



    @Override
    public int updateParam(String resultParams,String outTradeNo,long id) {
        return orderRecordDao.updateParam(resultParams,outTradeNo,id);
    }



    @Override
    public List<OrderRecordConditions> selectOrderRecordByConditions(OrderRecordConditions orderRecordConditions) {
        return orderRecordDao.selectOrderRecordByConditions(orderRecordConditions);
    }

    /**
     * 计算提现分润
     * @param orderRecord
     */
    private void calculationProfit(OrderRecord orderRecord){
        /**
         * ①查询收益
         */
        Map<String, Triple<Long, BigDecimal, String>> map= null;
        try{
            log.info("代付success,开始查询订单号为{}的收益。。。",orderRecord.getOrderId());
//            map = shallProfitDetailService.withdrawProfitCount(orderRecord);
            log.info("代付success,查询收益成功");
        }catch(Throwable e){
            log.error("代付success,订单为{}的【提现收益计算】异常",orderRecord.getOrderId());
            log.error("代付success,【提现收益计算】异常{}",e.getStackTrace());
        }
        //利润(服务费减去通道费)
        BigDecimal profit = (orderRecord.getServiceFee()).subtract(orderRecord.getChannelFee());
        /**
         * ②总出账
         */
        try{
            log.info("代付success,开始计算金开门总出账。。。");
            BigDecimal money = orderRecord.getTotalFee().subtract(orderRecord.getChannelFee());
            jkmAccountService.outMoney(money,1);
            log.info("代付success,金开门出账成功。。。");
        }catch(Throwable e){
            log.error("代付success,订单为{}的【提现总出账】异常",orderRecord.getOrderId());
            log.error("代付success,【提现总出账】异常{}",e.getStackTrace());
        }
        /**
         * ④分佣
         */
        //手续费容器
        try{
            log.info("代付success,分佣开始。。。");
            TradeFeeRecord tradeFeeRecord = new TradeFeeRecord();
            tradeFeeRecord.setMerchantId(orderRecord.getMerchantId());
            tradeFeeRecord.setOrderId(orderRecord.getId());
            tradeFeeRecord.setTradeType(EnumTradeType.DEPOSITOR.getId());
            tradeFeeRecord.setTotalFee(profit);
            tradeFeeRecord.setStatus(0);
            tradeFeeRecordService.insertSelective(tradeFeeRecord);
            //4.给一级代理商账号加钱，并记录
            log.info("代付success,跟一级代理商分佣开始。。。");
            if(map.get("firstMoney")!=null){
                log.info("代付success,进入一级代理商分佣。。。");
                accountInfoService.addUnsettledMoney(map.get("firstMoney").getLeft(),map.get("firstMoney").getMiddle());
                PastRecord pastRecord = new PastRecord();
                pastRecord.setPastNo(SnGenerator.generate());
                pastRecord.setAccounId(map.get("firstMoney").getLeft());
                pastRecord.setTradeId(tradeFeeRecord.getId());
                pastRecord.setOrderId(orderRecord.getId());
                pastRecord.setTotalFee(map.get("firstMoney").getMiddle());
                pastRecord.setSettlePeriod(map.get("firstMoney").getRight());
                pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                pastRecord.setTradeType(EnumTradeType.DEPOSITOR.getId());
                pastRecordService.insertSelective(pastRecord);
            }
            log.info("代付success,跟一级代理商分佣结束。。。");
            //5.给二级代理商账号加钱，并记录
            log.info("代付success,跟二级代理商分佣开始。。。");
            if(map.get("secondMoney")!=null){
                log.info("代付success,进入二级代理商分佣。。。");
                accountInfoService.addUnsettledMoney(map.get("secondMoney").getLeft(),map.get("secondMoney").getMiddle());
                PastRecord pastRecord = new PastRecord();
                pastRecord.setPastNo(SnGenerator.generate());
                pastRecord.setAccounId(map.get("secondMoney").getLeft());
                pastRecord.setTradeId(tradeFeeRecord.getId());
                pastRecord.setOrderId(orderRecord.getId());
                pastRecord.setTotalFee(map.get("secondMoney").getMiddle());
                pastRecord.setSettlePeriod(map.get("secondMoney").getRight());
                pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                pastRecord.setTradeType(EnumTradeType.DEPOSITOR.getId());
                pastRecordService.insertSelective(pastRecord);
            }
            log.info("代付success,跟二级代理商分佣结束。。。");
            //6.给通道账号加钱，并记录
            log.info("代付success,给通道账户加钱开始。。。");
            if(map.get("channelMoney")!=null){
                log.info("代付success,进入给通道账户加钱。。。");
                accountInfoService.addUnsettledMoney(map.get("channelMoney").getLeft(),map.get("channelMoney").getMiddle());
                PastRecord pastRecord = new PastRecord();
                pastRecord.setPastNo(SnGenerator.generate());
                pastRecord.setAccounId(map.get("channelMoney").getLeft());
                pastRecord.setTradeId(tradeFeeRecord.getId());
                pastRecord.setOrderId(orderRecord.getId());
                pastRecord.setTotalFee(map.get("channelMoney").getMiddle());
                pastRecord.setSettlePeriod(map.get("channelMoney").getRight());
                pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                pastRecord.setTradeType(EnumTradeType.DEPOSITOR.getId());
                pastRecordService.insertSelective(pastRecord);
            }
            log.info("代付success,给通道账户加钱结束。。。");
            //7.给产品账号加钱，并记录
            log.info("代付success,给产品账户加钱开始。。。");
            if(map.get("productMoney")!=null){
                log.info("代付success,进入给产品账户加钱。。。");
                accountInfoService.addUnsettledMoney(map.get("productMoney").getLeft(),map.get("productMoney").getMiddle());
                PastRecord pastRecord = new PastRecord();
                pastRecord.setPastNo(SnGenerator.generate());
                pastRecord.setAccounId(map.get("productMoney").getLeft());
                pastRecord.setTradeId(tradeFeeRecord.getId());
                pastRecord.setOrderId(orderRecord.getId());
                pastRecord.setTotalFee(map.get("productMoney").getMiddle());
                pastRecord.setSettlePeriod(map.get("productMoney").getRight());
                pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                pastRecord.setTradeType(EnumTradeType.DEPOSITOR.getId());
                pastRecordService.insertSelective(pastRecord);
            }
            log.info("代付success,给产品账户加钱结束。。。");
        }catch (Throwable e){
            log.error("代付success,订单号为{}，【提现分佣】失败",orderRecord.getOrderId());
            log.error("代付success【提现分佣】失败{}",e);
        }
    }


    /**
     * 定时查询代付结果
     * 1.查询状态为H、A和W的订单
     * 2.10分钟查询一次
     */
    @Override
    public void regularlyCheckOtherPayResult(){
        List<OrderRecord> list = orderRecordDao.selectOtherPayByConditions();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                otherPayBack(list.get(i));
            }
        }
    }

    private void otherPayBack(OrderRecord orderRecord){
        if(EnumPayResult.HAND.equals(orderRecord.getPayResult())||EnumPayResult.WAIT.equals(orderRecord.getPayResult())){//只有待支付、等待的状态查询
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("orderId", orderRecord.getOrderId());     //商品描述
            log.error("代付查询订单号为{}",orderRecord.getOrderId());
            String result = SmPost.post(MerchantConsts.getMerchantConfig().domain()+MerchantConsts.getMerchantConfig().dfQuery(),paramsMap);
            if(result!=null){
                JSONObject jo = JSONObject.fromObject(result);
                DfQueryResponse dfQueryResponse = (DfQueryResponse)JSONObject.toBean(jo,DfQueryResponse.class);
                if(dfQueryResponse.getCode()==1){//查询出结果
                    if(dfQueryResponse.getData().getBankStatus()!=null&&!"".equals(dfQueryResponse.getData().getBankStatus())){
                        if("S".equals(dfQueryResponse.getData().getBankStatus())){
                            Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
                            if(merchantInfoOptional.isPresent()){
                                try{
                                    accountInfoService.inMoney(orderRecord.getTotalFee(),merchantInfoOptional.get().getAccountId());
                                    log.info("代付成功，订单号是{}",orderRecord.getOrderId());
                                    orderRecord.setPayResult(EnumPayResult.SUCCESS.getId());
                                    orderRecord.setOutTradeNo(dfQueryResponse.getData().getOutTradeNo());
                                    orderRecord.setErrorMessage(dfQueryResponse.getMessage());
                                    orderRecord.setPayTime(new Date());
                                    orderRecord.setResultParams(result);
                                    try{
                                        orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                                        log.info("代付success,更改订单状态成功，订单号是{}",orderRecord.getOrderId());
                                    }catch (Throwable e){
                                        log.error("代付success,更改订单状态异常，订单号是{}",orderRecord.getOrderId());
                                        log.error("代付success,更改订单状态异常{}",e.getStackTrace());
                                    }
                                }catch (Throwable e){
                                    log.error("订单号为{}的订单入账异常",orderRecord.getOrderId());
                                }
                                try{
                                    log.info("计算收益开始+++++++++++");
                                    calculationProfit(orderRecord);
                                    log.info("计算收益结束+++++++++++");
                                }catch (Throwable e){
                                    log.error("订单号为{}的订单计算收益异常",orderRecord.getOrderId());
                                }
                                //代付成功通知
                                try{
                                    log.info("商户号{}",orderRecord.getMerchantId());
                                    Optional<UserInfo> ui = userInfoService.selectByMerchantId(merchantInfoOptional.get().getId());
                                    if(ui.isPresent()){
                                        log.info("用户号",ui.get().getId());
                                        log.info("openId",ui.get().getOpenId());
//                                      sendMsgService.sendMessage(orderRecord.get().getTotalFee().toString(),orderRecord.get().getOrderId(),payResult.get("outTradeNo").toString(),mr.get().getMerchantName(),mr.get().getMerchantName(),ui.get().getOpenId());
                                    }
                                }catch (Exception e){
                                    log.error("公众号通知提现消息发送失败");
                                }

                            }else{
                                log.error("商户号为{}的账户不存在",orderRecord.getMerchantId());
                            }
                        }else if("F".equals(dfQueryResponse.getData().getBankStatus())){
                            log.info("订单号为{}的订单提现失败",orderRecord.getOrderId());
                            Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
                            if(merchantInfoOptional.isPresent()){
                                log.info("代付失败，订单号是{}",orderRecord.getOrderId());
                                try{
                                    log.info("代付失败，开始解冻资金。。。");
                                    accountInfoService.backMoney(orderRecord.getTotalFee(),merchantInfoOptional.get().getAccountId());
                                    log.info("代付失败，解冻资金结束。。。");
                                }catch (Throwable e){
                                    log.error("代付失败,解冻异常{}",e);
                                    log.error("代付失败,解冻异常,订单号是{}",orderRecord.getOrderId());
                                }
                                log.info("代付失败，更改订单信息，订单号是{}",orderRecord.getOrderId());
                                orderRecord.setPayResult(EnumPayResult.FAIL.getId());
                                orderRecord.setOutTradeNo(dfQueryResponse.getData().getOutTradeNo());
                                orderRecord.setErrorMessage(dfQueryResponse.getMessage());
                                orderRecord.setPayTime(new Date());
                                orderRecord.setResultParams(result);
                                try{
                                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                                    log.info("代付失败，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                                }catch (Throwable e){
                                    log.error("代付失败，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
                                    log.error("代付失败，更该订单信息异常{}",e.getStackTrace());
                                }
                            }else{
                                log.error("商户号为{}的账户不存在",orderRecord.getMerchantId());
                            }
                        }
                    }
                }else{
                    log.error("订单号为{}的订单查询异常",orderRecord.getOrderId());
                }
            }else{
                log.error("订单号为{}的订单查询异常",orderRecord.getOrderId());
            }

        }
    }

    /**
     * 定时查询支付结果
     * 1.查询状态为A的订单
     * 2.2分钟查询一次
     */
    @Override
    public void regularlyCheckPayResult(){
        List<OrderRecord> list = orderRecordDao.selectPayByConditions();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                payBack(list.get(i));
            }
        }
    }

    @Override
    public int selectOrderRecordByConditionsCount(OrderRecordConditions orderRecordConditions) {
        return orderRecordDao.selectOrderRecordByConditionsCount(orderRecordConditions);
    }

    private  Pair<String,String> otherPayOf(String status) {
        String result="";
        String message = "";
        if("N".equals(status)){
            result="N";
            message="待审核";
        }
        if("H".equals(status)||"W".equals(status)||"A".equals(status)||"E".equals(status)){
            result="H";
            message="提现中";
        }
        if("S".equals(status)){
            result="S";
            message="提现成功";
        }
        if("F".equals(status)){
            result="F";
            message="提现失败";
        }
        if("O".equals(status)){
            result="O";
            message="审核未通过";
        }
        if("D".equals(status)){
            result="D";
            message="交易关闭";
        }
        return Pair.of(result,message);
    }

    private List<String>  backOtherPayOf(String status) {
        List<String> payResults = new ArrayList<String>();
        if("N".equals(status)){
            payResults.add("N");
        }
        if("H".equals(status)){
            payResults.add("H");
            payResults.add("W");
            payResults.add("A");
            payResults.add("E");
        }
        if("S".equals(status)){
            payResults.add("S");
        }
        if("F".equals(status)){
            payResults.add("F");
        }
        if("O".equals(status)){
            payResults.add("O");
        }
        if("D".equals(status)){
            payResults.add("D");
        }
        return payResults;
    }


    /**
     *
     * N 待审核
     * H 提现中
     * S 提现成功
     * F 提现失败
     * O 审核未通过
     * @param req
     * @return
     */
    @Override
    public List<OrderRecordAndMerchant> selectDrawWithRecordByPage(OrderRecordAndMerchantRequest req) {
        List<String> payResults = backOtherPayOf(req.getPayResult());
        req.setPayResults(payResults);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("merchantId",req.getMerchantId());
        map.put("payResults",req.getPayResults());
        map.put("mobile",req.getMobile());
        map.put("bankNoShort",req.getBankNoShort());
        map.put("startDate",req.getStartDate());
        map.put("endDate",req.getEndDate());
        map.put("payStartDate",req.getPayStartDate());
        map.put("payEndDate",req.getPayEndDate());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        List<OrderRecordAndMerchant> list = orderRecordDao.selectDrawWithRecordByPage(map);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                Pair<String,String> result = otherPayOf(list.get(i).getPayResult());
                list.get(i).setPayResult(result.getLeft());
                if(!"O".equals(result.getLeft())){
                    list.get(i).setErrorMessage(result.getRight());
                }
                if(list.get(i).getBankNo()!=null && !"".equals(list.get(i).getBankNo())){
                    list.get(i).setBankNo(MerchantSupport.decryptBankCard(list.get(i).getBankNo()));
                }
            }
        }
        return list;
    }

    @Override
    public long selectDrawWithCount(OrderRecordAndMerchantRequest req) {
        List<String> payResults = backOtherPayOf(req.getPayResult());
        req.setPayResults(payResults);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("merchantId",req.getMerchantId());
        map.put("payResults",req.getPayResults());
        map.put("mobile",req.getMobile());
        map.put("bankNoShort",req.getBankNoShort());
        map.put("startDate",req.getStartDate());
        map.put("endDate",req.getEndDate());
        map.put("payStartDate",req.getPayStartDate());
        map.put("payEndDate",req.getPayEndDate());
        return orderRecordDao.selectDrawWithCount(map);
    }
    @Transactional
    @Override
    public JSONObject unPass(long id,String message) {
        JSONObject resultJo = new JSONObject();
        OrderRecord orderRecord = orderRecordDao.selectByPrimaryKey(id);
        if(orderRecord==null){
            resultJo.put("code",-1);
            resultJo.put("message","查无此订单");
            return resultJo;
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
        if(merchantInfoOptional.isPresent()){
            if(merchantInfoOptional.get().getAccountId()>0){
                log.info("代付失败，订单号是{}",orderRecord.getOrderId());
                try{
                    log.info("代付失败，开始解冻资金。。。");
                    accountInfoService.backMoney(orderRecord.getTotalFee(),merchantInfoOptional.get().getAccountId());
                    log.info("代付失败，解冻资金结束。。。");
                }catch (Throwable e){
                    log.error("代付失败,解冻异常{}",e);
                    log.error("代付失败,解冻异常,订单号是{}",orderRecord.getOrderId());
                }
                log.info("代付失败，更改订单信息，订单号是{}",orderRecord.getOrderId());
                orderRecord.setPayResult(EnumPayResult.UNPASS.getId());
                orderRecord.setErrorMessage(message);
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("代付失败，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("代付失败，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
                    log.error("代付失败，更该订单信息异常{}",e.getStackTrace());
                }
                resultJo.put("code",1);
                resultJo.put("message","操作成功");
            }else{
                resultJo.put("code",-1);
                resultJo.put("message","查无此账户");
            }
        }else{
            resultJo.put("code",-1);
            resultJo.put("message","查无此商户");
            log.info("商户号为{}的账户不存在",orderRecord.getMerchantId());
        }
        return resultJo;
    }

    @Transactional
    @Override
    public JSONObject unfreeze(long id) {
        JSONObject resultJo = new JSONObject();
        OrderRecord orderRecord = orderRecordDao.selectByPrimaryKey(id);
        if(orderRecord==null){
            resultJo.put("code",-1);
            resultJo.put("message","查无此订单");
            return resultJo;
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
        if(merchantInfoOptional.isPresent()){
            if(merchantInfoOptional.get().getAccountId()>0){
                log.info("代付失败，订单号是{}",orderRecord.getOrderId());
                try{
                    log.info("代付失败，开始解冻资金。。。");
                    accountInfoService.backMoney(orderRecord.getTotalFee(),merchantInfoOptional.get().getAccountId());
                    log.info("代付失败，解冻资金结束。。。");
                }catch (Throwable e){
                    log.error("代付失败,解冻异常{}",e);
                    log.error("代付失败,解冻异常,订单号是{}",orderRecord.getOrderId());
                }
                log.info("代付失败，更改订单信息，订单号是{}",orderRecord.getOrderId());
                orderRecord.setPayResult(EnumPayResult.DOWN.getId());
                try{
                    orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                    log.info("代付失败，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                }catch (Throwable e){
                    log.error("代付失败，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
                    log.error("代付失败，更该订单信息异常{}",e.getStackTrace());
                }
                resultJo.put("code",1);
                resultJo.put("message","操作成功");
            }else{
                resultJo.put("code",-1);
                resultJo.put("message","查无此账户");
            }
        }else{
            resultJo.put("code",-1);
            resultJo.put("message","查无此商户");
            log.info("商户号为{}的账户不存在",orderRecord.getMerchantId());
        }
        return resultJo;
    }

    @Override
    public Optional<OrderRecord> selectByPrimaryKey(long id) {
        return Optional.fromNullable(orderRecordDao.selectByPrimaryKey(id));
    }


    private void payBack(OrderRecord orderRecord){
        if(EnumPayResult.HAND.equals(orderRecord.getPayResult())){//只有待支付、受理成功的状态查询
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("payNum", orderRecord.getOrderId());     //系统订单号
            paramsMap.put("outTradeNo", orderRecord.getOutTradeNo());     //扫米返回的订单号
            log.error("支付查询订单号为{}",orderRecord.getOrderId());
            String result = SmPost.post(MerchantConsts.getMerchantConfig().domain()+MerchantConsts.getMerchantConfig().orderQuery(),paramsMap);
            if(result!=null){
                JSONObject jo = JSONObject.fromObject(result);
                OrderQueryResponse orderQueryResponse = (OrderQueryResponse)JSONObject.toBean(jo,OrderQueryResponse.class);
                if(orderQueryResponse.getCode()==1){//查询出结果
                    if("SUCCESS".equals(orderQueryResponse.getData().getTradeState())){
                        if(orderRecord==null){
                            log.error("查找不到订单");
                        }else if(orderQueryResponse.getData().getOrderId().equals(orderRecord.getOrderId())){
                            log.error("订单号有误");
                        }else{
                            /**
                             * ①计算收益
                             */
                            Map<String, Triple<Long, BigDecimal, String>> map=null;
                            try{
//                                map = dealerService.shallProfit(orderRecord);
                            }catch (Throwable e){
                                log.error("订单为{}的【收益计算】错误",orderRecord.getOrderId());
                                log.error("【收益计算】异常{}",e);
                                log.error("【收益计算】异常"+e.getMessage());
                            }
                            //商户实际所得钱
                            BigDecimal realFee = map.get("merchant").getMiddle();
                            //通道的费（支付平台收金开门的钱）
                            BigDecimal channelFee = orderRecord.getTotalFee().subtract(map.get("merchantAndProfit").getMiddle());
                            //服务费（金开门收商户的钱）
                            BigDecimal serviceFee = orderRecord.getTotalFee().subtract(map.get("merchant").getMiddle());
                            //利润(服务费减去通道费)
                            BigDecimal profit = serviceFee.subtract(channelFee);

                            /**
                             * ②总入账
                             */
                            try{
                                jkmAccountService.income(orderRecord.getTotalFee(),map.get("merchantAndProfit").getMiddle(),1);
                            }catch(Throwable e){
                                log.error("订单为{}的【总入账】错误",orderRecord.getOrderId());
                                log.error("【总入账】异常{}",e);
                                log.error("【总入账】异常"+e.getMessage());
                            }

                            /**
                             * ③修改订单记录，给商户实时结算
                             */
                            orderRecord.setRealFee(realFee);
                            orderRecord.setServiceFee(serviceFee);
                            orderRecord.setChannelFee(channelFee);
                            orderRecord.setPayResult(EnumPayResult.SUCCESS.getId());
                            orderRecord.setErrorMessage(orderQueryResponse.getMessage()==null?"":orderQueryResponse.getMessage());
                            orderRecord.setPayTime(new Date());
                            try{
                                orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                            }catch (Throwable e){
                                log.error("更改订单{}失败",orderRecord.getOrderId());
                                log.error("更改订单失败{}",e);
                                log.error("更改订单失败"+e.getMessage());
                            }
                            if(map.get("merchant")!=null){
                                try{
                                    accountInfoService.addAvailableMoney(map.get("merchant").getLeft(),map.get("merchant").getMiddle());
                                }catch (Throwable e){
                                    log.error("订单号为{}，【入账】失败",orderRecord.getOrderId());
                                    log.error("【入账】失败{}",e);
                                    log.error("【入账】失败"+e.getMessage());
                                    payExceptionRecordService.insertByCondition(orderRecord.getOrderId(),orderRecord.getOutTradeNo(), EnumPayResult.EXCEPTION.getId());
                                }
                            }else{
                                log.error("订单为{}的商户收益为空，调用收益接口错误",orderRecord.getOrderId());
                            }

                            /**
                             * ④分佣
                             */
                            //手续费容器
                            try{
                                TradeFeeRecord tradeFeeRecord = new TradeFeeRecord();
                                tradeFeeRecord.setMerchantId(orderRecord.getMerchantId());
                                tradeFeeRecord.setOrderId(orderRecord.getId());
                                tradeFeeRecord.setTradeType(EnumTradeType.DEAL.getId());
                                tradeFeeRecord.setTotalFee(profit);
                                tradeFeeRecord.setStatus(0);
                                tradeFeeRecordService.insertSelective(tradeFeeRecord);
                                //4.给一级代理商账号加钱，并记录
                                if(map.get("firstMoney")!=null){
                                    accountInfoService.addUnsettledMoney(map.get("firstMoney").getLeft(),map.get("firstMoney").getMiddle());
                                    PastRecord pastRecord = new PastRecord();
                                    pastRecord.setPastNo(SnGenerator.generate());
                                    pastRecord.setAccounId(map.get("firstMoney").getLeft());
                                    pastRecord.setTradeId(tradeFeeRecord.getId());
                                    pastRecord.setOrderId(orderRecord.getId());
                                    pastRecord.setTotalFee(map.get("firstMoney").getMiddle());
                                    pastRecord.setSettlePeriod(map.get("firstMoney").getRight());
                                    pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                                    pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                                    pastRecordService.insertSelective(pastRecord);
                                }
                                //5.给二级代理商账号加钱，并记录
                                if(map.get("secondMoney")!=null){
                                    accountInfoService.addUnsettledMoney(map.get("secondMoney").getLeft(),map.get("secondMoney").getMiddle());
                                    PastRecord pastRecord = new PastRecord();
                                    pastRecord.setPastNo(SnGenerator.generate());
                                    pastRecord.setAccounId(map.get("secondMoney").getLeft());
                                    pastRecord.setTradeId(tradeFeeRecord.getId());
                                    pastRecord.setOrderId(orderRecord.getId());
                                    pastRecord.setTotalFee(map.get("secondMoney").getMiddle());
                                    pastRecord.setSettlePeriod(map.get("secondMoney").getRight());
                                    pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                                    pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                                    pastRecordService.insertSelective(pastRecord);
                                }
                                //6.给通道账号加钱，并记录
                                if(map.get("channelMoney")!=null){
                                    accountInfoService.addUnsettledMoney(map.get("channelMoney").getLeft(),map.get("channelMoney").getMiddle());
                                    PastRecord pastRecord = new PastRecord();
                                    pastRecord.setPastNo(SnGenerator.generate());
                                    pastRecord.setAccounId(map.get("channelMoney").getLeft());
                                    pastRecord.setTradeId(tradeFeeRecord.getId());
                                    pastRecord.setOrderId(orderRecord.getId());
                                    pastRecord.setTotalFee(map.get("channelMoney").getMiddle());
                                    pastRecord.setSettlePeriod(map.get("channelMoney").getRight());
                                    pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                                    pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                                    pastRecordService.insertSelective(pastRecord);
                                }
                                //7.给产品账号加钱，并记录
                                if(map.get("productMoney")!=null){
                                    accountInfoService.addUnsettledMoney(map.get("productMoney").getLeft(),map.get("productMoney").getMiddle());
                                    PastRecord pastRecord = new PastRecord();
                                    pastRecord.setPastNo(SnGenerator.generate());
                                    pastRecord.setAccounId(map.get("productMoney").getLeft());
                                    pastRecord.setTradeId(tradeFeeRecord.getId());
                                    pastRecord.setOrderId(orderRecord.getId());
                                    pastRecord.setTotalFee(map.get("productMoney").getMiddle());
                                    pastRecord.setSettlePeriod(map.get("productMoney").getRight());
                                    pastRecord.setSettleStatus(EnumSettleStatus.UNSETTLE.getId());
                                    pastRecord.setTradeType(EnumTradeType.DEAL.getId());
                                    pastRecordService.insertSelective(pastRecord);
                                }
                            }catch (Throwable e){
                                log.error("订单号为{}，【分佣】失败",orderRecord.getOrderId());
                                log.error("【分佣】失败{}",e);
                                log.error("【分佣】失败"+e.getMessage());
                                payExceptionRecordService.insertByCondition(orderRecord.getOrderId(),orderRecord.getOutTradeNo(), EnumPayResult.EXCEPTION.getId());
                            }
                            try{
                                log.info("商户号{}",orderRecord.getMerchantId());
                                Optional<MerchantInfo> mr = merchantInfoService.selectById(orderRecord.getMerchantId());
                                log.info("参数",JSONObject.fromObject(map).toString());
                                if(mr.isPresent()){
                                    Optional<UserInfo> ui = userInfoService.selectByMerchantId(mr.get().getId());
                                    if(ui.isPresent()){
                                        log.info("用户号",ui.get().getId());
                                        log.info("openId",ui.get().getOpenId());
                                        sendMsgService.sendMessage(orderRecord.getTotalFee().toString(),orderRecord.getOrderId(),orderRecord.getOutTradeNo(),mr.get().getMerchantName(),mr.get().getMerchantName(),ui.get().getOpenId());
                                    }

                                }
                            }catch (Exception e){
                                log.error("公众号通知消息发送失败");
                            }
                        }
                    }
                }else{
                    log.error("订单号为{}的订单查询异常",orderRecord.getOrderId());
                }
            }else{
                log.error("订单号为{}的订单查询异常",orderRecord.getOrderId());
            }

        }
    }


    private void otherPayBack(String outTradeNo,String tradeResult,String bankStatus,String payNum){
        OrderRecord orderRecord = orderRecordDao.selectOrderId(payNum,EnumTradeType.DEPOSITOR.getId());
        if(orderRecord!=null){
            if("S".equals(tradeResult)){
                if("S".equals(bankStatus)){//已成功
                    Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
                    if(merchantInfoOptional.isPresent()){
                        try{
                            accountInfoService.inMoney(orderRecord.getTotalFee(),merchantInfoOptional.get().getAccountId());
                            log.info("代付成功，订单号是{}",orderRecord.getOrderId());
                            orderRecord.setPayResult(EnumPayResult.SUCCESS.getId());
                            orderRecord.setSettleStatus(EnumSettleStatus.SETTLE.getId());
                            orderRecord.setOutTradeNo(outTradeNo);
                            orderRecord.setPayTime(new Date());
                            try{
                                orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                                log.info("代付success,更改订单状态成功，订单号是{}",orderRecord.getOrderId());
                            }catch (Throwable e){
                                log.error("代付success,更改订单状态异常，订单号是{}",orderRecord.getOrderId());
                                log.error("代付success,更改订单状态异常{}",e.getStackTrace());
                            }
                        }catch (Throwable e){
                            log.error("订单号为{}的订单入账异常",orderRecord.getOrderId());
                        }
                        try{
                            log.info("计算收益开始+++++++++++");
                            calculationProfit(orderRecord);
                            log.info("计算收益结束+++++++++++");
                        }catch (Throwable e){
                            log.error("订单号为{}的订单计算收益异常",orderRecord.getOrderId());
                        }
                        //代付成功通知
                        try{
                            log.info("商户号{}",orderRecord.getMerchantId());
                            Optional<UserInfo> ui = userInfoService.selectByMerchantId(merchantInfoOptional.get().getId());
                            if(ui.isPresent()){
                                log.info("用户号",ui.get().getId());
                                log.info("openId",ui.get().getOpenId());
                                DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                                String total = decimalFormat.format(orderRecord.getTotalFee());
                              sendMsgService.sendPushMessage(total,merchantInfoOptional.get().getBankName(),merchantInfoOptional.get().getBankNoShort(),ui.get().getOpenId());
                            }
                        }catch (Exception e){
                            log.error("公众号通知提现消息发送失败");
                        }

                    }else{
                        log.error("商户号为{}的账户不存在",orderRecord.getMerchantId());
                    }
                }
                if("F".equals(bankStatus)){//出款失败， 原因见打款明细查询fail_Desc
                    log.info("订单号为{}的订单提现失败",orderRecord.getOrderId());
                    Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
                    if(merchantInfoOptional.isPresent()){
                        log.info("代付失败，更改订单信息，订单号是{}",orderRecord.getOrderId());
                        orderRecord.setPayResult(EnumPayResult.FAIL.getId());
                        orderRecord.setOutTradeNo(outTradeNo);
                        try{
                            orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                            log.info("代付失败，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                        }catch (Throwable e){
                            log.error("代付失败，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
                            log.error("代付失败，更该订单信息异常{}",e.getStackTrace());
                        }
                    }else{
                        log.error("商户号为{}的商户不存在",orderRecord.getMerchantId());
                    }
                }
            }else if("F".equals(tradeResult)){
                log.info("订单号为{}的订单提现失败",orderRecord.getOrderId());
                Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(orderRecord.getMerchantId());
                if(merchantInfoOptional.isPresent()){
                    log.info("代付失败，更改订单信息，订单号是{}",orderRecord.getOrderId());
                    orderRecord.setPayResult(EnumPayResult.FAIL.getId());
                    orderRecord.setOutTradeNo(outTradeNo);
                    try{
                        orderRecordDao.updateByPrimaryKeySelective(orderRecord);
                        log.info("代付失败，更改订单信息成功，订单号是{}",orderRecord.getOrderId());
                    }catch (Throwable e){
                        log.error("代付失败，更改订单信息异常，订单号是{}",orderRecord.getOrderId());
                        log.error("代付失败，更该订单信息异常{}",e.getStackTrace());
                    }
                }else{
                    log.error("商户号为{}的商户不存在",orderRecord.getMerchantId());
                }
            }
        }else{
            log.info("代付查询不到订单，订单号{}",payNum);
        }
    }

    /**
     * 代付回调
     * @param outTradeNo
     * @param tradeResult
     * @param bankStatus
     * @param payNum
     */
    @Override
    public void otherPayResult(String outTradeNo,String tradeResult,String bankStatus,String payNum) {
        otherPayBack(outTradeNo,tradeResult,bankStatus,payNum);
    }


    private List<String>  PayOf(String status) {
        List<String> payResults = new ArrayList<String>();
        if("N".equals(status)){
            payResults.add("N");
        }
        if("H".equals(status)){
            payResults.add("H");
            payResults.add("W");
            payResults.add("A");
            payResults.add("E");
        }
        if("S".equals(status)){
            payResults.add("S");
        }
        if("F".equals(status)){
            payResults.add("F");
        }
        return payResults;
    }

    private String  PayOfStatus(String status) {
        String message = "";
        if("N".equals(status)){
            message="待支付";
        }
        if("H".equals(status)||"W".equals(status)||"A".equals(status)||"E".equals(status)){
            message="支付中";
        }
        if("S".equals(status)){
            message="支付成功";
        }
        if("F".equals(status)){
            message="支付失败";
        }
        return message;
    }
    @Override
    public List<MerchantAndOrderRecord> selectOrderListByPage(OrderListRequest req) {
        List<String> payResults = PayOf(req.getPayResult());
        req.setPayResults(payResults);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderId",req.getOrderId());
        map.put("startTime",req.getStartTime());
        map.put("endTime",req.getEndTime());
        map.put("merchantId",req.getMerchantId());
        map.put("subName",req.getSubName());
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("payResults",req.getPayResults());
        map.put("payChannel",req.getPayChannel());
        map.put("mdMobile",req.getMdMobile());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        List<MerchantAndOrderRecord> list = orderRecordDao.selectOrderList(map);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                list.get(i).setOrderMessage(PayOfStatus(list.get(i).getPayResult()));
            }
        }
        return list;
    }

    @Override
    public int selectOrderListCount(OrderListRequest req) {
        List<String> payResults = PayOf(req.getPayResult());
        req.setPayResults(payResults);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderId",req.getOrderId());
        map.put("startTime",req.getStartTime());
        map.put("endTime",req.getEndTime());
        map.put("merchantId",req.getMerchantId());
        map.put("subName",req.getSubName());
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("payResults",req.getPayResults());
        map.put("payChannel",req.getPayChannel());
        map.put("mdMobile",req.getMdMobile());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        return orderRecordDao.selectOrderListCount(map);
    }
}
