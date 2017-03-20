package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;

import java.util.List;


/**
 * Created by zhangbin on 2016/11/27.
 */
public interface MerchantInfoQueryService {

    /**
     * 查询所有商户
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoRequest req);

    /**
     * 查询 总数
     * @return
     */
//    int getCount(MerchantInfoRequest req);

    /**
     * 查询待审核商户列表
     * @param req
     * @return
     */
    List<MerchantInfoResponse> getRecord(MerchantInfoRequest req);

    /**
     * 查询待审核总数
     * @return
     */
    int getCountRecord(MerchantInfoRequest req);


    /**
     * 导出所有商户
     * @param req
     * @param baseUrl
     * @return
     */
    String downloadExcel(MerchantInfoRequest req, String baseUrl);
}
