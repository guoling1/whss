package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.helper.request.*;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-25 20:08
 */
public interface OrderRecordService {
    /**
     * 收款记录
     * @param req
     * @return
     */
    List<OrderRecord> selectAllOrderRecordByPage(RequestOrderRecord req);
    /**
     * 收款记录条数
     * @param req
     * @return
     */
    long selectAllOrderRecordCount(RequestOrderRecord req);

    /**
     * 余额明细
     * @param req
     * @return
     */
    List<OrderRecord> selectBalanceByPage(RequestOrderRecord req);
    /**
     * 收款记录条数
     * @param merchantId
     * @return
     */
    long selectBalanceCount(long merchantId);

    Optional<OrderRecord> selectOrderId(String orderId,int tradeType);

    /**
     * 代理商提现
     * @param req
     * @return
     */
    public JSONObject dealerOtherPay(DfmRequest req);
    /**
     * 代付
     * @return
     */
    JSONObject otherPay(MerchantInfo merchantInfo);


    JSONObject checkWithdraw(WithDrawRequest req);
    /**
     * 支付
     * @param req
     * @return
     */
    JSONObject PayOrder(TradeRequest req);


    /**
     * 支付回调
     * @param payResult
     * @return
     */
    void payResult(Map payResult);

    /**
     * 代付回调
     * @param outTradeNo
     * @param tradeResult
     * @param payNum
     */
    void otherPayResult(String outTradeNo, String tradeResult, String bankStatus, String payNum);


    /**
     * 更改订单返回信息
     * @param resultParams
     * @return
     */
    int updateParam(String resultParams, String outTradeNo, long id);

    /**
     * 根据传入的参数查询OrderRecord
     * @param orderRecord
     * @return
     */
    List<OrderRecordConditions> selectOrderRecordByConditions(OrderRecordConditions orderRecord);

    /**
     * 定时查询代付结果
     */
    void regularlyCheckOtherPayResult();
    /**
     * 定时查询支付结果
     */
    void regularlyCheckPayResult();

    int selectOrderRecordByConditionsCount(OrderRecordConditions orderRecordConditions);



    /**
     * 提现记录
     * @param req
     * @return
     */
    List<OrderRecordAndMerchant> selectDrawWithRecordByPage(OrderRecordAndMerchantRequest req);
    /**
     * 提现条数
     * @param req
     * @return
     */
    long selectDrawWithCount(OrderRecordAndMerchantRequest req);

    /**
     * 未通过
     */
    JSONObject unPass(long id, String message);

    /**
     * 解冻
     */
    JSONObject unfreeze(long id);
    /**
     *
     * @param id
     * @return
     */
    Optional<OrderRecord> selectByPrimaryKey(long id);
    /**
     * 查询交易列表
     * @param orderRecord
     * @return
     */
    List<MerchantAndOrderRecord> selectOrderListByPage(OrderListRequest orderRecord);

    int selectOrderListCount(OrderListRequest orderRecord);

    /**
     * 查询交易详情
     * @param orderRecord
     * @return
     */
    MerchantAndOrderRecord selectOrderListByPageAll(OrderListRequest orderRecord);

    /**
     * 提现详情
     * @param req
     * @return
     */
    OrderRecordAndMerchant selectDrawWithRecordByPageAll(OrderRecordAndMerchantRequest req);
}
