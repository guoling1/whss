package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yuxiang on 2017-02-15.
 */
@Data
public class FlowDetailsSelectResponse {

    private String flowSn;

    private String createTime;

    private String beforeAmount;

    private String incomeAmount;

    private String outAmount;

    private String afterAmount;

    private String remark;
}
