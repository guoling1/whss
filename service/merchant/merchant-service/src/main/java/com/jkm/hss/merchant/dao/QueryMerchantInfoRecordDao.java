package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.LogResponse;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2016/12/2.
 */
@Repository
public interface QueryMerchantInfoRecordDao {

    /**
     * 查询审核详情
     * @param merchantInfo
     * @return
     */
    List<MerchantInfoResponse> getAll(MerchantInfoResponse merchantInfo);

    List<MerchantInfoResponse> getLevel(@Param("dealerId") long dealerId);

    List<MerchantInfoResponse> getResults(@Param("level") int level,@Param("dealerId") long dealerId);

    List<MerchantInfoResponse> getFirstLevel(@Param("firstLevelDealerId") long firstLevelDealerId);

    /**
     * 审核记录
     * @param merchantInfo
     * @return
     */
    List<LogResponse> getLog(MerchantInfoResponse merchantInfo);
}
