package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/5/2.
 */
@Data
public class OemInfo extends BaseEntity {
    /**
     * 分公司编码
     */
    private long dealerId;
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
     * 分公司机构号
     */
    private String oemNo;

}
