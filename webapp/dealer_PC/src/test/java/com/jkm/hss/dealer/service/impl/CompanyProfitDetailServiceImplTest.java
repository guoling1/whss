package com.jkm.hss.dealer.service.impl;

import com.jkm.hss.dealer.entity.CompanyProfitDetail;
import com.jkm.hss.dealer.enums.EnumProfitType;
import com.jkm.hss.dealer.service.CompanyProfitDetailService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-12-05.
 */
public class CompanyProfitDetailServiceImplTest extends TestCase {

    @Autowired
    private CompanyProfitDetailService companyProfitDetailService;

    @Test
    public void testAdd() throws Exception {
        final CompanyProfitDetail companyProfitDetail = new CompanyProfitDetail();
        companyProfitDetail.setMerchantId(12);
        companyProfitDetail.setPaymentSn("12");
        companyProfitDetail.setTotalFee(new BigDecimal(12));
        companyProfitDetail.setWaitShallAmount(new BigDecimal(12));
        companyProfitDetail.setProfitType(EnumProfitType.WITHDRAW.getId());
        companyProfitDetail.setProductShallAmount(new BigDecimal(12));
        companyProfitDetail.setChannelShallAmount(new BigDecimal(12));
        companyProfitDetailService.add(companyProfitDetail);
        System.out.print("12");
    }


    public void test(){
        final CompanyProfitDetailServiceImpl companyProfitDetailService = new CompanyProfitDetailServiceImpl();
        final CompanyProfitDetail companyProfitDetail = new CompanyProfitDetail();
        companyProfitDetail.setMerchantId(12);
        companyProfitDetail.setPaymentSn("12");
        companyProfitDetail.setTotalFee(new BigDecimal(12));
        companyProfitDetail.setWaitShallAmount(new BigDecimal(12));
        companyProfitDetail.setProfitType(EnumProfitType.WITHDRAW.getId());
        companyProfitDetail.setProductShallAmount(new BigDecimal(12));
        companyProfitDetail.setChannelShallAmount(new BigDecimal(12));
        companyProfitDetailService.add(companyProfitDetail);
        System.out.print("12");
    }
}