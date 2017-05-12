package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/3.
 *
 * 会员账户
 *
 * tb_member_account
 */
@Data
public class MemberAccount extends BaseEntity {

    /**
     * 充值总金额
     */
    private BigDecimal rechargeTotalAmount;
    /**
     * 剩余可用金额
     */
    private BigDecimal available;
    /**
     * 累计消费总金额
     */
    private BigDecimal consumeTotalAmount;
}
