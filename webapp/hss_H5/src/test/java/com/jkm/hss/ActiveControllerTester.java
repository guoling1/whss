package com.jkm.hss;

import com.google.gson.Gson;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.push.entity.AppResult;
import com.jkm.hsy.user.util.AppDateUtil;
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
    public static String url="http://localhost:8081/active/rest";
    public static String v="v1.0.0";

    @Test
    public void testInsertTokenDeviceClientInfoAndReturnKey()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001001");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setAppType("android");
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"deviceId\":\"4707D3CA-EB83-4064-81CD-21E84933F5CB\",\"deviceName\":\"设备名\",\"osVersion\":\"6.0.0\",\"appVersion\":\"1.0.0\",\"appChannel\":\"MI\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testUpdateClientID()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001002");
        p.setAccessToken("c1bec574ef95ba380d92cc55fa180233");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"clientId\":\"clientid12345678\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testGetAppVersion()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001003");
        p.setAccessToken("c1bec574ef95ba380d92cc55fa180233");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"versionCode\":\"1.0.0\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }


    public static void testRest(AppParam p)throws Exception{
        HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod("POST");
        httpConnection.setDoInput(true);
        httpConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpConnection.setRequestProperty("accept", "*/*");
        httpConnection.setRequestProperty("connection", "Keep-Alive");
        httpConnection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        String s="serviceCode="+p.getServiceCode()+
                "&accessToken="+p.getAccessToken()+
                "&appType="+p.getAppType()+
                "&timeStamp="+p.getTimeStamp()+
                "&deviceId="+p.getDeviceId()+
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
        System.out.println("dataResult---"+result.getEncryptDataResult());
    }
}
