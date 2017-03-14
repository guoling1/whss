package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import com.jkm.hss.admin.enums.EnumQRCodeActivateStatus;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class MyQrCodeListRequest extends PageQueryParams {
    /**
     * 代理商编码
     */
    private long dealerId;
    /**
     * 二维码编号
     */
    private String code;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     *二级代理名称
     */
    private String secondDealerName;
    /**
     * 项目类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeSysType}
     */
    private String sysType;
    /**
     * 激活状态
     *
     * 0.全部 1未激活 2已激活
     */
    private int activateStatus;
    /**
     * 商户状态
     *
     */
    private int merchantStatus;
    /**
     *
     */
    private int offset;
    /***
     *
     */
    private int count;
}
