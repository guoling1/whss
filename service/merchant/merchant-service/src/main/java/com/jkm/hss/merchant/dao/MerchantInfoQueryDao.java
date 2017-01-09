package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import org.apache.ibatis.annotations.Param;
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
     * 查询代理商
     * @param firstLevelDealerId
     * @return
     */
    MerchantInfoResponse getProxyName(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 查询待审核总数
     * @return
     */
    int getCountRecord(MerchantInfoRequest req);

    MerchantInfoResponse getProxyName1(long firstLevelDealerId);


}
