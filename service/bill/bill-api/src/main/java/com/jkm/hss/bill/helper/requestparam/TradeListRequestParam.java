package com.jkm.hss.bill.helper.requestparam;
import com.jkm.base.common.entity.PageQueryParams;

/**
 * Created by wayne on 17/5/16.
 */
public class TradeListRequestParam extends PageQueryParams {
    private int shopId;
    private String startTime;
    private String endTime;
    private String channel;
    private String[] paymentChannels;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String[] getPaymentChannels() {
        return paymentChannels;
    }

    public void setPaymentChannels(String[] paymentChannels) {
        this.paymentChannels = paymentChannels;
    }
}
