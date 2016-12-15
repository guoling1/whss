package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.admin.enums.EnumQRCodeActivateStatus;
import com.jkm.hss.admin.enums.EnumQRCodeDistributionStatus;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * 二维码
 *
 * tb_qr_code
 *
 */
@Data
public class QRCode extends BaseEntity {

    /**
     * 码段（12）
     */
    private String code;

    /**
     * 后台操作人id
     */
    private long adminId;

    /**
     * 一级代理商id
     */
    private long firstLevelDealerId;

    /**
     * 二级代理商id
     */
    private long secondLevelDealerId;

    /**
     * 商户id
     */
    private long merchantId;

    /**
     * 签名时加盐(16)
     */
    private String salt;

    /**
     * 签名（MD5（码段 + MD5(salt)））
     */
    private String sign;

    /**
     * 分配状态
     *
     * {@link EnumQRCodeDistributionStatus}
     */
    private int distributeStatus;

    /**
     * 激活状态
     *
     * {@link EnumQRCodeActivateStatus}
     */
    private int activateStatus;

    /**
     * 二维码生成方式（公众号关注生成， 扫码）
     *
     * {@link com.jkm.hss.admin.enums.EnumQRCodeType}
     */
    private int type;

    /**
     * 获取签名
     *
     * @return
     */
    public String getSignCode() {
        return DigestUtils.sha256Hex(this.code + DigestUtils.sha256Hex(this.salt));
    }

    /**
     * 是否分配
     *
     * @return
     */
    public boolean isDistribute() {
        return this.distributeStatus == EnumQRCodeDistributionStatus.DISTRIBUTION.getCode();
    }

    /**
     * 是否激活
     *
     * @return
     */
    public boolean isActivate() {
        return this.activateStatus == EnumQRCodeActivateStatus.ACTIVATE.getCode();
    }

    public boolean isCorrectSign(final String sign) {
        return this.sign.equals(sign);
    }
}
