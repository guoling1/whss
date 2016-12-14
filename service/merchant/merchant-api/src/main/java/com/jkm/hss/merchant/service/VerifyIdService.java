package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.VerifyID4ElementRecord;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
public interface VerifyIdService {
    /**
     * 插入
     *
     * @param verifyID4ElementRecord
     */
    void add(VerifyID4ElementRecord verifyID4ElementRecord);

    /**
     * 更新
     *
     * @param verifyID4ElementRecord
     */
    int update(VerifyID4ElementRecord verifyID4ElementRecord);

    /**
     * 将记录标记为无效
     *
     * @param id
     */
    int markToIneffective(long id, String remark);

    /**
     * 将记录标记为无效
     *
     * @param mobile
     */
    int markToIneffective(String mobile);


    /**
     * 按手机号查询有效的校验记录
     *
     * @param mobile
     */
    Optional<VerifyID4ElementRecord> getRecordByMobile(String mobile);


    /**
     * 校验身份4要素
     *
     * @param mobile   注册手机号
     * @param bankcard 银行卡号
     * @param idCard   身份证
     * @param bankReserveMobile  银行预留手机号
     * @param realName  姓名
     * @return
     */
    Pair<Integer, String> verifyID(String mobile, String bankcard, String idCard, String bankReserveMobile, String realName);
}
