package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/4/17.
 */
@Data
public class CmbcResponse {
    /**
     * 返回值
     */
    private int code;
    /**
     * 返回描述信息
     */
    private String msg;
}
