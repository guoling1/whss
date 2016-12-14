package com.jkm.hss.notifier.dao;

import com.jkm.hss.notifier.entity.SmsTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglingxin on 15/11/3.
 */
@Repository
public interface MessageTemplateDao {
    /**
     * 添加模板
     *
     * @param messageTemplate
     * @return
     */
    long addTemplate(SmsTemplate messageTemplate);

    /**
     * 清空
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
     * @param messageTemplate
     * @return
     */
    int modifyMessageTemplate(@Param("messageTemplateId") long messageTemplateId,
                              @Param("messageTemplate") String messageTemplate);

    /**
     * 根据消息类型获取消息模板
     *
     * @param typeId
     * @return
     */
    SmsTemplate getTemplateByType(@Param("typeId") int typeId);

    /**
     * 根据模板id获取消息模板
     *
     * @param id
     * @return
     */
    SmsTemplate getMessageTemplateById(@Param("id") long id);

}
