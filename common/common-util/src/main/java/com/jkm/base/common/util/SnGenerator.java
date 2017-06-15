package com.jkm.base.common.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * Created by hutao on 15/6/12.
 */
public final class SnGenerator {
    private SnGenerator() {
    }

    /**
     * 生成订单号
     *
     * @return
     */
    public static String generateOrderNo(final String id) {
        Preconditions.checkState(id.length() <= 8);
        return DateFormatUtil.format(new Date(), "yyyyMMdd") + "00000000".substring(id.length()) + id;
    }

    /**
     * 生成流水号（待结算流水，可用余额流水，分账记录流水）
     *
     * @return
     */
    public static String generateFlowSn() {
        return DateFormatUtil.format(new Date(), "yyyyMMdd") + RandomStringUtils.randomNumeric(8);
    }

    /**
     * 生成结算单号
     *
     * @param settleObject  结算对象
     * @param settleDestination   结算目的地
     * @return
     */
    public static String generateSettlementRecordSn(final int settleObject, final int settleDestination) {
        return settleObject + "" + settleDestination + DateFormatUtil.format(new Date(), "yyyyMMdd") + RandomStringUtils.randomNumeric(8);
    }

    /**
     * 生成订单号
     * JKM + 时间戳＋5位随机数
     *
     * @return
     */
    public static String generate() {
        return DateFormatUtil.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(6);
    }


    /**
     * 生成升级支付单号
     * JKM + 时间戳＋5位随机数
     *
     * @return
     */
    public static String generateReqSn() {
        return DateFormatUtil.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(5);
    }
    /**
     * 生成交易订单号
     *
     * @param tradeType
     * @return
     */
    public static String generateSn(final int tradeType) {
        return tradeType + "0" + DateFormatUtil.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(6);
    }

    /**
     * 生成退款订单号
     *
     * @return
     */
    public static String generateRefundSn() {
        return "40" + DateFormatUtil.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(6);
    }

    /**
     * 生成length位订单号
     * "JKM" + 时间戳＋＊位随机数
     *
     * @return
     */
    public static String generate(final int length) {
        Preconditions.checkArgument(length > 17);
        return "JKM" + DateFormatUtil.format(new Date(), "yyyyMMddHHmmss") +
                RandomStringUtils.randomNumeric(length - 17);
    }

    /**
     * 生成length位订单号
     * 前缀＋时间戳＋*位随机数
     *
     * @return
     */
    public static String generate(final String prefix, final int length) {
        final int minLength = 17 + prefix.length();
        Preconditions.checkArgument(length > minLength);
        return prefix + DateFormatUtil.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(length - minLength);
    }

    /**
     * 合众流水号
     * 时间戳＋3位随机数
     *
     * @return
     */
    public static String generateFusionReqSn() {
        return DateFormatUtil.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(3);
    }
}
