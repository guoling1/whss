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

    @Test
    public void testUpdatePassword()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001063");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"id\": 71,\"passwordOrigin\":\"123456\",\"password\":\"hao0818\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testInsertAndSendVerificationCode()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001064");
        p.setAccessToken(ActiveControllerTester.accessToken);
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(ActiveControllerTester.v);
        String param="{\"cellphone\": \"18612406643\",\"type\":\"2\"}";
//        String param="{\"cellphone\": \"13521691431\",\"type\":\"2\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }
}
