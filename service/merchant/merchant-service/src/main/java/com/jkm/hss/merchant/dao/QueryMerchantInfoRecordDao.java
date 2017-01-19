package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.LogResponse;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.entity.ReferralResponse;
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

    /**
     * 审核记录
     * @param merchantInfo
     * @return
     */
    List<LogResponse> getLog(MerchantInfoResponse merchantInfo);

    /**
     * 查询推荐信息
     * @param id
     * @return
     */
    ReferralResponse getRefInformation(@Param("id") long id);
}
