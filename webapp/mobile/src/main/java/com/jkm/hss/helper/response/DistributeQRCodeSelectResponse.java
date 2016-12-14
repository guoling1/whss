package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/28.
 */
@Data
public class DistributeQRCodeSelectResponse {

    /**
     * 二级代理名字
     */
    private String secondLevelDealerName;

    /**
     * 开始
     */
    private String startCode;

    /**
     * 结束
     */
    private String endCode;

    /**
     * 个数
     */
    private int count;

    /**
     * 分配日期
     */
    private String distributeDate;

}
