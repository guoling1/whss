package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/4/19.
 */
public class AppBizBankBranch {
    private Long id;
    private String branchCode;//联行号
    private String branchName;//联行名称
    private String bankName;//所属银行
    private String province;//省
    private String city;//市
    private Integer available;//可用状态
    private String districtCode;//地区代码
    private Integer bankIndex;//顺序

    private Integer currentPage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public Integer getBankIndex() {
        return bankIndex;
    }

    public void setBankIndex(Integer bankIndex) {
        this.bankIndex = bankIndex;
    }
}
