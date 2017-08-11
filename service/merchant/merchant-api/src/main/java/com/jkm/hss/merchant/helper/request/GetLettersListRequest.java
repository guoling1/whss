package com.jkm.hss.merchant.helper.request;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Data
public class GetLettersListRequest extends PageQueryParams {
    private String startTime;
    private String endTime;
    private int offset;
    private int count;
}
