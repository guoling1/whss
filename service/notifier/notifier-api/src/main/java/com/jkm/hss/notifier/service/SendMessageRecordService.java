package com.jkm.hss.notifier.service;

import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.entity.SendMessageRecord;

import java.util.List;

/**
 * Created by konglingxin on 15/10/19.
 * 短信消息记录
 */
public interface SendMessageRecordService {

    /**
     * 插入短信记录
     *
     * @param sendRecord
     * @return
     */
    long insert(SendMessageRecord sendRecord);

    /**
     * 修改发送记录状态
     *
     * @param result
     * @param status
     */
    void updateStatus(long result, int status);

    /**
     * 根据用户id查询短信发送记录
     *
     * @param uid
     * @return
     */
    List<SendMessageRecord> getMessageRecodeByUid(final long uid, final EnumUserType userType);

    /**
     * 根据手机号和发送类型获取该类型信息发送条数
     *
     * @param mobile
     * @param templateId
     * @return
     */
    int getRecordByMobileAndTemp(String mobile, int templateId);

    /**
     * 根据手机号和发送类型获取最后发送的信息
     *
     * @param mobile
     * @param templateId
     * @return
     */
    SendMessageRecord selectLast(String mobile, int templateId);
}
