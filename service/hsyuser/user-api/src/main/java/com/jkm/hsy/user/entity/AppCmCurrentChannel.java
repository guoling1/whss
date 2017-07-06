package com.jkm.hsy.user.entity;

/**
 * Created by Allen on 2017/5/26.
 */
/**app_cm_current_channel*/
public class AppCmCurrentChannel {
    private Long uid;
    private Long wecharTrateT1ID;//微信通道T1 ID
    private Long alipayRateT1ID;//支付宝通道T1 ID
    private Long wecharTrateT0ID;//微信通道T0 ID
    private Long alipayRateT0ID;//支付宝通道T0 ID

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getWecharTrateT1ID() {
        return wecharTrateT1ID;
    }

    public void setWecharTrateT1ID(Long wecharTrateT1ID) {
        this.wecharTrateT1ID = wecharTrateT1ID;
    }

    public Long getAlipayRateT1ID() {
        return alipayRateT1ID;
    }

    public void setAlipayRateT1ID(Long alipayRateT1ID) {
        this.alipayRateT1ID = alipayRateT1ID;
    }

    public Long getWecharTrateT0ID() {
        return wecharTrateT0ID;
    }

    public void setWecharTrateT0ID(Long wecharTrateT0ID) {
        this.wecharTrateT0ID = wecharTrateT0ID;
    }

    public Long getAlipayRateT0ID() {
        return alipayRateT0ID;
    }

    public void setAlipayRateT0ID(Long alipayRateT0ID) {
        this.alipayRateT0ID = alipayRateT0ID;
    }
}
