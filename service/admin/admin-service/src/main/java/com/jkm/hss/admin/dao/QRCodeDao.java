package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
@Repository
public interface QRCodeDao {

    /**
     * 插入
     *
     * @param qrCode
     */
    void insert(QRCode qrCode);

    /**
     * 按id更新
     *
     * @param qrCode
     * @return
     */
    int updateById(QRCode qrCode);

    /**
     * 按码段更新
     *
     * @param qrCode
     * @return
     */
    int updateByCode(QRCode qrCode);

    /**
     * 将二维码标记在代理商名下
     *
     * @param firstLevelDealerId
     * @param codeIds
     * @return
     */
    int markCodeToDealer(@Param("firstLevelDealerId") long firstLevelDealerId, @Param("codeIds") List<Long> codeIds);

    /**
     * 将指定的二维码标记在代理商名下
     *
     * @param firstLevelDealerId
     * @param startCode
     * @param endCode
     * @return
     */
    int markCodeToDealerByRange(@Param("firstLevelDealerId") long firstLevelDealerId, @Param("startCode") String startCode, @Param("endCode") String endCode);

    /**
     * 将codeIds表示的二维码标记为已分配
     *
     * @param codeIds
     */
    int markAsDistribute(@Param("codeIds") List<Long> codeIds);

    /**
     * 将一级代理商（dealerId）下codeIds表示的码段标记为二级代理商（toDealerId）名下已分配
     *
     * @param codeIds
     * @param toDealerId 二级代理商id
     */
    int markAsDistribute2(@Param("codeIds") List<Long> codeIds, @Param("toDealerId") long toDealerId);

    /**
     * code标记为已激活
     *
     * @param code
     * @param merchantId
     * @return
     */
    int markAsActivate(@Param("code") String code, @Param("merchantId") long merchantId);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    QRCode selectById(@Param("id") long id);

    /**
     * 按码段查询
     *
     * @param code
     * @return
     */
    QRCode selectByCode(@Param("code") String code);

    /**
     * 按商户id查询
     *
     * @param merchantId
     * @return
     */
    QRCode selectByMerchantId(@Param("merchantId") long merchantId);

    /**
     * 按商户ids查询
     *
     * @param ids
     * @return
     */
    List<QRCode> selectByMerchantIds(@Param("ids") List<Long> ids);

    /**
     * 查询某一级代理商下的码段
     *
     * @param firstLevelDealerId
     * @return
     */
    List<QRCode> selectFirstLevelDealerCodeByDealerId(@Param("firstLevelDealerId") long firstLevelDealerId, @Param("status") int status);

    /**
     * 查询某二级代理商下的码段
     *
     * @param secondLevelDealerId
     * @return
     */
    List<QRCode> selectSecondLevelDealerCodeByDealerId(@Param("secondLevelDealerId") long secondLevelDealerId, @Param("status") int status);

    /**
     *查询最新的一个码段
     *
     * @return
     */
    QRCode selectLatestQRCodeForUpdate();

    /**
     * 查询一级代理商下未分配的码段
     *
     * @param dealerId
     * @return
     */
    int selectUnDistributeCodeCountByFirstLevelDealerId(@Param("dealerId") long dealerId);

    /**
     * 查询一级代理商下未分配的码段
     *
     * @param dealerId
     * @return
     */
    List<QRCode> selectUnDistributeCodeByFirstLevelDealerId(@Param("dealerId") long dealerId);

    /**
     * 查询代理商某一二维码范围下的未分配的二维码个数
     *
     * @param startCode
     * @param endCode
     * @param dealerId
     * @return
     */
    int selectUnDistributeCodeCountByDealerIdAndRangeCode(@Param("startCode") String startCode,
                                                          @Param("endCode") String endCode, @Param("dealerId") long dealerId);

    /**
     * 查询代理商某一二维码范围下的未分配的二维码
     *
     * @param dealerId
     * @param startCode
     * @param endCode
     * @return
     */
    List<QRCode> selectUnDistributeCodeByDealerIdAndRangeCode(@Param("dealerId") long dealerId,
                                                              @Param("startCode") String startCode, @Param("endCode") String endCode);
    /**
     * 查询一级代理分配给二级代理的二维码数
     *
     * @param dealerId
     * @param secondLevelDealerIds
     * @return
     */
    List<DistributeCodeCount> selectDistributeCodeCount(@Param("dealerId") long dealerId, @Param("secondLevelDealerIds") List<Long> secondLevelDealerIds);

    /**
     * 查询一级代理分配给二级代理的二维码已经激活数
     *
     * @param dealerId
     * @param secondLevelDealerIds
     * @return
     */
    List<ActiveCodeCount> selectActiveCodeCount(@Param("dealerId") long dealerId, @Param("secondLevelDealerIds") List<Long> secondLevelDealerIds);

    /**
     * 查询一级代理商名下激活的二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int selectFirstLastDayActivateCount(@Param("firstLevelDealerId") long firstLevelDealerId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询二级代理商名下激活的二维码数
     *
     * @param secondLevelDealerId
     * @param startDate
     * @param endDate
     * @return
     */
    int selectSecondLastDayActivateCount(@Param("secondLevelDealerId") long secondLevelDealerId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    /**
     * 查询一级代理商剩余二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int getFirstResidueCount(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询一级代理商已分配二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int getFirstDistributeCount(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询一级代理商分配给自己的二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int getFirstDistributeToSelfCount(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询一级代理商未激活二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int getFirstUnActivateCount(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询一级代理商已激活二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int getFirstActivateCount(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询二级代理商二维码数
     *
     * @param secondLevelDealerId
     * @return
     */
    int getSecondCodeCount(@Param("secondLevelDealerId") long secondLevelDealerId);

    /**
     * 查询二级代理商未激活二维码数
     *
     * @param secondLevelDealerId
     * @return
     */
    int getSecondUnActivateCount(@Param("secondLevelDealerId") long secondLevelDealerId);

    /**
     * 一级代理商给二级代理分配的二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int selectDistributeToSecondDealerCodeCount(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 代理商本周发展的商户激活数
     *
     * @param dealerId
     * @param level
     * @return
     */
    int selectCurrentWeekActivateCodeCount(@Param("dealerId") long dealerId, @Param("level") int level,
                                           @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    /**
     * 代理商发展的商户激活数
     *
     * @param dealerId
     * @param level
     * @return
     */
    int selectActivateCodeCount(@Param("dealerId") long dealerId, @Param("level") int level);

    /**
     * 查询code
     *
     * @param startCode
     * @param endCode
     * @return
     */
    List<QRCode> getCodeByRange(@Param("startCode") String startCode, @Param("endCode") String endCode);

    /**
     * 查询
     *
     * @param startId
     * @param endId
     * @return
     */
    List<QRCode> selectByIdRange(@Param("startId") long startId, @Param("endId") long endId);

    /**
     * 查询未分配的二维码个数
     *
     * @return
     */
    List<Long> getUnDistributeCode();

    /**
     *
     * @param startCode
     * @param endCode
     */
    void test(@Param("startCode") String startCode, @Param("endCode") String endCode);

    /**
     * 查询当前码段的可用个数
     *
     * @param startCode
     * @param endCode
     * @return
     */
    int selectCountByRange(@Param("startCode") String startCode, @Param("endCode") String endCode);
}
