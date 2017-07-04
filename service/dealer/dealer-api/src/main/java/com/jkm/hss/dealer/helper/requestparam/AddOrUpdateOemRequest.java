package com.jkm.hss.dealer.helper.requestparam;

import com.jkm.hss.dealer.enums.EnumSignCode;
import com.jkm.hss.dealer.helper.response.OemDetailResponse;
import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/5/3.
 */
@Data
public class AddOrUpdateOemRequest {
    /**
     * 分公司编码
     */
    protected long dealerId;
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
    private List<OemDetailResponse.TemplateInfo> TemplateInfos;
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
