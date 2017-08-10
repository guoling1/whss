package com.jkm.hss.product.servcie;

import com.jkm.hss.product.entity.UpgradePayRecord;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Thinkpad on 2017/1/9.
 */
public interface UpgradePayRecordService {
    /**
     * 初始化
     * @param upgradePayRecord
     */
    int insert(UpgradePayRecord upgradePayRecord);

    /**
     * 根据请求单号查询
     * @param reqSn
     * @return
     */
    UpgradePayRecord selectByReqSn(String reqSn);

    /**
     * 根据请求单号更改订单状态
     * @param reqSn
     * @param payResult
     * @return
     */
    int updatePayResult(String payResult,String reqSn);

    /**
     * 根据merchantId查询
     * @param merchantId
     * @return
     */
    UpgradePayRecord selectByMerchantId(Long merchantId);
}
