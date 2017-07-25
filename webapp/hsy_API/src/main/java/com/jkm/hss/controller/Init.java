package com.jkm.hss.controller;

import com.jkm.hss.admin.service.AdminUserInitService;
import com.jkm.hss.notifier.service.MessageTemplateInitService;
import com.jkm.hsy.user.service.ShopSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by yulong.zhang on 2016/11/16.
 */
@Slf4j
@Component
public class Init {

    @Autowired
    private MessageTemplateInitService messageTemplateInitService;

    @PostConstruct
    public void initSystem() {
        log.info("######################初始化系统--start##########################");
        this.initSmsTemplate();
        log.info("######################初始化系统--end##########################");
    }

    /**
     * 初始化模板
     */
    private void initSmsTemplate() {
        log.info("######################初始化模板--start##########################");
        this.messageTemplateInitService.initTemplate();
        log.info("######################初始化模板--end##########################");
    }
}
