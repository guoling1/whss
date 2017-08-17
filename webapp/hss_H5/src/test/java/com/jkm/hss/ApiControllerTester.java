package com.jkm.hss;

import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Slf4j
public class ApiControllerTester {
    private static String API001="http://localhost:8081/api/hss/mctApply";
    private static String API002="http://localhost:8081/api/hss/kuaiPayOpenCard";

    @Test
    public void testmctApply()throws Exception{
        Map map = new HashMap();
        map.put("dealerMarkCode","320000000035");
        map.put("merchantName","邢留杰测试001");
        map.put("mobile","13155555559");
        map.put("provinceCode","110000");
        map.put("cityCode","110000");
        map.put("countyCode","110101");
        map.put("address","科林大厦");
        map.put("branchCode","305100001024");
        map.put("branchName","中国民生银行股份有限公司北京正义路支行");
        map.put("bankProvinceCode","110000");
        map.put("bankCityCode","110000");
        map.put("bankCountryCode","110101");
        map.put("bankNo","6226220117842210");
        map.put("name","邢留杰");
        map.put("identity","411082198805113634");
        map.put("reserveMobile","13146716739");
        String sign = SdkSignUtil.sign2(map, "");
        map.put("sign",sign);
        ApiControllerTester.testRest(map,API001);
    }

    @Test
    public void testKuaiPayOpenCard()throws Exception{
        Map map = new HashMap();
        map.put("merchantNo","120000001931");
        map.put("dealerMarkCode","320000000035");
        map.put("frontUrl","http://www.baidu.com");
        map.put("bindCardReqNo","1000000114");
        map.put("cardNo","6259655533117715 ");
        String sign = SdkSignUtil.sign2(map, "");
        map.put("sign",sign);
        ApiControllerTester.testRest(map,API002);
    }

    public static void testRest(Map<String, String> paramsMap,String url)throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            method.addHeader("Content-type", "application/json; charset=utf-8");
            method.setHeader("Accept", "application/json");
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
