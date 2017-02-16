package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class DistributeQrCodeRequest  extends PageQueryParams {
    /**
     * 代理商编号
     */
    private String markCode;
    /**
     * 代理商名称
     */
    private String name;
}
