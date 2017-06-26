package com.jkm.base.common.util.email;

import lombok.Data;

import java.util.Properties;

/**
 * Created by yulong.zhang on 2017/6/23.
 */
@Data
public class BaseEmailInfo {
    /**
     * 服务器ip
     */
    private String serverHost;
    /**
     * 端口
     */
    private String serverPort;
    /**
     * 发送者的邮件地址
     */
    private String fromAddress;
    /**
     * 邮件接收者地址
     */
    private String toAddress;
    /**
     * 登录邮件发送服务器的用户名
     */
    private String username;
    /**
     * 登录邮件发送服务器的密码
     */
    private String password;
    /**
     * 是否需要身份验证
     */
    private boolean validate = false;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 附件名称
     */
    private String[] attachFileNames;


    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.serverHost);
        properties.put("mail.smtp.port", this.serverPort);
        properties.put("mail.smtp.auth", validate ? "true" : "false");
        return properties;
    }

}
