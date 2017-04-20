package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/27.
 */
@Repository
public interface MerchantInfoQueryDao {



    /**
     * 查询所有商户
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoRequest req);

    /**
     * 查询总数
     * @return
     */
    int getCount(MerchantInfoRequest req);

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
     * 下载
     * @param req
     * @return
     */
    List<MerchantInfoResponse> downloade(MerchantInfoRequest req);

    /**
     * 查询状态非3或6
     * @param req
     * @return
     */
    List<MerchantInfoResponse> getAll1(MerchantInfoRequest req);

    /**
     * 查询状态非3或6总数
     * @param req
     * @return
     */
    int getCount1(MerchantInfoRequest req);

    /**
     * 下载
     * @param req
     * @return
     */
    List<MerchantInfoResponse> downloade1(MerchantInfoRequest req);
}
