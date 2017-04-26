package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface OrderDao {

    /**
     * 插入
     *
     * @param order
     */
    void insert(Order order);

    /**
     * 更新
     *
     * @param order
     * @return
     */
    int update(Order order);

    /**
     * 更改交易备注
     *
     * @param id
     * @param remark
     * @return
     */
    int updateRemark(@Param("id") long id, @Param("remark") String remark);

    /**
     * 更新交易状态
     *
     * @param id
     * @param status
     */
    int updateStatus(@Param("id") long id, @Param("status") int status, @Param("remark") String remark);

    /**
     * 更新结算状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateSettleStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 更新对账状态
     *
     * @param id
     * @param checkedStatus
     * @return
     */
    int updateCheckedStatus(@Param("id") long id, @Param("checkedStatus") int checkedStatus);

    /**
     * 结算成功
     *
     * @param id
     * @return
     */
    int markSettleSuccess(@Param("id") long id, @Param("status") int status, @Param("successSettleTime") Date successSettleTime);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Order selectById(@Param("id") long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Order selectByIdWithLock(@Param("id") long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @param tradeType
     * @return
     */
    Order selectByOrderNoAndTradeType(@Param("orderNo") String orderNo, @Param("tradeType") int tradeType);

    /**
     * 根据支付单id查询打款单
     *
     * @param payOrderId
     * @return
     */
    Order selectByPayOrderId(@Param("payOrderId") long payOrderId);

    /**
     * 交易列表
     * @param map
     * @return
     */
    List<MerchantTradeResponse> selectOrderList(Map map);

    /**
     * 总页数
     * @param map
     * @return
     */
    int selectOrderListCount(Map map);

    /**
     * 交易详情
     * @param orderNo
     * @return
     */
    MerchantTradeResponse selectOrderListByPageAll(@Param("orderNo") String orderNo);

    /**
     * 查询商户信息
     * @param payee
     * @return
     */
    MerchantTradeResponse getMerchantAll(@Param("payee") long payee);

    /**
     * 查询代理商信息
     * @param dealerId
     * @return
     */
    MerchantTradeResponse getDealerAll(@Param("dealerId") long dealerId);

    /**
     * 查询二级代理商
     * @param firstLevelDealerId
     * @return
     */
    MerchantTradeResponse getProxyName1(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 查询商户的收款单-分页
     *
     * @param requestParam
     * @return
     */
    List<Order> selectMerchantPayOrders(QueryMerchantPayOrdersRequestParam requestParam);

    /**
     * 查询商户的收款单个数-分页
     *
     * @param requestParam
     * @return
     */
    long selectCountMerchantPayOrders(QueryMerchantPayOrdersRequestParam requestParam);

    /**
     * 按下游业务订单号查询
     *
     * @param businessOrderNo
     * @return
     */
    Order selectByBusinessOrderNo(@Param("businessOrderNo") String businessOrderNo);

    /**
     * 查询当前账户的交易总额
     *
     * @param accountId
     * @param appId
     * @param serviceType
     * @return
     */
    BigDecimal selectTotalTradeAmountByAccountId(@Param("accountId") long accountId, @Param("appId") String appId, @Param("serviceType") int serviceType);

    /**
     * 查询已经对过账的订单号
     *
     * @param orderNos
     * @return
     */
    List<String> selectCheckedOrderNosByOrderNos(@Param("orderNos") List<String> orderNos);

    /**
     * 分页查询--查询个数
     *
     * @param accountId
     * @param appId
     * @return
     */
    long selectPageOrdersCountByAccountId(@Param("accountId") long accountId, @Param("appId") String appId,
                                          @Param("date") Date date);

    /**
     * 分页查询--查询记录
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @return
     */
    List<Order> selectPageOrdersByAccountId(@Param("accountId") long accountId, @Param("appId") String appId,
                                            @Param("offset") int offset, @Param("count") int count,

                                            @Param("date") Date date);

    /**
     * 批量查询
     * @param orderNos
     * @return
     */
    List<Order> getByOrderNos(@Param("orderNos") List<String> orderNos);

    /**
     * 一二级筛选用
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getOrderList(OrderTradeRequest req);

    /**
     * 统计
     * @param req
     * @return
     */
    String amountCount(OrderTradeRequest req);

    /**
     * 查询指定结算日期，指定业务线，T1的支付成功的待结算的订单IDS
     *
     * @param settleDate
     * @param appId
     * @return
     */
    List<Long> selectT1PaySuccessAndUnSettleOrderIds(@Param("settleDate") Date settleDate, @Param("appId") String appId, @Param("channelList") List<Integer> channelList);

    /**
     * 下载查询
     * @param map
     * @return
     */
    List<MerchantTradeResponse> downloadOrderList(Map map);

    /**
     * 提现列表
     * @param req
     * @return
     */
    List<WithdrawResponse> withdrawList(WithdrawRequest req);

    /**
     * 总数
     * @param req
     * @return
     */
    int getNo(WithdrawRequest req);

    /**
     * 提现统计
     * @param req
     * @return
     */
    WithdrawResponse withdrawAmount(WithdrawRequest req);

    /**
     * 提现详情代理商
     * @param idd
     * @return
     */
    WithdrawResponse withdrawDetail(@Param("idd") long idd,@Param("createTimes") String createTimes);

    /**
     * 提现详情商户
     * @param idm
     * @return
     */
    WithdrawResponse withdrawDetails(@Param("idm") long idm,@Param("createTimes") String createTimes);

    /**
     * 查询代理打款
     * @param orderNo
     * @return
     */
    List<PlayResponse> getPlayMoney(@Param("orderNo") String orderNo);

    /**
     * 代理商pc交易查询二级
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getTrade(OrderTradeRequest req);

    /**
     * 代理商pc交易查询一级
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getTradeFirst(OrderTradeRequest req);

    /**
     *  代理商pc交易查询二级总数
     * @param req
     * @return
     */
    int listCount(OrderTradeRequest req);

    /**
     * 代理商pc交易查询一级总数
     * @param req
     * @return
     */
    int listFirstCount(OrderTradeRequest req);

    /**
     * 提现下载
     * @param req
     * @return
     */
    List<WithdrawResponse> withdrawList1(WithdrawRequest req);

    /**
     * 按订单号统计交易金额
     *
     * @param orderNos
     * @return
     */
    Map<String,BigDecimal> selectTradeAmountAndFeeByOrderNoList(@Param("orderNos") List<String> orderNos);

    /**
     * 按订单号s 查询交易（app）
     *
     * @param orderNos
     * @param offset
     * @param count
     * @return
     */
    List<Order> selectByAppParam(@Param("orderNos") List<String> orderNos, @Param("offset") int offset, @Param("count") int count);
}
