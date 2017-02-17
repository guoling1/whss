package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.helper.requestparam.DistributeQrCodeRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
@Repository
public interface DistributeQRCodeRecordDao {

    /**
     * 插入
     *
     * @param distributeQRCodeRecord
     */
    void insert(DistributeQRCodeRecord distributeQRCodeRecord);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    DistributeQRCodeRecord selectById(@Param("id") long id);

    /**
     * 查询分配给自己的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> selectDistributeSelfRecords(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> selectDistributeSecondDealerRecords(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询一级代理商分配给指定代理商的二维码记录
     *
     * @param dealerId
     * @param secondLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> selectRecordBySecondLevelDealerId(@Param("dealerId") long dealerId,
                                                                   @Param("secondLevelDealerId") long secondLevelDealerId);
    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    int selectDistributeCountByContions(@Param("firstLevelDealerId") long firstLevelDealerId,
                                                                 @Param("markCode") String markCode,@Param("name") String name);
    /**
     * 查询分配给二级代理商的二维码记录
     *
     * @param firstLevelDealerId
     * @return
     */
    List<DistributeQRCodeRecord> selectDistributeRecordsByContions(@Param("firstLevelDealerId") long firstLevelDealerId,
                                                                 @Param("markCode") String markCode,@Param("name") String name,
                                                                   @Param("offset") int offset,@Param("count") int count);
    /**
     * boss后台查询分配二维码记录条数
     *
     * @param distributeQrCodeRequest
     * @return
     */
    int selectBossDistributeCountByContions(DistributeQrCodeRequest distributeQrCodeRequest);
    /**
     * boss后台二维码分配记录
     *
     * @param distributeQrCodeRequest
     * @return
     */
    List<DistributeQRCodeRecord> selectBossDistributeRecordsByContions(DistributeQrCodeRequest distributeQrCodeRequest);
    /**
     * boss后台查询分配二维码记录条数
     *
     * @param distributeQrCodeRequest
     * @return
     */
    int selectBossDistributeCountByContionsOfJKM(DistributeQrCodeRequest distributeQrCodeRequest);
    /**
     * boss后台二维码分配记录
     *
     * @param distributeQrCodeRequest
     * @return
     */
    List<DistributeQRCodeRecord> selectBossDistributeRecordsByContionsOfJKM(DistributeQrCodeRequest distributeQrCodeRequest);
}
