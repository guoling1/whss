package com.jkm.hss.notifier.dao;

import com.jkm.hss.notifier.entity.VerificationCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by huangwei on 6/11/15.
 */
@Repository
public interface VerificationCodeDao {

    /**
     * 查找最后发送的验证码
     *
     * @param mobile
     * @param type
     * @return
     */
    VerificationCode selectLast(@Param("mobile") String mobile, @Param("type") int type);

    /**
     * 重试次数 + 1
     *
     * @param id
     * @param maxRetryCount
     * @return
     */
    int increaseRetryCount(@Param("id") long id, @Param("maxRetryCount") int maxRetryCount);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 当天的短信验证码发送量
     *
     * @param mobile
     * @param type
     * @return
     */
    int selectTodaySendCount(@Param("mobile") String mobile, @Param("type") int type);

    /**
     * insert
     *
     * @param lastCode
     * @return
     */
    int insert(VerificationCode lastCode);
}
