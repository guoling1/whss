package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class ProductionRequest extends PageQueryParams {
    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeDistributeType}
     */
    private int qrType;
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
