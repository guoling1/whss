package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/17.
 */
@Data
public class AuditSupportBankRequest {

    private long id;
    /**
     * 1：启用，2：禁用
     */
    private int operation;
}
