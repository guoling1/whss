package com.jkm.base.common.entity;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * Created by yulong.zhang on 2016/11/15.
 */
@Data
public class PageQueryParams {
    @Min(value = 1)
    private int pageNo = 1;
    @Min(value = 1)
    private int pageSize = 10;
}
