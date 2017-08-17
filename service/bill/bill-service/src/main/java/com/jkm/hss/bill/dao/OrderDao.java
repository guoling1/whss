package com.jkm.hss.bill.dao;

import com.jkm.hss.account.entity.SplitAccountRefundRecord;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.helper.AppStatisticsOrder;
import com.jkm.hss.bill.helper.requestparam.OrderBalanceStatistics;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.merchant.entity.GeTuiResponse;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    long insert(Order order);

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
     * 更新退款信息
     *
     * @param id
     * @param refundAmount
     * @param refundStatus
     * @return
     */
    int updateRefundInfo(@Param("id") long id, @Param("refundAmount") String refundAmount, @Param("refundStatus") int refundStatus);


    /**
     * T1 生成商户结算单后，将结算单id标记到交易中，并将交易标记为结算中
     *
     * @param settleDate
     * @param accountId
     * @param settlementRecordId
     * @param settleStatus
     * @return
     */
    int markOrder2SettlementIng(@Param("settleDate") Date settleDate, @Param("accountId") long accountId,
                                @Param("settlementRecordId") long settlementRecordId,
                                @Param("settleStatus") int settleStatus,
                                @Param("upperChannel") int upperChannel);

    /**
     * 按结算单批量更新结算状态
     *
     * @param settlementRecordId
     * @param settleStatus
     * @return
     */
    int markOrder2SettlementSuccess(@Param("settlementRecordId") long settlementRecordId, @Param("settleStatus") int settleStatus, @Param("oriSettleStatus") int oriSettleStatus);

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
     * @param payee
     * @return
     */
    Order selectByBusinessOrderNoAndPayee(@Param("businessOrderNo") String businessOrderNo, @Param("payee") long payee);

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
     * 分页查询--查询个数
     *
     * @param accountId
     * @param appId
     * @param payChannelSigns
     * @param startTime
     * @param endTime
     * @return
     */
    long selectOrderCountByParam(@Param("accountId") long accountId, @Param("appId") String appId,
                                          @Param("payChannelSigns") List<Integer> payChannelSigns, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 分页查询--查询记录
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @return
     */
    List<Order> selectOrdersByParam(@Param("accountId") long accountId, @Param("appId") String appId,
                                            @Param("offset") int offset, @Param("count") int count,
                                           @Param("payChannelSigns") List<Integer> payChannelSigns, @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime);

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
     * 代理商pc交易查询二级
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getHsyTrade(OrderTradeRequest req);

    /**
     * 代理商pc交易查询一级
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getTradeFirst(OrderTradeRequest req);
    /**
     * 代理商pc交易查询一级
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getHsyTradeFirst(OrderTradeRequest req);

    /**
     *  代理商pc交易查询二级总数
     * @param req
     * @return
     */
    int listCount(OrderTradeRequest req);
    /**
     *  代理商pc交易查询二级总数
     * @param req
     * @return
     */
    int listHsyCount(OrderTradeRequest req);

    /**
     * 代理商pc交易查询一级总数
     * @param req
     * @return
     */
    int listFirstCount(OrderTradeRequest req);
    /**
     * 代理商pc交易查询一级总数
     * @param req
     * @return
     */
    int listHsyFirstCount(OrderTradeRequest req);

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

    /**
     * 保存回调信息
     * @param geTuiResponse
     */
    void save(GeTuiResponse geTuiResponse);

    /**
     * 统计金额
     *
     * @param accountId
     * @param appId
     * @param payChannelSigns
     * @param sDate
     * @param eDate
     * @return
     */
    AppStatisticsOrder statisticsByParam(@Param("accountId") long accountId, @Param("appId") String appId, @Param("payChannelSigns") ArrayList<Integer> payChannelSigns, @Param("sDate") String sDate, @Param("eDate") String eDate);

    /**
     * 查询退款单号
     * @param orderNo
     * @return
     */
    String getRefundOrder(@Param("orderNo") String orderNo);

    /**
     * 根据订单号查分润列表
     * @param orderNo
     * @return
     */
    List<ProfitRefundResponse> getProfitRefundList(@Param("orderNo") String orderNo);

    /**
     * 分润退款记录
     * @param orderNo
     * @return
     */
    List<SplitAccountRefundRecord> splitAccountRefundList(String orderNo);

    /**
     * 查询hss订单（订单查询）
     * @param req
     * @return
     */
    List<QueryOrderResponse> queryOrderList(QueryOrderRequest req);

    /**
     * 查询hss订单总数（订单查询）
     * @param req
     * @return
     */
    int queryOrderListCount(QueryOrderRequest req);

    /**
     * 按业务订单号查询个数
     *
     * @param businessOrderNo
     * @param payee
     * @return
     */
    int selectCountByBusinessOrderAndPayee(@Param("businessOrderNo") String businessOrderNo, @Param("payee") long payee);

    /**
     * 统计指定结算日期的交易
     *
     * @param settleDate
     * @return
     */
    List<OrderBalanceStatistics> statisticsPendingBalanceOrder(@Param("settleDate") Date settleDate, @Param("upperChannel") int upperChannel, @Param("accountIdList") List<Long> accountIdList, @Param("accountId") long accountId);

    /**
     * 统计订单金额
     * @param req
     * @return
     */
    String getOrderCount(QueryOrderRequest req);

    /**
     * 统计订单金额
     * @param req
     * @return
     */
    String getOrderCount1(QueryOrderRequest req);

    /**
     * 统计金额
     * @param req
     * @return
     */
    String getAmountCount(OrderTradeRequest req);

    /**
     * 统计金额
     * @param req
     * @return
     */
    String getAmountCount1(OrderTradeRequest req);

    /**
     * 统计手续费
     * @param req
     * @return
     */
    String getPoundageCount(OrderTradeRequest req);

    /**
     * 统计手续费
     * @param req
     * @return
     */
    String getPoundageCount1(OrderTradeRequest req);

    /**
     * 业绩统计
     * @param req
     * @return
     */
    List<AchievementStatisticsResponse> getAchievement(QueryOrderRequest req);

    /**
     * 业绩统计总数
     * @param req
     * @return
     */
    int getAchievementCount(QueryOrderRequest req);

    /**
     * 导出业绩
     * @param req
     * @return
     */
    List<AchievementStatisticsResponse> downloadeYJ(QueryOrderRequest req);

    /**
     * 代理商业绩统计
     * @param req
     * @return
     */
    List<AchievementStatisticsResponse> getDaiLiAchievement(QueryOrderRequest req);

    /**
     * 代理商业绩统计总数
     * @param req
     * @return
     */
    int getDaiLiAchievementCount(QueryOrderRequest req);

    /**
     * 代理商业绩统计总数
     * @param req
     * @return
     */
    List<AchievementStatisticsResponse> downloadeDaiLi(QueryOrderRequest req);

    List<MerchantTradeResponse> getTrade1(OrderTradeRequest req);

    List<MerchantTradeResponse> getHsyTrade1(OrderTradeRequest req);

    List<MerchantTradeResponse> getTradeFirst1(OrderTradeRequest req);

    List<MerchantTradeResponse> getHsyTradeFirst1(OrderTradeRequest req);

    /**
     * hsy交易详情
     * @param orderNo
     * @return
     */
    MerchantTradeResponse selectOrderListHsy(String orderNo);

    /**
     * 按结算单查询个数
     *
     * @param settlementRecordId
     * @return
     */
    int selectCountBySettlementReocrdId(@Param("settlementRecordId") long settlementRecordId);

    /**
     * 按结算单查询列表
     *
     * @param settlementRecordId
     * @param offset
     * @param count
     * @return
     */
    List<Order> selectBySettlementReocrdId(@Param("settlementRecordId") long settlementRecordId, @Param("offset") int offset, @Param("count") int count);


    /**
     * Test
     *
     * @param orderNos
     * @param id
     * @return
     */
    int updateSettlementRecordIdByOrderNos(@Param("orderNos") List<String> orderNos, @Param("id") long id);
    /**
     * 分公司hss交易
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getBranch(OrderTradeRequest req);

    /**
     * 分公司hsy交易
     * @param req
     * @return
     */
    List<MerchantTradeResponse> getBranchHsy(OrderTradeRequest req);

    int getBranchCount(OrderTradeRequest req);

    List<MerchantTradeResponse> downLoadHssBranch(OrderTradeRequest req);

    String getAmountCountBranch(OrderTradeRequest req);

    String getAmountCountBranch1(OrderTradeRequest req);

    String getAmountCountHss(OrderTradeRequest req);

    String getAmountCountsHsy(OrderTradeRequest req);

    String getAmountCountHss1(OrderTradeRequest req);

    String getAmountCountsHsy1(OrderTradeRequest req);

    int markOrder2SettlementIngBySettleChannel(@Param("settleDate") Date settleDate, @Param("accountId") long accountId,
                                               @Param("settlementRecordId") long settlementRecordId, @Param("settleStatus") int settleStatus,
                                               @Param("upperChannel") int upperChannel, @Param("settleChannel") int settleChannel);

    List<Order> selectOrderListByCount(@Param("accountId") long accountId, @Param("count") int count,
                                       @Param("status") int status, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    Order selectOrderBySn(String sn);

    void markOrder2SettleFail(@Param("settlementRecordId") long settlementRecordId, @Param("settleStatus") int settleStatus, @Param("oriSettleStatus") int oriSettleStatus);

    long selectWithdrawOrderCountByParam(long accountId);

    List<Order> selectWithdrawOrdersByParam(@Param("accountId") long accountId, @Param("firstIndex") int firstIndex, @Param("pageSize") int pageSize);

    List<Order> selectWithdrawingOrderByAccountId(@Param("accountId") long accountId, @Param("begin") String begin, @Param("end") String end);

    void updateOrdersBySns(@Param("sns") List<String> sns, @Param("settleStatus") int settleStatus, @Param("settleId") long settleId);

    void updateOrdersBySns2Withdraw(@Param("sns") List<String> sns, @Param("status") int status);


    List<Order> selectWithdrawingOrderByBefore(Date date);
}
