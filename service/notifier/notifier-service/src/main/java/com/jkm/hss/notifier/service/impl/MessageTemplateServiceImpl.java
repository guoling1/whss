package com.jkm.hss.notifier.service.impl;

import com.jkm.hss.notifier.dao.MessageTemplateDao;
import com.jkm.hss.notifier.entity.SmsTemplate;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by konglingxin on 15/11/4.
 */


@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {
    @Autowired
    private MessageTemplateDao messageTemplateDao;

    /**
     * {@inheritDoc}
     *
     * @param messageTemplate
     * @return
     */
    @Override
    public long addTemplate(final SmsTemplate messageTemplate) {

        final SmsTemplate template = this.messageTemplateDao.getTemplateByType(messageTemplate.getNoticeType());
        if (template != null) {
            this.messageTemplateDao.modifyMessageTemplate(template.getId(), messageTemplate.getMessageTemplate());
            return template.getId();
        }
        this.messageTemplateDao.addTemplate(messageTemplate);
        return messageTemplate.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int clearAll() {
        return this.messageTemplateDao.clearAll();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<SmsTemplate> getMessageTemplate() {

        return this.messageTemplateDao.getMessageTemplate();
    }

    /**
     * {@inheritDoc}
     *
     * @param messageTemplateId
     * @param smsTemplate
     * @return
     */
    @Override
    public int modifyMessageTemplate(final long messageTemplateId, final String smsTemplate) {

        return this.messageTemplateDao.modifyMessageTemplate(messageTemplateId, smsTemplate);
    }

    /**
     * {@inheritDoc}
     *
     * @param type
     * @return
     */
    @Override
    public SmsTemplate getTemplateByType(final EnumNoticeType type) {

        return this.messageTemplateDao.getTemplateByType(type.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public SmsTemplate getMessageTemplateById(final long id) {
        return this.messageTemplateDao.getMessageTemplateById(id);
    }
}
