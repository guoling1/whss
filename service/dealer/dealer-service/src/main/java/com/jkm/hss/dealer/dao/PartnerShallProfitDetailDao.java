package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2017-01-11.
 */
@Repository
public interface PartnerShallProfitDetailDao {


    void init(PartnerShallProfitDetail partnerShallProfitDetail);

    long selectCountByMerchantId(long merchantId);

    List<PartnerShallProfitDetail> selectByMerchantIdAndId(@Param("merchantId") long merchantId, @Param("id") long id, @Param("pageSize") int pageSize);

    BigDecimal selectFirstTotalProfitByMerchantId(long merchantId);

    BigDecimal selectSecondTotalProfitByMerchantId(long merchantId);
}
