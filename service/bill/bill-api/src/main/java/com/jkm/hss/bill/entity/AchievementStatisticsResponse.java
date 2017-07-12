package com.jkm.hss.bill.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/6/12.
 */
@Data
public class AchievementStatisticsResponse {
    private long id;
    private String realname;//报单员真实姓名
    private String username;//报单员登录
    private Date createTime;
    private String createTimes;//交易日期
    private String tradeCount;//5元以上交易笔数
    private String vaildTradeUserCount;//有效商户数
    private String tradeTotalCount;//当日名下商户交易总笔数
    private String tradeTotalAmount;//当日名下商户交易总额

}
