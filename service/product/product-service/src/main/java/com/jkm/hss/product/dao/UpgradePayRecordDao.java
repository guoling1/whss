package com.jkm.hss.product.dao;

import com.jkm.hss.product.entity.UpgradePayRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Thinkpad on 2017/1/9.
 */
@Repository
public interface UpgradePayRecordDao {
    /**
     * 初始化
     * @param upgradePayRecord
     */
    int insert(UpgradePayRecord upgradePayRecord);

    /**
     * 根据编码查询
     * @param reqSn
     * @return
     */
    UpgradePayRecord selectByReqSn(@Param("reqSn") String reqSn);
    /**
     * 根据请求单号更改订单状态
     * @param reqSn
     * @return
     */
    int updatePayResult(@Param("payResult") String payResult,@Param("reqSn") String reqSn);
}
