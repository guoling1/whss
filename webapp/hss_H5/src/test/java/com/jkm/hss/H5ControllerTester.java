package com.jkm.hss;

import com.google.gson.Gson;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.push.entity.AppResult;
import com.jkm.hsy.user.util.AppDateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 2017/1/7.
 */
@Slf4j
public class H5ControllerTester {
    public static String accessToken = "b2068198baf4c479221b03f615c4fbf1";
    public static String HSSH5001001="http://localhost:8081/wx/getCode";
    public static String HSSH5001002="http://localhost:8081/appMerchantInfo/register";
    public static String HSSH5001003="http://localhost:8081/appMerchantInfo/getRecommendInfo";
    public static String HSSH5001006="http://localhost:8081/appMerchantInfo/sendVerifyCode";
    public static String HSSH5001008="http://localhost:8081/appMerchantInfo/getMerchanStatus";
    public static String HSSH5001009="http://localhost:8081/appMerchantInfo/authentication";

    @Test
    public void testGetRegisterCode()throws Exception{
        Map<String, String> map = new JSONObject();
        map.put("mobile","13155555555");
        map.put("oemNo","0");
        H5ControllerTester.testRest(map,HSSH5001001);
    }

    @Test
    public void testRegister()throws Exception{
        Map<String, String> map = new JSONObject();
        map.put("mobile","13146716739");
        map.put("code","485074");
        map.put("inviteCode","13146716739");
        H5ControllerTester.testRest(map,HSSH5001002);
    }

    @Test
    public void testGetRecommendInfo()throws Exception{
        Map<String, String> map = new JSONObject();
        map.put("inviteCode","13146716739");
        map.put("oemNo","");
        H5ControllerTester.testRest(map,HSSH5001003);
    }

    @Test
    public void testSendVerifyCode()throws Exception{
        Map<String, String> map = new JSONObject();
        map.put("bankNo","6226220117842210");
        map.put("name","邢留杰");
        map.put("identity","411082198805113634");
        map.put("reserveMobile","13146716739");
        H5ControllerTester.testRest(map,HSSH5001006);
    }

    @Test
    public void testGetMerchanStatus()throws Exception{
        Map<String, String> map = new JSONObject();
        H5ControllerTester.testRest(map,HSSH5001008);
    }

    @Test
    public void testAuthentication()throws Exception{
        Map<String, String> map = new JSONObject();
        H5ControllerTester.testRest(map,HSSH5001009);
    }



    public static void testRest(Map<String, String> paramsMap,String url)throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            method.addHeader("Content-type", "application/json; charset=utf-8");
            method.setHeader("Accept", "application/json");
            method.setHeader("accessToken",accessToken);
            if (paramsMap != null) {
                JSONObject jsonParam = new JSONObject();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    jsonParam.put(param.getKey(), param.getValue());
                }
                method.setEntity(new StringEntity(jsonParam.toString(), "UTF-8"));

            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info(responseText);
    }
}
