package com.jkm.hss.admin.service;

import com.google.common.base.Optional;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.helper.FirstLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.MyMerchantCount;
import com.jkm.hss.admin.helper.SecondLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import com.sun.org.apache.bcel.internal.classfile.Code;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
public interface QRCodeService {

    /**
     * 插入
     *
     * @param qrCode
     */
    void add(QRCode qrCode);

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
    int markCodeToDealer(long firstLevelDealerId, List<Long> codeIds);

    /**
     * 将指定范围的二维码标记在代理商名下
     *
     * @param firstLevelDealerId
     * @param startCode
     * @param endCode
     * @return
     */
    int markCodeToDealer(long firstLevelDealerId, String startCode, String endCode);

    /**
     * code标记为已激活
     *
     * @param code
     * @param merchantId
     * @return
     */
    int markAsActivate(String code, long merchantId);

    /**
     * 将一级代理商下未分配的count个码段标记为自己名下已分配
     *
     * @param dealerId
     * @param count
     */
    int markAsDistribute(long dealerId, int count);

    /**
     * 将一级代理商（dealerId）下未分配的count个码段标记为二级代理商（toDealerId）名下已分配
     *
     * @param dealerId
     * @param toDealerId
     * @param count
     */
    int markAsDistribute2(long dealerId, long toDealerId, int count);

    /**
     * 初始化商户的二维码（公众号注册）
     *
     * @return
     */
    QRCode initMerchantCode(long merchantId);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<QRCode> getById(long id);

    /**
     * 按码段查询
     *
     * @param code
     * @return
     */
    Optional<QRCode> getByCode(String code);

    /**
     * 按code查询代理商id
     *
     * @param code
     * @return
     */
    long getDealerIdByCode(String code);

    /**
     * 按商户id查询
     *
     * @param merchantId
     * @return
     */
    Optional<QRCode> getByMerchantId(long merchantId);

    /**
     * 按商户ids查询
     *
     * @param ids
     * @return
     */
    List<QRCode> getByMerchantIds(List<Long> ids);

    /**
     * 查询某一级代理商下的码段
     *
     * @param firstLevelDealerId
     * @return
     */
    List<QRCode> getFirstLevelDealerCodeByDealerId(long firstLevelDealerId, int status);

    /**
     * 查询某二级代理商下的码段
     *
     * @param secondLevelDealerId
     * @return
     */
    List<QRCode> getSecondLevelDealerCodeByDealerId(long secondLevelDealerId, int status);

    /**
     * 查找最新的一个码段
     *
     * @return
     */
    Optional<QRCode> getLatestQRCodeForUpdate();

    /**
     * 查询一级代理商下未分配的码段
     *
     * @param dealerId
     * @return
     */
    int getUnDistributeCodeCountByFirstLevelDealerId(long dealerId);

    /**
     * 查询一级代理商下未分配的count个码段
     *
     * @param dealerId
     * @param count
     * @return
     */
    List<QRCode> getUnDistributeCodeByFirstLevelDealerId(long dealerId, int count);

    /**
     * 查询一级代理分配给二级代理的二维码数
     *
     * @param dealerId
     * @param secondLevelDealerIds
     * @return
     */
    List<DistributeCodeCount> getDistributeCodeCount(long dealerId, List<Long> secondLevelDealerIds);

    /**
     * 查询一级代理分配给二级代理的二维码已经激活数
     *
     * @param dealerId
     * @param secondLevelDealerIds
     * @return
     */
    List<ActiveCodeCount> getActiveCodeCount(long dealerId, List<Long> secondLevelDealerIds);

    /**
     * 查询一级代理商二维码分配情况信息
     *
     * @return
     */
    Optional<FirstLevelDealerCodeInfo> getFirstLevelDealerCodeInfos(long firstLevelDealerId);

    /**
     * 查询二级代理商二维码分配情况信息
     *
     * @param secondLevelDealerId
     * @return
     */
    Optional<SecondLevelDealerCodeInfo> getSecondLevelDealerCodeInfos(long secondLevelDealerId);

    /**
     * 一级代理商给二级代理分配的二维码数
     *
     * @param firstLevelDealerId
     * @return
     */
    int getDistributeToSecondDealerCodeCount(long firstLevelDealerId);

    /**
     * 我发展的店铺统计
     *
     * @param dealerId   代理商id
     * @param level   级别
     * @return
     */
    Optional<MyMerchantCount> getMyMerchantCount(long dealerId, int level);

    /**
     * 下载二维码zip
     *
     * @param count
     */
    String downloadCodeZip(long adminId, int count, final String baseUrl);

    /**
     * 下载二维码Excel
     *
     * @param count
     */
    String downloadExcel(long adminId, int count, final String baseUrl);


    /**
     * 按code下载
     *
     * @param adminId
     * @param startId
     * @param endId
     * @param baseUrl
     * @return
     */
    String downloadExcelByCode(int adminId, long startId, long endId, String baseUrl);

    /**
     * 查询所有未分配的二维码个数
     *
     * @return
     */
    List<Long> getUnDistributeCode();

    /**
     * 校验此码段范围的二维码是否可以分配
     *
     * @param startCode
     * @param endCode
     * @return
     */
    boolean checkRangeQRCode(String startCode, String endCode);

    void test(String startCode, String endCode);
}
