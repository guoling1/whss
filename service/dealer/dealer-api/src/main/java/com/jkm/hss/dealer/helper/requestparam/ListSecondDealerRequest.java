package com.jkm.hss.dealer.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/7.
 *
 * 代理商列表  入参
 */
@Data
public class ListSecondDealerRequest extends PageQueryParams {
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
     * 上级代理名称
     */
    private String firstDealerName;

    /**
     * 代理产品
     * {@link com.jkm.hss.admin.enums.EnumQRCodeSysType}
     */
    private String sysType;
    /**
     * 省市编码
     */
    private String districtCode;
    /**
     * 所属分公司
     */
    private String oemName;

    private int offset;

    private int count;
}
