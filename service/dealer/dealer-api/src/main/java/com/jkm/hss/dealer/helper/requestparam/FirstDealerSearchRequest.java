package com.jkm.hss.dealer.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/7.
 *
 * 代理商列表  入参
 */
@Data
public class FirstDealerSearchRequest extends PageQueryParams {
    /**
     * 分公司编码
     */
    private long oemId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 代理商编号
     */
    private String markCode;
    /**
     * 代理商名称
     */
    private String name;

    /**
     * 代理产品
     * {@link com.jkm.hss.admin.enums.EnumQRCodeSysType}
     */
    private String sysType;
    /**
     * 省市编码
     */
    private String districtCode;

    private int offset;

    private int count;
}
