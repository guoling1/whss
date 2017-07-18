package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yuxiang on 2017-07-18.
 */
@Data
public class WithdrawOrderResponse {


    private String orderNo;

    private String avaWithdraw;

    private String receiveAmount;

    private String poundage;

    private String beginTime;

    private String endTime;

    private String bankName;

    private String cardNo;

    private String bankPic;

    private int status;

}
