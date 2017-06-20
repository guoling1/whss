package com.jkm.hsy.user.help.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;


@Data
public class NetLogRequest extends PageQueryParams {
    private long userId;
    private int offset;
    private int count;
}
