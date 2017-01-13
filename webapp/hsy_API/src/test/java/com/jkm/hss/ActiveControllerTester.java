package com.jkm.hss;

import com.google.gson.Gson;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.entity.AppResult;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by Allen on 2017/1/7.
 */
public class ActiveControllerTester {
    public static String url="http://localhost:8080/hsy/active/rest";

    @Test
    public void testInsertHsyUser()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001001");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(DateFormatUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cellphone\": \"13521691431\",\"password\":\"123456\",\"code\":\"667828\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testLogin()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001002");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(DateFormatUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cellphone\": \"13521691431\",\"password\":\"123456\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testInsertAndSendVerificationCode()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001003");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(DateFormatUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
//        String param="{\"cellphone\": \"13041250755\",\"type\":\"2\"}";
        String param="{\"cellphone\": \"13521691431\",\"type\":\"2\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testUpdateHsyUserForSetPassword()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001004");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(DateFormatUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cellphone\": \"13041250755\",\"password\":\"654321\",\"code\":\"227959\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testInsertHsyShop()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001005");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(DateFormatUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"name\": \"店铺名称\",\"shortName\":\"商铺简称\",\"industryCode\":\"1000\",\"districtCode\":\"110000\",\"address\":\"街道\",\"isPublic\":1,\"uid\":12}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testUpdateHsyShopContact()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001006");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(DateFormatUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"id\":5,\"contactName\":\"联系人\",\"contactCellphone\":\"13521691431\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testInsertHsyCard()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001007");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(DateFormatUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cardNO\":\"4033930019071753\",\"cardBank\":\"1000000\",\"cardAccountName\":\"开户名\",\"sid\":5,\"bankAddress\":\"开户行支行\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    public static void testRest(AppParam p)throws Exception{
        HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod("POST");
//		httpConnection.setReadTimeout(5*1000);
        httpConnection.setDoInput(true);
        httpConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpConnection.setRequestProperty("accept", "*/*");
        httpConnection.setRequestProperty("connection", "Keep-Alive");
        httpConnection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

        String s="serviceCode="+p.getServiceCode()+
                "&accessToken="+p.getAccessToken()+
                "&appType="+p.getAppType()+
                "&timeStamp="+p.getTimeStamp()+
                "&deviceid="+p.getDeviceid()+
                "&v="+p.getV();
        if(p.getRequestData()!=null)
            s+="&requestData="+p.getRequestData();
        OutputStream output = httpConnection.getOutputStream();
        output.write(s.getBytes("utf-8"));
        output.flush();
        output.close();

        InputStream inStream = httpConnection.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        output.close();
        outStream.close();
        String json = new String(outStream.toByteArray(),"utf-8");
        System.out.println("appParam---"+s);
        System.out.println("appResult---"+json);
        Gson gson=new Gson();
        AppResult result=gson.fromJson(json, AppResult.class);
        System.out.println("dataResult---");
    }
}
