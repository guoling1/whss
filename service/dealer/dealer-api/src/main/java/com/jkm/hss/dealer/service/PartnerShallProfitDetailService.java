package com.jkm.hss.dealer.service;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2017-01-11.
 */
public interface PartnerShallProfitDetailService {


    void init(PartnerShallProfitDetail partnerShallProfitDetail);

    PageModel<PartnerShallProfitDetail> getPartnerShallProfitList(long merchantId, long id, int pageSize);

    BigDecimal selectTotalProfitByMerchantId(long merchantId);
}
