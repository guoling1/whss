package com.jkm.hss.admin.service;

import com.google.common.base.Optional;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
public interface DistributeQRCodeRecordService {

    /**
     * 插入
     *
     * @param distributeQRCodeRecord
     */
    void add(DistributeQRCodeRecord distributeQRCodeRecord);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<DistributeQRCodeRecord> getById(long id);

    /**
     * 查询分配给自己的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> getDistributeSelfRecords(long firstLevelDealerId);

    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> getDistributeSecondDealerRecords(long firstLevelDealerId);

    /**
     * 查询一级代理商分配给指定代理商的二维码记录
     *
     * @param dealerId
     * @param secondLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> getRecordBySecondLevelDealerId(long dealerId, long secondLevelDealerId);
    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    int selectDistributeCountByContions(long firstLevelDealerId,String markCode,String name);
    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> selectDistributeRecordsByContions(long firstLevelDealerId,String markCode,String name,int offset,int count);
}
