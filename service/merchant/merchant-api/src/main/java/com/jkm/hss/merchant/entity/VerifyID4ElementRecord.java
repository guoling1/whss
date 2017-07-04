package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/9.
 *
 * 校验身份4要素 记录
 *
 * tb_verify_id_4element_record
 *
 * {@link com.jkm.hss.merchant.enums.EnumVerifyID4ElementRecordStatus}
 */
@Data
public class VerifyID4ElementRecord extends BaseEntity {

    /**
     * 最多验证十次
     */
    public static final int DEFAULT_CAN_RETRY_COUNT = 10;

    /**
     * 用户对应手机号
     */
    private String mobile;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 银行预留手机号
     */
    private String bankReserveMobile;

    /**
     * 校验次数
     */
    private int verifyCount;

    /**
     * 备注
     */
    private String remark;
}
