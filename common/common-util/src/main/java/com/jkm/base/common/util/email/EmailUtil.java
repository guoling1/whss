package com.jkm.base.common.util.email;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/6/23.
 */
@Slf4j
@UtilityClass
public final class EmailUtil {

    /**
     * 发送邮件
     *
     * @param baseEmailInfo
     */
    public static void sendEmail(final BaseEmailInfo baseEmailInfo) {
        final Session session = Session.getDefaultInstance(baseEmailInfo.getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(baseEmailInfo.getUsername(), baseEmailInfo.getPassword());
            }
        });
        final Message message = new MimeMessage(session);

        try {
            final Address from = new InternetAddress(baseEmailInfo.getFromAddress());//创建邮件发送者地址
            message.setFrom(from);//设置邮件消息的发送者
            final Address to = new InternetAddress(baseEmailInfo.getToAddress());//创建邮件的接收者地址
            message.setRecipient(Message.RecipientType.TO, to);//设置邮件消息的接收者
            message.setSubject(baseEmailInfo.getSubject());//设置邮件消息的主题
            message.setSentDate(new Date());//设置邮件消息发送的时间
            //mailMessage.setText(mailInfo.getContent());//设置邮件消息的主要内容

            //MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象
            final Multipart mainPart = new MimeMultipart();
            final MimeBodyPart messageBodyPart = new MimeBodyPart();//创建一个包含附件内容的MimeBodyPart
            messageBodyPart.setContent(baseEmailInfo.getContent(),"text/html; charset=utf-8");
            mainPart.addBodyPart(messageBodyPart);
            //存在附件
            final String[] filePaths = baseEmailInfo.getAttachFileNames();
            if (filePaths != null && filePaths.length > 0) {
                for(String filePath:filePaths){
                    File file = new File(filePath);
                    if(file.exists()){//附件存在磁盘中
                        FileDataSource fds = new FileDataSource(file);//得到数据源
                        messageBodyPart.setDataHandler(new DataHandler(fds));//得到附件本身并至入BodyPart
                        messageBodyPart.setFileName(file.getName());//得到文件名同样至入BodyPart
                        mainPart.addBodyPart(messageBodyPart);
                    }
                }
            }
            //将MimeMultipart对象设置为邮件内容
            message.setContent(mainPart);
            Transport.send(message);//发送邮件
        } catch (final Throwable e) {
            log.error("发送邮件失败", e);
        }

    }
}
