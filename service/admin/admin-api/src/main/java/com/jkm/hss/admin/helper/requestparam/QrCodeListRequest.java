package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class QrCodeListRequest extends PageQueryParams {
    /**
     * 二维码编号
     */
    private String code;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 一级代理名称
     */
    private String firstDealerName;
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
     *
     */
    private int offset;
    /***
     *
     */
    private int count;
}
