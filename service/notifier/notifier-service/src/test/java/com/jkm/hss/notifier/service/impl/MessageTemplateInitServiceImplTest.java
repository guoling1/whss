package com.jkm.hss.notifier.service.impl;

import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.TestBase;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.MessageTemplateInitService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by yulong.zhang on 2016/11/17.
 */
public class MessageTemplateInitServiceImplTest extends TestBase {

    @Autowired
    private MessageTemplateInitService messageTemplateInitService;

    @Autowired
    private SendMessageService sendMessageService;

    @Test
    public void initTemplate() {
        this.messageTemplateInitService.initTemplate();
    }

    @Test
    public void sendMessage() {
        final HashMap<String, String> data = new HashMap<>();
        data.put("code", "123987");
        data.put("amount", "321");
        final SendMessageParams params = SendMessageParams.builder()
                .uid("321")
                .mobile("18640426296")
                .noticeType(EnumNoticeType.PAYMENT_CODE)
                .userType(EnumUserType.FOREGROUND_USER)
                .data(data)
                .build();
        this.sendMessageService.sendMessage(params);
    }
}
