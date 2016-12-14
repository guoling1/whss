package com.jkm.base.common.spring.aliyun.service;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2016/12/9.
 *
 * 校验身份4要素
 */
public interface VerifyID4ElementService {

    /**
     * 校验身份4要素
     *
     * @param bankcard  银行卡
     * @param idCard    身份证
     * @param mobile    银行预留手机号
     * @param realName  真实姓名
     * @return
     */
    Pair<Integer, String> verify(String bankcard, String idCard, String mobile, String realName);
}
