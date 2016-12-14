package com.jkm.hss.merchant.dao;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.OrderRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Repository
public interface TransactionRecordDao {

    /**
     * 查询所有
     * @param orderRecord
     * @return
     */
    Optional<OrderRecord> selectAll(OrderRecord orderRecord);

    /**
     * 按照交易时间查询
     * @param createTime
     * @return
     */
    Optional<OrderRecord> selectByTime(@Param("createTime") Date createTime);

    /**
     * 按照交易金额查询
     * @param totalFee
     * @return
     */
    Optional<OrderRecord> selectByMoney(@Param("totalFee") BigDecimal totalFee);

    /**
     * 按照设备编号查询
     * @param createTime
     * @return
     */
//    Optional<OrderRecord> selectByNo(Date createTime);

    /**
     * 按照订单状态查询
     * @param payResult
     * @return
     */
    Optional<OrderRecord> selectByStatus(@Param("payResult") String payResult);

    /**
     * 按照商户编号查询
     * @param merchantId
     * @return
     */
    Optional<OrderRecord> selectByMerchantNo(@Param("merchantId") long merchantId);

    /**
     * 按照商户名称查询
     * @param subName
     * @return
     */
    Optional<OrderRecord> selectByMerchantName(@Param("subName") String subName);

    /**
     * 按照商户手机号查询
     * @param createTime
     * @return
     */
//    Optional<OrderRecord> selectByMobileNo(Date createTime);

    /**
     * 按照结算状态查询
     * @param settleStatus
     * @return
     */
    Optional<OrderRecord> selectBySettleStatus(@Param("settleStatus") int settleStatus);


    /**
     * 按照业务类型查询
     * @param payChannel
     * @return
     */
    Optional<OrderRecord> selectByPayChannel(@Param("payChannel") Integer payChannel);
}
