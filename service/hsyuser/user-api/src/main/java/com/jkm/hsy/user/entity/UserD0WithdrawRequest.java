package com.jkm.hsy.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * do提现请求参数
 */
@Data
public class UserD0WithdrawRequest {
    private Long userId;//用户编码
    private Integer isOpenD0;//是否开通d0提现
}
