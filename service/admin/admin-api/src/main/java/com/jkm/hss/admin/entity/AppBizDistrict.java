package com.jkm.hss.admin.entity;

import lombok.Data;

@Data
public class AppBizDistrict {
    private String code;
    private String aname;
    private Integer aindex;
    private Integer atype;
    private String parentCode;
    private Integer isLeaf;
}
