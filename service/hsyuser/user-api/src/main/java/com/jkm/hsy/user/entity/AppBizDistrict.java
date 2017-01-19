package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/1/12.
 */
public class AppBizDistrict {
    private String code;
    private String aname;
    private Integer aindex;
    private Integer atype;
    private String parentCode;
    private Integer isLeaf;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public Integer getAindex() {
        return aindex;
    }

    public void setAindex(Integer aindex) {
        this.aindex = aindex;
    }

    public Integer getAtype() {
        return atype;
    }

    public void setAtype(Integer atype) {
        this.atype = atype;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }
}
