package com.jkm.hss.merchant.helper.request;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/9.
 */
@Data
public class AppliationListRequest extends PageQueryParams {
    private String mobile;
    private String markCode;
    private int offset;
    private int count;
}
