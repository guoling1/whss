package com.jkm.hss.product.servcie.impl;

import com.jkm.hss.product.dao.UpgradePayRecordDao;
import com.jkm.hss.product.entity.UpgradePayRecord;
import com.jkm.hss.product.servcie.UpgradePayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Thinkpad on 2017/1/9.
 */
@Service
public class UpgradePayRecordServiceImpl implements UpgradePayRecordService {
    @Autowired
    private UpgradePayRecordDao upgradePayRecordDao;
    /**
     * 初始化
     *
     * @param upgradePayRecord
     */
    @Override
    public int insert(UpgradePayRecord upgradePayRecord) {
        return upgradePayRecordDao.insert(upgradePayRecord);
    }

    /**
     * 根据请求单号查询
     *
     * @param reqSn
     * @return
     */
    @Override
    public UpgradePayRecord selectByReqSn(String reqSn) {
        return upgradePayRecordDao.selectByReqSn(reqSn);
    }

    /**
     * 根据请求单号更改订单状态
     *
     * @param reqSn
     * @return
     */
    @Override
    public int updatePayResult(String payResult,String reqSn) {
        return upgradePayRecordDao.updatePayResult(payResult,reqSn);
    }
}
