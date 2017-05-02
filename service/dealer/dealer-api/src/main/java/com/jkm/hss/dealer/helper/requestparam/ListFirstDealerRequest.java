package com.jkm.hss.dealer.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import com.jkm.hss.dealer.enums.EnumOemType;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/7.
 *
 * 代理商列表  入参
 */
@Data
public class ListFirstDealerRequest extends PageQueryParams {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 代理商名字
     */
    private String name;

    /**
     * 代理商编号
     */
    private String markCode;

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
     * 分公司标示 0代理商 1分公司
     *{@link EnumOemType}
     */
    private Integer oemType;

    private int offset;

    private int count;
}
