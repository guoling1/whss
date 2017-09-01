package com.jkm.hss.notifier.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by konglingxin on 3/11/15.
 * 通知模板
 * tb_message_template
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsTemplate extends BaseEntity {
    /**
     * 通知类型:{@link com.jkm.hss.notifier.enums.EnumNoticeType}
     */
    private int noticeType;

    /**
     * 短信模板
     */
    private String messageTemplate;
    /**
     * o单商编号
     */
    private String oemNo;
    /**
     * 短信通道
     *
     * {@link com.jkm.base.sms.service.constants.EnumSmsSdkChannel}
     */
    private String channel;
    /**
     * 模板编码
     */
    private String templateCode;
    /**
     * 模板签名
     */
    private String signName;
    /**
     * 参数
     */
    private String templateParam;
    /**
     *
     */
    private String appCode;

    private String appKey;

    private String appSecret;

    /**
     * 是否是缺省通道
     */
    private String isDefault;


    public boolean checkDefault() {
        return "Y".equals(this.isDefault);
    }


    /**
     * 制定参数的构造函数
     *
     * @param smsTemplate
     * @param noticeType
     */
    public SmsTemplate(final String smsTemplate, final EnumNoticeType noticeType) {
        this.messageTemplate = smsTemplate;
        this.noticeType = noticeType.getId();
    }

    /**
     * 初始化模板构造器
     *
     * @param noticeType
     * @param smsTemplate
     */
    public SmsTemplate(final EnumNoticeType noticeType, final String smsTemplate) {
        this.noticeType = noticeType.getId();
        this.messageTemplate = smsTemplate;
    }

    /**
     * 获取通知的枚举类型
     *
     * @return
     */
    public EnumNoticeType getEnumNoticeType() {
        return EnumNoticeType.integer2Enum(getNoticeType());
    }

}
