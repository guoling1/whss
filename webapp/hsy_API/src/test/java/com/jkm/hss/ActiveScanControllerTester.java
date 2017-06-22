package com.jkm.hss;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.util.AppDateUtil;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Allen on 2017/6/21.
 */
public class ActiveScanControllerTester {
    @Test
    public void testInsertHsyOrder()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001057");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"shopid\": 333,\"amount\":0.02}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testUpdateHsyOrderAndPay()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001058");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"id\": 1192,\"authCode\":\"130533030354204664\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testFindHsyOrderRelateInfo()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001059");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"id\": 1191}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }
}
