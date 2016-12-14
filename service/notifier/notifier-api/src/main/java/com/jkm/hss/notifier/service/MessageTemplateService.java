package com.jkm.hss.notifier.service;


import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.entity.SmsTemplate;

import java.util.List;

/**
 * Created by konglingxin on 15/10/19.
 * 通知模板service
 */
public interface MessageTemplateService {
    /**
     * 添加模板
     *
     * @param messageTemplate
     * @return
     */
    long addTemplate(SmsTemplate messageTemplate);

    /**
     * 清空模板
     *
     * @return
     */
    int clearAll();

    /**
     * 获取通知模板
     *
     * @return
     */
    List<SmsTemplate> getMessageTemplate();


    /**
     * 修改短信模板
     *
     * @param messageTemplateId
     * @param smsTemplate
     * @return
     */
    int modifyMessageTemplate(long messageTemplateId, String smsTemplate);

    /**
     * 根据消息类型获取消息模板
     *
     * @param type
     * @return
     */
    SmsTemplate getTemplateByType(EnumNoticeType type);

    /**
     * 根据模板id获取消息模板
     *
     * @param id
     * @return
     */
    SmsTemplate getMessageTemplateById(long id);

}
