package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/17.
 */
@Data
public class ConfirmBindResponse {
    /**
     * 二维码
     */
    private String code;
    /**
     * 店铺名
     */
    private String name;
}
