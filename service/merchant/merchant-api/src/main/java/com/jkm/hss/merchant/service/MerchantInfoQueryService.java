package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;

import java.util.List;


/**
 * Created by zhangbin on 2016/11/27.
 */
public interface MerchantInfoQueryService {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Optional<MerchantInfo> selectById(long id);


    /**
     * 根据商户状态查询
     * @param status
     * @return
     */
    Optional<MerchantInfo> selectByStatus(int status);

    /**
     * 根据经销商id查询
     * @param dealerId
     * @return
     */
    Optional<MerchantInfo> selectByDealerId(long dealerId);

    /**
     * 查询所有商户
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfoResponse);


    /**
     * 根据商户名称查询
     * @param pageNo
     * @param pageSize
     * @param merchantName
     * @return
     */
    List<MerchantInfoResponse> selectByName(int pageNo, int pageSize, String merchantName);

    /**
     * 查询总数
     * @return
     */
    List<MerchantInfoResponse> getCount();

    /**
     * 查询待审核商户列表
     * @param merchantInfoResponse
     * @return
     */
    List<MerchantInfoResponse> getRecord(MerchantInfoResponse merchantInfoResponse);

    /**
     * 查询待审核总数
     * @return
     */
    List<MerchantInfoResponse> getCountRecord();

    /**
     * 导出
     * @param merchantInfoResponse
     * @param baseUrl
     * @return
     */
    String downloadExcel(MerchantInfoResponse merchantInfoResponse, String baseUrl);
}
