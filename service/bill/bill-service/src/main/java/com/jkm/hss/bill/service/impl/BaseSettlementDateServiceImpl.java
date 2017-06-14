package com.jkm.hss.bill.service.impl;

import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.helper.HolidaySettlementConstants;
import com.jkm.hss.bill.service.MergeTableSettlementDateService;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumUpperChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/12.
 */
@Slf4j
@Service
public class BaseSettlementDateServiceImpl implements BaseSettlementDateService {

    @Autowired
    private MergeTableSettlementDateService mergeTableSettlementDateService;

    /**
     * {@inheritDoc}
     *
     * @param appId
     * @param tradeDate
     * @param upperChannel
     * @return
     */
    @Override
    public Date getSettlementDate(final String appId, final Date tradeDate, final String settleType, final EnumUpperChannel upperChannel) {
        if (EnumAppType.HSY.getId().equals(appId)) {
            return this.getHsySettleDate(tradeDate, upperChannel);
        } else if (EnumAppType.HSS.getId().equals(appId)) {
            return this.getHssSettleDate(tradeDate, settleType, upperChannel);
        }
        throw new RuntimeException("获取结算时间异常，错误的业务线");
    }

    /**
     *  获取结算日期
     *
     * @param tradeDate
     * @param upperChannel
     * @return
     */
    private Date getHsySettleDate(final Date tradeDate, final EnumUpperChannel upperChannel) {
        switch (upperChannel) {
            case SYJ:
                return this.getShouYinJiaPaySettleDate(tradeDate);
        }
        log.error("can not be here");
        return new Date();
    }

    /**
     *  获取结算日期
     *
     * @param tradeDate
     * @param upperChannel
     * @return
     */
    private Date getHssSettleDate(final Date tradeDate, final String settleType, final EnumUpperChannel upperChannel) {
        if (EnumBalanceTimeType.D0.getType().equals(settleType)) {
            return tradeDate;
        } else if (EnumBalanceTimeType.T1.getType().equals(settleType)) {
            switch (upperChannel) {
                case EASY_LINK:
                    return this.getEasyLinkUnionPaySettleDate(tradeDate);
                case MOBAO:
                    return this.getMoBaoUnionPaySettleDate(tradeDate);
            }
        }
        log.error("can not be here");
        return new Date();
    }

    /**
     * 获取收银家结算时间
     *
     * @param tradeDate
     * @return
     */
    private Date getShouYinJiaPaySettleDate(Date tradeDate) {
        if (HolidaySettlementConstants.HOLIDAY_OPEN) {
            final Date settlementDate = this.mergeTableSettlementDateService.getSettlementDate(tradeDate, EnumUpperChannel.SYJ.getId());
            if (null != settlementDate) {
                return settlementDate;
            }
        }
        return DateTimeUtil.generateT1SettleDate(tradeDate);
    }

    /**
     * 获取魔宝快捷结算时间
     *
     * 82560000表示22:56:00
     *
     * @param tradeDate
     * @return
     */
    private Date getMoBaoUnionPaySettleDate(Date tradeDate) {
        final int millisOfDay = new DateTime(tradeDate).getMillisOfDay();
        if (millisOfDay > 82560000) {//当作第二个工作日的交易
            tradeDate = new DateTime(tradeDate).plusDays(1).toDate();
        }
        if (HolidaySettlementConstants.HOLIDAY_OPEN) {
            final Date settlementDate = this.mergeTableSettlementDateService.getSettlementDate(tradeDate, EnumUpperChannel.MOBAO.getId());
            if (null != settlementDate) {
                return settlementDate;
            }
        }
        return DateTimeUtil.generateT1SettleDate(tradeDate);
    }

    /**
     * 获得易联支付结算时间
     *
     * 82740000表示22:59:00(通道清算时间是23:00:00)
     *
     * @param tradeDate
     * @return
     */
    private Date getEasyLinkUnionPaySettleDate(Date tradeDate) {
        final int millisOfDay = new DateTime(tradeDate).getMillisOfDay();
        if (millisOfDay > 82740000) {//当作第二个工作日的交易
            tradeDate = new DateTime(tradeDate).plusDays(1).toDate();
        }
        if (HolidaySettlementConstants.HOLIDAY_OPEN) {
            final Date settlementDate = this.mergeTableSettlementDateService.getSettlementDate(tradeDate, EnumUpperChannel.EASY_LINK.getId());
            if (null != settlementDate) {
                return settlementDate;
            }
        }
        return DateTimeUtil.generateT1SettleDate(tradeDate);
    }
}
