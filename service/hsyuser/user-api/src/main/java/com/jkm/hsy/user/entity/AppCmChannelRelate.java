package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/5/26.
 */

import java.math.BigDecimal;
import java.util.Date;

/**app_cm_channel_relate*/
public class AppCmChannelRelate {
    private Long id;
    private Long uid;//法人ID
    private Long pid;//tb_produce_channel_detail的ID
    private String channelName;//通道名称
    private String channelType;//通道类型 微信wechat 支付宝alipay
    private String channelSettleType;//通道结算类型 D0 T1
    private BigDecimal channelRate;//交易费率 有代理商的情况是代理商定制的费率
    private BigDecimal channelWithdrawFee;//提现成本费用 元／笔 有代理商的情况是代理商定制的提现费
    private Integer channelSign;//通道唯一标识
    private Integer isNeed;//是否需要商户入网 1 需要 2 不需要
    private Integer belongChannelStatus;//入网状态 0不支持,1成功,2未入网,3已提交,4失败
    private String belongChannelInfo;//入网信息
    private String accessChannelStatus;//产品开通状态 0不支持,1成功,2报备中
    private String accessChannelInfo;//产品开通信息
    private String exchannelCode;//渠道商户编号
    private String exchannelEventCode;//活动编号
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelSettleType() {
        return channelSettleType;
    }

    public void setChannelSettleType(String channelSettleType) {
        this.channelSettleType = channelSettleType;
    }

    public BigDecimal getChannelRate() {
        return channelRate;
    }

    public void setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
    }

    public BigDecimal getChannelWithdrawFee() {
        return channelWithdrawFee;
    }

    public void setChannelWithdrawFee(BigDecimal channelWithdrawFee) {
        this.channelWithdrawFee = channelWithdrawFee;
    }

    public Integer getChannelSign() {
        return channelSign;
    }

    public void setChannelSign(Integer channelSign) {
        this.channelSign = channelSign;
    }

    public Integer getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(Integer isNeed) {
        this.isNeed = isNeed;
    }

    public Integer getBelongChannelStatus() {
        return belongChannelStatus;
    }

    public void setBelongChannelStatus(Integer belongChannelStatus) {
        this.belongChannelStatus = belongChannelStatus;
    }

    public String getBelongChannelInfo() {
        return belongChannelInfo;
    }

    public void setBelongChannelInfo(String belongChannelInfo) {
        this.belongChannelInfo = belongChannelInfo;
    }

    public String getAccessChannelStatus() {
        return accessChannelStatus;
    }

    public void setAccessChannelStatus(String accessChannelStatus) {
        this.accessChannelStatus = accessChannelStatus;
    }

    public String getAccessChannelInfo() {
        return accessChannelInfo;
    }

    public void setAccessChannelInfo(String accessChannelInfo) {
        this.accessChannelInfo = accessChannelInfo;
    }

    public String getExchannelCode() {
        return exchannelCode;
    }

    public void setExchannelCode(String exchannelCode) {
        this.exchannelCode = exchannelCode;
    }

    public String getExchannelEventCode() {
        return exchannelEventCode;
    }

    public void setExchannelEventCode(String exchannelEventCode) {
        this.exchannelEventCode = exchannelEventCode;
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
}
