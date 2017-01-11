package com.jkm.hsy.user.entity;

import java.util.Date;

/**app_biz_shop*/
public class AppBizShop {
    private Long id;
    private String name;//店铺名称
    private String shortName;//店铺简称
    private String industryCode;//行业代码
    private String districtCode;//行政区代码
    private String address;//地址
    private String licenceID;//营业执照id
    private String storefrontID;//店面照片id
    private String counterID;//收银台ID
    private String indoorID;//室内ID
    private Long parentID;//父ID
    private String contactName;//联系人姓名
    private String contactCellphone;//联系人手机号
    private Integer status;//状态：1 正常 99禁用
    private Integer isPublic;//是否对公1是 2否'
    private Date createTime;
    private Date updateTime;

    private Long uid;//用户ID
    private Integer role;//角色 1法人 2店长 3店员  4财务

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getLicenceID() {
        return licenceID;
    }

    public void setLicenceID(String licenceID) {
        this.licenceID = licenceID;
    }

    public String getStorefrontID() {
        return storefrontID;
    }

    public void setStorefrontID(String storefrontID) {
        this.storefrontID = storefrontID;
    }

    public String getCounterID() {
        return counterID;
    }

    public void setCounterID(String counterID) {
        this.counterID = counterID;
    }

    public String getIndoorID() {
        return indoorID;
    }

    public void setIndoorID(String indoorID) {
        this.indoorID = indoorID;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactCellphone() {
        return contactCellphone;
    }

    public void setContactCellphone(String contactCellphone) {
        this.contactCellphone = contactCellphone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
