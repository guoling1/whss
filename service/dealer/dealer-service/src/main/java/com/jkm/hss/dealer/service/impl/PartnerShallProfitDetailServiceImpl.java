package com.jkm.hss.dealer.service.impl;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.dealer.dao.PartnerShallProfitDetailDao;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import com.jkm.hss.dealer.service.PartnerShallProfitDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by yuxiang on 2017-01-11.
 */
@Service
@Slf4j
public class PartnerShallProfitDetailServiceImpl implements PartnerShallProfitDetailService {

    @Autowired
    private PartnerShallProfitDetailDao partnerShallProfitDetailDao;

    @Override
    public void init(PartnerShallProfitDetail partnerShallProfitDetail) {

        this.partnerShallProfitDetailDao.init(partnerShallProfitDetail);
    }

    @Override
    public PageModel<PartnerShallProfitDetail> getPartnerShallProfitList(long merchantId, long id, int pageSize) {

        final PageModel<PartnerShallProfitDetail> page = new PageModel<>(1, pageSize);

        final long count = partnerShallProfitDetailDao.selectCountByMerchantId(merchantId);
        final List<PartnerShallProfitDetail> detailList = partnerShallProfitDetailDao.selectByMerchantIdAndId(merchantId, id, pageSize);
        if (CollectionUtils.isEmpty(detailList)) {
            page.setRecords(Collections.<PartnerShallProfitDetail>emptyList());
        } else {
            page.setRecords(detailList);
        }
        page.setCount(count);
        return page;
    }

    @Override
    public BigDecimal selectTotalProfitByMerchantId(long merchantId) {

        return  this.partnerShallProfitDetailDao.selectFirstTotalProfitByMerchantId(merchantId).add(
                this.partnerShallProfitDetailDao.selectSecondTotalProfitByMerchantId(merchantId)
        );
    }
}
