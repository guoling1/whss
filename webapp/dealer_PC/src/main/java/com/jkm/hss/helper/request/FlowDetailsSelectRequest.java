package com.jkm.hss.helper.request;

import com.jkm.hss.account.enums.EnumAccountFlowType;
import lombok.Data;

/**
 * Created by yuxiang on 2017-02-14.
 */
@Data
public class FlowDetailsSelectRequest {

    private String flowSn;

    /**
     * 类型
     *
     * {@link EnumAccountFlowType}
     */
    private int type;

    private String beginDate;

    private String endDate;


    private int pageNo;

    private int pageSize;

}
