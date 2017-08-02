package com.jkm.hss;

import com.google.gson.Gson;
import com.jkm.base.common.util.AppAesUtil;
import com.jkm.base.common.util.DateUtil;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.push.entity.AppResult;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by Allen on 2017/1/7.
 */
public class ActiveControllerTester {
    public static String url="http://localhost:8081/active/rest";
    public static String v="v1.0.0";
    public static String privateKey = "6w3W8OOgnRZrkBGS2AdpFTpOykcUsvfI";
    public static String accessToken = "b2068198baf4c479221b03f615c4fbf1";

    @Test
    public void testInsertTokenDeviceClientInfoAndReturnKey()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001001");
        p.setAppType("ios");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("865873032687208");
        String param="{\"deviceId\":\"865873032687208\",\"deviceName\":\"Xiaomi MI 6\",\"osVersion\":\"7.1.1\",\"appVersion\":\"1.0.0\",\"appChannel\":\"web\",\"appCode\":\"hss\",\"oemNo\":\"\"}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", privateKey.substring(0,16), privateKey.substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testUpdateClientID()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001002");
        p.setAccessToken(accessToken);
        p.setAppType("android");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"clientId\":\"clientid12345678\"}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", p.getAccessToken().substring(0,16), p.getAccessToken().substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testFindVersionDetail()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001003");
        p.setAccessToken(accessToken);
        p.setAppType("android");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"versionCode\":1,\"appSort\":\"hssJkm\"}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", p.getAccessToken().substring(0,16), p.getAccessToken().substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testGetAppVersion()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001004");
        p.setAccessToken(accessToken);
        p.setAppType("android");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"versionCode\":1,\"appSort\":\"hssJkm\"}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", p.getAccessToken().substring(0,16), p.getAccessToken().substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testGetCode()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001005");
        p.setAccessToken(accessToken);
        p.setAppType("android");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"mobile\":\"13146716739\",\"oemNo\":\"\",\"type\":2}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", p.getAccessToken().substring(0,16), p.getAccessToken().substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testRegister()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001006");
        p.setAccessToken(accessToken);
        p.setAppType("android");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"mobile\":\"13146716739\",\"code\":\"123456\",\"inviteCode\":\"13146716739\"}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", p.getAccessToken().substring(0,16), p.getAccessToken().substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testLogin()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001007");
        p.setAccessToken(accessToken);
        p.setAppType("android");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        String param="{\"mobile\":\"13146716739\",\"code\":\"267069\",\"oemNo\":\"\"}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", p.getAccessToken().substring(0,16), p.getAccessToken().substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testLogout()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001008");
        p.setAccessToken(accessToken);
        p.setAppType("android");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("4707D3CA-EB83-4064-81CD-21E84933F5CB");
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testGetInitOemInfo()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSS001009");
        p.setAppType("ios");
        p.setTimeStamp(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV(v);
        p.setDeviceId("865873032687208");
        String param="{\"oemNo\":\"\"}";
        String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(param, "utf-8", privateKey.substring(0,16), privateKey.substring(16,32));
        String encodeUrl = URLEncoder.encode(base64E,"utf-8");
        p.setRequestData(encodeUrl);
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
