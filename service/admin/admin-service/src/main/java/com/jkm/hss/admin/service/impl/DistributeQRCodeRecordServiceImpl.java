package com.jkm.hss.admin.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.admin.dao.DistributeQRCodeRecordDao;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.helper.requestparam.DistributeQrCodeRequest;
import com.jkm.hss.admin.service.DistributeQRCodeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
@Service
public class DistributeQRCodeRecordServiceImpl implements DistributeQRCodeRecordService {

    @Autowired
    private DistributeQRCodeRecordDao distributeQRCodeRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param distributeQRCodeRecord
     */
    @Override
    public void add(final DistributeQRCodeRecord distributeQRCodeRecord) {
        this.distributeQRCodeRecordDao.insert(distributeQRCodeRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<DistributeQRCodeRecord> getById(final long id) {
        return Optional.fromNullable(this.distributeQRCodeRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> getDistributeSelfRecords(final long firstLevelDealerId) {
        return this.distributeQRCodeRecordDao.selectDistributeSelfRecords(firstLevelDealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> getDistributeSecondDealerRecords(final long firstLevelDealerId) {
        return this.distributeQRCodeRecordDao.selectDistributeSecondDealerRecords(firstLevelDealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param secondLevelDealerId
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> getRecordBySecondLevelDealerId(final long dealerId, final long secondLevelDealerId) {
        return this.distributeQRCodeRecordDao.selectRecordBySecondLevelDealerId(dealerId, secondLevelDealerId);
    }

    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @param markCode
     * @param name
     * @return
     */
    @Override
    public int selectDistributeCountByContions(long firstLevelDealerId, String markCode, String name) {
        return this.distributeQRCodeRecordDao.selectDistributeCountByContions(firstLevelDealerId,markCode,name);
    }

    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @param markCode
     * @param name
     * @param offset
     * @param count              @return
     */
    @Override
    public List<DistributeQRCodeRecord> selectDistributeRecordsByContions(long firstLevelDealerId, String markCode, String name, int offset, int count) {
        return this.distributeQRCodeRecordDao.selectDistributeRecordsByContions(firstLevelDealerId,markCode,name,offset,count);
    }

    /**
     * boss后台查询分配二维码记录条数
     *
     * @param distributeQrCodeRequest
     * @return
     */
    @Override
    public int selectDistributeCountByContions(DistributeQrCodeRequest distributeQrCodeRequest) {
        return this.distributeQRCodeRecordDao.selectBossDistributeCountByContions(distributeQrCodeRequest);
    }
    /**
     * boss后台二维码分配记录
     *
     * @param distributeQrCodeRequest
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> selectDistributeRecordsByContions(DistributeQrCodeRequest distributeQrCodeRequest) {
        return this.distributeQRCodeRecordDao.selectBossDistributeRecordsByContions(distributeQrCodeRequest);
    }

    /**
     * boss后台查询分配二维码记录条数
     *
     * @param distributeQrCodeRequest
     * @return
     */
    @Override
    public int selectBossDistributeCountByContionsOfJKM(DistributeQrCodeRequest distributeQrCodeRequest) {
        return this.distributeQRCodeRecordDao.selectBossDistributeCountByContionsOfJKM(distributeQrCodeRequest);
    }

    /**
     * boss后台二维码分配记录
     *
     * @param distributeQrCodeRequest
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> selectBossDistributeRecordsByContionsOfJKM(DistributeQrCodeRequest distributeQrCodeRequest) {
        return this.distributeQRCodeRecordDao.selectBossDistributeRecordsByContionsOfJKM(distributeQrCodeRequest);
    }


}
