package com.jkm.hsy.user.entity;

import java.math.BigDecimal;

/**
 * Created by Allen on 2017/7/5.
 */
public class AppPolicyMemberStatistic {
    private String year;
    private String month;
    private Integer cardCount;
    private Integer consumeCount;
    private BigDecimal consumeSum;

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    public Integer getConsumeCount() {
        return consumeCount;
    }

    public void setConsumeCount(Integer consumeCount) {
        this.consumeCount = consumeCount;
    }

    public BigDecimal getConsumeSum() {
        return consumeSum;
    }

    public void setConsumeSum(BigDecimal consumeSum) {
        this.consumeSum = consumeSum;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
