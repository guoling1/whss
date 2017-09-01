package com.jkm.hss.notifier.service.impl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.VelocityStringTemplate;
import com.jkm.base.sms.service.SmsSendMessageService;
import com.jkm.base.sms.service.constants.EnumSmsSdkChannel;
import com.jkm.hss.notifier.dao.MessageTemplateDao;
import com.jkm.hss.notifier.entity.SmsTemplate;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.dao.SendMessageRecordDao;
import com.jkm.hss.notifier.entity.SendMessageRecord;
import com.jkm.hss.notifier.exception.NoticeSendException;
import com.jkm.hss.notifier.helper.SendMessageParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by konglingxin on 15/10/9.
 */
@Slf4j
@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Autowired
    private SmsSendMessageService smsSendMessageService;

    @Autowired
    private SendMessageRecordDao sendMessageRecordDao;

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    /**
     * 发送短信消息
     *
     * @param uid
     * @param mobile
     * @param noticeType
     * @param params
     * @return
     */
        /**
         * {@inheritDoc}
         */
        @Override
        public long sendMessage(final SendMessageParams params) {
            checkSendMessageParams(params);
            final List<SmsTemplate> messageTemplateList = this.messageTemplateDao.getTemplateByType(params.getNoticeType().getId());
            SmsTemplate messageTemplate = null;
            if (StringUtils.isEmpty(params.getOemNo())) {
                for (SmsTemplate template : messageTemplateList) {
                    if (template.checkDefault()) {
                        messageTemplate = template;
                        break;
                    }
                }
            } else {
                for (SmsTemplate template : messageTemplateList) {
                    if (Objects.equal(template.getOemNo(), params.getOemNo())) {
                        messageTemplate = template;
                        break;
                    }
                }
            }
            Preconditions.checkNotNull(messageTemplate, "[%s]消息模板为空", params.getNoticeType().getDesc());
            final EnumSmsSdkChannel smsSdkChannel = EnumSmsSdkChannel.of(messageTemplate.getChannel());
            switch (smsSdkChannel) {
                case DEFAULT:
                    return sendRealMessage(params.getUid(), params.getUserType(),
                            params.getMobile(), params.getData(), messageTemplate);
                case ALIYUN:
                    final String paramContent = VelocityStringTemplate.process(messageTemplate.getTemplateParam(), params.getData());
                    final String resultContent;
                    log.info("oemNo[{}],uid-[{}]发送短信内容[{}]", params.getOemNo(), params.getUid(), paramContent);
                    try {
                        resultContent = this.smsSendMessageService.sendMessageWithAliyun(params.getMobile(), messageTemplate.getTemplateCode(),
                                messageTemplate.getSignName(), paramContent, messageTemplate.getAppCode());
                    } catch (Exception e) {
                        log.error("oemNo[" + params.getOemNo() + "]，uid-[" + params.getUid() + "]-发送短信失败", e);
                        throw new NoticeSendException("短信消息发送失败，失败原因:" + e.getMessage(), e);
                    }

                    final SendMessageRecord sendMessageRecord = recordSendMessage(params.getUid(), params.getUserType(), params.getMobile(), messageTemplate, paramContent, resultContent, 1);
                    return sendMessageRecord.getId();
            }
           return 0;
        }

        private void checkSendMessageParams(final SendMessageParams params) {
            final Pair<Boolean, String> checkResult = params.checkParamsCorrect();
            Preconditions.checkArgument(checkResult.getLeft(), checkResult.getRight());
        }


        /**
         * 调用第三方接口发送短信
         *
         * @param uid
         * @param userType
         *@param mobile
         * @param data
         * @param messageTemplate    @return
         */
        private long sendRealMessage(final String uid,
                                     final EnumUserType userType,
                                     final String mobile,
                                     final Map data,
                                     final SmsTemplate messageTemplate) {

            final String content = VelocityStringTemplate.process(messageTemplate.getMessageTemplate(), data);

            final String sn;
            log.info("uid[" + uid +"]" + "发送短信内容[" + content + "]");
            try {
                sn = this.smsSendMessageService.sendMessage(mobile, content);
            } catch (Exception e) {
                log.error("用户[" + uid + "]发送短信失败", e);
                throw new NoticeSendException("短信消息发送失败，失败原因:" + e.getMessage(), e);
            }

            final SendMessageRecord sendMessageRecord = recordSendMessage(uid, userType, mobile, messageTemplate, content, sn, 1);
            return sendMessageRecord.getId();
        }

        /**
         * 记录发送消息内容
         *
         * @param uid
         * @param userType
         *@param mobile
         * @param messageTemplate
         * @param content
         * @param sn     @return
         */
        private SendMessageRecord recordSendMessage(final String uid,
                                                    final EnumUserType userType,
                                                    final String mobile,
                                                    final SmsTemplate messageTemplate,
                                                    final String content,
                                                    final String sn,
                                                    final int status) {
            final SendMessageRecord sendRecord = new SendMessageRecord();
            sendRecord.setUid(uid);
            sendRecord.setUserType(userType.getId());
            sendRecord.setMobile(mobile);
            sendRecord.setContent(content);
            sendRecord.setMessageTemplateId(messageTemplate.getId());
            sendRecord.setSn(sn);
            sendRecord.setStatus(status);
            sendMessageRecordDao.insert(sendRecord);
            return sendRecord;
        }
}
