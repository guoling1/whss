package com.jkm.hss.merchant.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.service.SendMsgService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/2.
 */
@Slf4j
@Service
public class SendMsgServiceImpl implements SendMsgService {
    @Override
    public void sendMessage(String money,String orderNo,String payNo,String store,String merchantName,String touser) {
            Map<String, String> ret = new HashMap<String, String>();
            String turl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ WxPubUtil.getToken();

            HttpClient client = new DefaultHttpClient();
            HttpPost method = new HttpPost(turl);
            JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
            try
            {
                JSONObject jsonParam = new JSONObject();

                JSONObject jo = new JSONObject();

                JSONObject first =new JSONObject();
                first.put("value","您好,收到一笔新的支付订单!");
                jo.put("first",first);
                JSONObject keyword1 =new JSONObject();
                keyword1.put("value",money);
                jo.put("keyword1",keyword1);
                JSONObject keyword2 =new JSONObject();
                keyword2.put("value",orderNo);
                jo.put("keyword2",keyword2);
                JSONObject keyword3 =new JSONObject();
                keyword3.put("value",payNo);
                jo.put("keyword3",keyword3);
                JSONObject keyword4 =new JSONObject();
                keyword4.put("value",store);
                jo.put("keyword4",keyword4);
                JSONObject keyword5 =new JSONObject();
                keyword5.put("value",merchantName);
                jo.put("keyword5",keyword5);
                JSONObject remark = new JSONObject();
                remark.put("value","感谢您的支持，如有疑问请联系客服!");
                jo.put("remark",remark);


                jsonParam.put("touser",touser);
                jsonParam.put("template_id","JW4HPfr8xrm0iQ7lqcWzHQKASHh79y69NMgMWOi41bE");
                jsonParam.put("data",jo);

                String tt  = jsonParam.toString();
                method.setEntity(new StringEntity(jsonParam.toString(), "UTF-8"));
                HttpResponse res = client.execute(method);
                String responseContent = null; // 响应内容
                HttpEntity entity = res.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();
                // 将json字符串转换为json对象
                if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    if (json.get("errcode")!=null)
                    {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
//                    ret.put("errcode", json.get("errcode").getAsString());
//                    ret.put("errmsg",json.get("errmsg").getAsString());
                    }
                    else
                    {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
                        ret.put("openid", json.get("openid").getAsString());
                        ret.put("accesstoken",json.get("access_token").getAsString());
                        ret.put("refresh_token",json.get("refresh_token").getAsString());
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                // 关闭连接 ,释放资源
                client.getConnectionManager().shutdown();
            }
        }

    @Override
    public void sendPushMessage(final BigDecimal totalAmount, final Date withdrawTime, final BigDecimal poundage,
                                final String bankNo, final String toUser) {
        Map<String, String> ret = new HashMap<String, String>();
        String turl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ WxPubUtil.getToken();

        final HttpClient client = new DefaultHttpClient();
        final HttpPost method = new HttpPost(turl);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        try {
            final DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
            JSONObject jsonParam = new JSONObject();

            JSONObject jo = new JSONObject();

            final JSONObject first =new JSONObject();
            first.put("value","您于" + format.format(withdrawTime) + "申请的提现已到账，请查收。");
            jo.put("first",first);

            final JSONObject keyword1 =new JSONObject();
            keyword1.put("value", totalAmount);
            jo.put("keyword1", keyword1);

            final JSONObject keyword2 =new JSONObject();
            keyword2.put("value", poundage);
            jo.put("keyword2",keyword2);

            final JSONObject keyword3 =new JSONObject();
            keyword3.put("value", totalAmount.subtract(poundage).toPlainString());
            jo.put("keyword3",keyword3);

            JSONObject remark = new JSONObject();
            remark.put("value","到账银行卡尾号 " + bankNo + "，如有疑问，请联系我们：400-622-6233");
            jo.put("remark",remark);
            jsonParam.put("touser", toUser);
            jsonParam.put("template_id","pdQoWhmiFChbff5YTTzj-86X_z9OjUsc1aqWPVeX4R8");
            jsonParam.put("data",jo);

            method.setEntity(new StringEntity(jsonParam.toString(), "UTF-8"));
            HttpResponse res = client.execute(method);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();
            // 将json字符串转换为json对象
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                if (json.get("errcode")!=null)
                {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
//                    ret.put("errcode", json.get("errcode").getAsString());
//                    ret.put("errmsg",json.get("errmsg").getAsString());
                }
                else
                {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
                    ret.put("openid", json.get("openid").getAsString());
                    ret.put("accesstoken",json.get("access_token").getAsString());
                    ret.put("refresh_token",json.get("refresh_token").getAsString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // 关闭连接 ,释放资源
            client.getConnectionManager().shutdown();
        }
    }

}
