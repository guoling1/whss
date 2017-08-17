package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Data
public class OpenCardRecord extends BaseEntity{
    /**
     * 绑卡流水
     */
    private String bindCardReqNo;
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 银行卡号
     */
    private String cardNo;
    /**
     * 前台通知
     */
    private String frontUrl;
}
