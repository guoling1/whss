package com.jkm.hss.admin.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.entity.CodeQueryResponse;
import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.helper.FirstLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.MyMerchantCount;
import com.jkm.hss.admin.helper.SecondLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.requestparam.MyQrCodeListRequest;
import com.jkm.hss.admin.helper.requestparam.QrCodeListRequest;
import com.jkm.hss.admin.helper.responseparam.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

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
     * 将codeIds表示的二维码为自己名下已分配
     *
     * @param codeIds
     */
    int markAsDistribute(List<Long> codeIds);

    /**
     * 将一级代理商（dealerId）下codeIds表示的码段标记为二级代理商（toDealerId）名下已分配
     *
     * @param codeIds
     * @param toDealerId
     */
    int markAsDistribute2(List<Long> codeIds, long toDealerId);
    /**
     * 初始化商户的二维码（公众号注册）
     *
     * @return
     */
    QRCode initMerchantCode(long merchantId,final long productId,final String sysType);


    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<QRCode> getById(long id);

    /**
     * 按id查询
     *
     * @param ids
     * @return
     */
    List<QRCode> getByIds(List<Long> ids);


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
     * 按code查询当前代理商id，一级代理商id,二级代理商id
     *
     * @param code
     * @return
     */
    Triple<Long, Long, Long> getCurrentAndFirstAndSecondByCode(String code);

    /**
     * 按商户id查询
     *
     * @param merchantId
     * @return
     */
//    Optional<QRCode> getByMerchantId(long merchantId);

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
     * 查询一级代理商下未分配的码段
     *
     * @param dealerId
     * @return
     */
    List<QRCode> getUnDistributeCodeByFirstLevelDealerId(long dealerId);

    /**
     * 查询代理商某一二维码范围下的未分配的二维码个数
     *
     * @param startCode
     * @param endCode
     * @param dealerId
     * @return
     */
    int getUnDistributeCodeCountByDealerIdAndRangeCode(String startCode, String endCode, long dealerId);

    /**
     * 查询代理商某一二维码范围下的未分配的二维码
     *
     * @param dealerId
     * @param startCode
     * @param endCode
     * @return
     */
    List<QRCode> getUnDistributeCodeByDealerIdAndRangeCode(long dealerId, String startCode, String endCode);

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
    String downloadCodeZip(long adminId, int count, final String baseUrl,long productId,String sysType);

    /**
     * 下载二维码Excel
     *
     * @param count
     */
    String downloadExcel(long adminId, int count, final String baseUrl,long productId,String sysType);


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
    List<Long> getUnDistributeCode(String sysType);

    /**
     * 校验此码段范围的二维码是否可以分配
     *
     * @param startCode
     * @param endCode
     * @return
     */
    boolean checkRangeQRCode(String startCode, String endCode);

    void test(String startCode, String endCode);

    /**
     * 从list中，将连续的二维码分为一组，返回所有组
     *
     * @param qrCodes
     * @return
     */
    List<Pair<QRCode, QRCode>> getPairQRCodeList(List<QRCode> qrCodes);

    /**
     * 查询系统中指定范围的可分配的二维码
     *
     * @return
     */
    List<Long> getUnDistributeCodeByRangeCode(String startCode, String endCode, String sysType);

    /**
     * 按码段查询状态
     * @param code
     * @return
     */
    CodeQueryResponse getCode(String code);

    /**
     * 商户id查询其名称
     * @param firstLevelDealerId
     * @return
     */
    CodeQueryResponse getProxyName(long firstLevelDealerId);

    /**
     * 根据firstLevelDealerId
     * 查询一级代理商名称
     * @param secondLevelDealerId
     * @return
     */
    CodeQueryResponse getProxyName1(long secondLevelDealerId);

    /**
     * 根据merchantId
     * 查询商户名称
     * @param merchantId
     * @return
     */
    CodeQueryResponse getMerchantName(long merchantId);

    //  =================以下为好收银新增部分=======================
    /**
     * 按码段查询
     *
     * @param code
     * @return
     */
    Optional<QRCode> getByCode(String code,String sysType);



    /**
     * 绑定店铺个数
     * @param shopId
     * @param sysType
     * @return
     */
    int bindShopCount(long shopId,String sysType);

    /**
     * 二维码列表
     * @param shopId
     * @param sysType
     * @return
     */
    List<QRCodeList> bindShopList(long shopId,String sysType);

    /**
     * 按码段、和产品类型查询某个代理商下的所有二维码
     *
     * @param dealerId
     * @param startCode
     * @param endCode
     * @return
     */
    List<QRCode> getUnDistributeCodeByDealerIdAndRangeCodeAndSysType(long dealerId, String startCode, String endCode,String sysType);

    /**
     * 按产品类型查询某个代理商下的所有二维码
     * @param dealerId
     * @param sysType
     * @return
     */
    List<QRCode> getUnDistributeCodeByDealerIdAndSysType(long dealerId,String sysType);


    /**
     * admin查询所有未分配的二维码个数
     *
     * @return
     */
    List<QRCode> getUnDistributeCodeBySysType(String sysType);
    /**
     * admin查询所有未分配的二维码个数
     *
     * @return
     */
    int getUnDistributeCountBySysType(String sysType);

    /**
     * 根据码段和产品类型
     *
     * @param startCode
     * @param endCode
     * @return
     */
    List<QRCode> getUnDistributeCodeByCodeAndSysType(String startCode, String endCode,String sysType);

    /**
     * 生产二维码
     * @param adminId
     * @param count
     * @param baseUrl
     * @param productId
     * @param sysType
     * @return
     */
    ProductionQrCodeRecord productionQrCode(long adminId, int count, final String baseUrl, long productId, String sysType, int type);

    /**
     * 所有二维码[boss]
     * @param qrCodeListRequest
     * @return
     */
    PageModel<QrCodeListResponse> selectQrCodeList(QrCodeListRequest qrCodeListRequest);
    /**
     * 所有二维码[dealer]
     * @param myQrCodeListRequest
     * @return
     */
    PageModel<MyQrCodeListResponse> selectDealerQrCodeList(MyQrCodeListRequest myQrCodeListRequest);

    /**
     * 未分配个数
     * @param firstLevelDealerId
     * @return
     */
    int getFirstResidueCount(long firstLevelDealerId,String sysType);

    /**
     * 已分配个数
     * @param firstLevelDealerId
     * @return
     */
    int getFirstDistributeCount(long firstLevelDealerId,String sysType);

    /**
     * 未激活个数
     * @param firstLevelDealerId
     * @return
     */
    int getFirstUnActivateCount(long firstLevelDealerId,String sysType);

    /**
     * 已激活个数
     * @param firstLevelDealerId
     * @return
     */
    int getFirstActivateCount(long firstLevelDealerId,String sysType);
    /**
     * 查询二级代理商未激活二维码数
     *
     * @param secondLevelDealerId
     * @return
     */
    int getSecondUnActivateCount(long secondLevelDealerId,String sysType);
    /**
     * 查询二级代理商激活二维码数
     *
     * @param secondLevelDealerId
     * @return
     */
    int getSecondActivateCount(long secondLevelDealerId,String sysType);
}
