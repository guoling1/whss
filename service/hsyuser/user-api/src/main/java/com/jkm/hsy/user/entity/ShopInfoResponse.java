package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/7/3.
 */
@Data
public class ShopInfoResponse {
    private int type;//店铺类型
    private String shortName;//店铺简称
    private String districtCode;//所在地
    private String address;//地址
    private String contactCellphone;//联系人手机号
}
