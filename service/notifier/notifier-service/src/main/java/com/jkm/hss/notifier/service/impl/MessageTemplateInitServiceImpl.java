package com.jkm.hss.notifier.service.impl;

import com.jkm.hss.notifier.entity.SmsTemplate;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.helper.NotifierConstants;
import com.jkm.hss.notifier.service.MessageTemplateInitService;
import com.jkm.hss.notifier.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by konglingxin on 15/11/24.
 */
@Service
public class MessageTemplateInitServiceImpl implements MessageTemplateInitService {
    @Autowired
    private MessageTemplateService messageTemplateService;

    @Override
    public void initTemplate() {
        final List<SmsTemplate> messageTemplate = this.messageTemplateService.getMessageTemplate();
        if (!CollectionUtils.isEmpty(messageTemplate)) {
            return;
        }

        final String platformName = NotifierConstants.getNotifierConfig().platformName();

        addSmsTemplate(EnumNoticeType.LOGIN_DEALER,
                "好收银登录验证码${code}，5分钟有效，千万不要告诉别人哦【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.REGISTER_MERCHANT,
                "好收银注册验证码${code}，5分钟有效，千万不要告诉别人哦【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.BIND_CARD_DEALER,
                "好收银绑定银行卡验证码${code}，5分钟有效，千万不要告诉别人哦【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.BIND_CARD_MERCHANT,
                "好收银绑定银行卡验证码${code}，5分钟有效，千万不要告诉别人哦【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.WITHDRAW_CODE,
                "好收银提现验证码${code}，5分钟有效，千万不要告诉别人哦【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.WITHDRAW_CODE_DEALER,
                "好收银提现验证码${code}，5分钟有效，千万不要告诉别人哦【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.DUPLICATE_PAY_REFUND,
                "您在${time}发生一笔重复支付，多余款项${amount}已退回至原支付账户，预计1~7个工作日到账，请注意查收【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.REGISTER_MERCHANT,
                "好收银登录验证码${code}，5分钟有效，千万不要告诉别人哦【" + platformName + "】");
    }

    private void addSmsTemplate(final EnumNoticeType noticeType,
                                final String template) {
        final SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setMessageTemplate(template);
        smsTemplate.setNoticeType(noticeType.getId());
        this.messageTemplateService.addTemplate(smsTemplate);

    }
}
