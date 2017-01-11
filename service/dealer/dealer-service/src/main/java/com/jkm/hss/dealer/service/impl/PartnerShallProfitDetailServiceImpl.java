package com.jkm.hss.dealer.service.impl;

import com.jkm.hss.dealer.dao.PartnerShallProfitDetailDao;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import com.jkm.hss.dealer.service.PartnerShallProfitDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
