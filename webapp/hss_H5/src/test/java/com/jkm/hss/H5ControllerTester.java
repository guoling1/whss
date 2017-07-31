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
    public static String HSSH5001001="http://localhost:8081/appMerchantInfo/getRegisterCode";
    public static String HSSH5001002="http://localhost:8081/appMerchantInfo/register";

    @Test
    public void testGetRegisterCode()throws Exception{
        Map<String, String> map = new JSONObject();
        map.put("mobile","13155555555");
        map.put("oemId","0");
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





    public static void testRest(Map<String, String> map,String url)throws Exception{
        String result = SmPost.post(url,map);
        log.info(result);
    }
}
