package com.jkm.hss.dealer.helper.response;

import com.jkm.hss.dealer.enums.EnumSignCode;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/5/2.
 */
@Data
public class OemDetailResponse {
    /**
     * 主键id
     */
    protected long id;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 微信公众号名称
     */
    private String wechatName;
    /**
     * 微信号
     */
    private String wechatCode;
    /**
     * appid
     */
    private String appId;
    /**
     * 秘钥
     */
    private String appSecret;
    /**
     * logo
     */
    private String logo;
    /**
     * 公众号二维码
     */
    private String qrCode;
    /**
     * 消息模板集合
     */
    private List<TemplateInfo> TemplateInfos;
    @Data
    public static class TemplateInfo{
        /**
         * 模板标示
         * {@link EnumSignCode}
         */
        private int signCode;
        /**
         * 模板id
         */
        private String templateId;
        /**
         * 模板名称
         */
        private String templateName;
    }
}
