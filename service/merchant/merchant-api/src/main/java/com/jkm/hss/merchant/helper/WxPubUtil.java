package com.jkm.hss.merchant.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jkm.hss.merchant.entity.WxConfig;
import com.jkm.hss.merchant.enums.EnumCommonStatus;
import com.jkm.hss.merchant.service.WxConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-24 16:04
 */
@Slf4j
@Component
public class WxPubUtil {
    @Autowired
    private WxConfigService wxConfigService;
    private static WxPubUtil wxPubUtil;

    @PostConstruct
    public void init() {
        wxPubUtil = this;
        wxPubUtil.wxConfigService = this.wxConfigService;

    }
    /**
     * 获取OpenId
     * @param code
     * @return
     */
    public static Map<String, String> getOpenid(String code)
    {
        Map<String, String> ret = new HashMap<String, String>();
        String turl = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code", WxConstants.GET_LOGIN_TOKEN_URL,WxConstants.APP_ID, WxConstants.APP_KEY,code);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(turl);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        try
        {
            HttpResponse res = client.execute(get);
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
            return ret;
        }
    }

    /**
     * 获取OpenId
     * @param code
     * @return
     */
    public static Map<String, String> getOpenid(String code,String appId,String appSecret)
    {
        Map<String, String> ret = new HashMap<String, String>();
        String turl = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code", WxConstants.GET_LOGIN_TOKEN_URL,appId, appSecret,code);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(turl);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        try
        {
            HttpResponse res = client.execute(get);
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
            return ret;
        }
    }
    /**
     * 获取用户基本信息
     * @param openId
     * @return
     */
    public static Map<String, String> getUserInfo(String openId)
    {
        Map<String, String> token= getTokenAndJsapiTicket();
        Map<String, String> ret = new HashMap<String, String>();
        String turl = String.format("%s?access_token=%s&openid=%s&lang=zh_CN", WxConstants.GET_USER_INFO,token.get("accessToken"),openId);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(turl);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        try
        {
            HttpResponse res = client.execute(get);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();
            System.out.print("返回数据为:"+json.toString());
            // 将json字符串转换为json对象
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                if (json.get("errcode")!=null)
                {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
                    System.out.print("获取微信用户信息错误，错误信息是:"+json.get("errmsg").getAsString());
                    ret.put("nickname", "");
                    ret.put("headimgurl","");
                    ret.put("subscribe","");
                }
                else
                {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
                    if(json.get("subscribe").getAsInt()==1){//
                        ret.put("nickname", json.get("nickname").getAsString());
                        ret.put("headimgurl",json.get("headimgurl").getAsString());
                        ret.put("subscribe",json.get("subscribe").getAsInt()+"");
                    }else{
                        ret.put("nickname", "");
                        ret.put("headimgurl","");
                        ret.put("subscribe",json.get("subscribe").getAsInt()+"");
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.print("获取微信用户信息异常:"+e);
            ret.put("nickname", "");
            ret.put("headimgurl","");
            ret.put("subscribe","");
        }
        finally
        {
            // 关闭连接 ,释放资源
            client.getConnectionManager().shutdown();
            return ret;
        }
    }

    /**
     * 方法名称: getToken<br>
     * 描述：获取Token
     * 作者: 邢留杰
     * 修改日期：2015年12月19日上午9:46:18
     * @return
     */
    public static String getToken()
    {
        String turl = String.format("%s?grant_type=client_credential&appid=%s&secret=%s", WxConstants.GET_TOKEN_URL,WxConstants.APP_ID, WxConstants.APP_KEY);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(turl);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        String result = null;
        try
        {
            HttpResponse res = client.execute(get);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();// 将json字符串转换为json对象
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                if (json.get("errcode") != null)
                {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
                }
                else
                {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
                    result = json.get("access_token").getAsString();
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
            return result;
        }
    }

    /**
     * 方法名称: getcallbackip<br>
     * 描述：获取服务器ip列表
     * 作者: 邢留杰
     * 修改日期：2015年12月19日上午9:46:18
     * @return
     */
    public static String getcallbackip(String accessToken)
    {
        String turl = String.format("%s?access_token=%s", WxConstants.GET_CALLBACK_IP,accessToken);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(turl);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        String result = null;
        try
        {
            HttpResponse res = client.execute(get);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();// 将json字符串转换为json对象
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                if (json.get("errcode") != null)
                {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
                }
                else
                {// 正常情况下{"ip_list":["127.0.0.1","127.0.0.1"]}
                    result="success";
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
            return result;
        }
    }

    /**
     * 方法名称: getJsapiTicket<br>
     * 描述：获取jsapiTicket
     * 作者: 邢留杰
     * 修改日期：2015年12月19日上午10:03:10
     * @param token
     * @return
     */
    public static String getJsapiTicket(String token)
    {
        String turl = String.format("%s?access_token=%s&type=jsapi", WxConstants.GET_TICKET_URL,token);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(turl);
        JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
        String result = null;
        try
        {
            HttpResponse res = client.execute(get);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();
            // 将json字符串转换为json对象
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                if (json.get("errcode").getAsInt() != 0)
                {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
                }
                else
                {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
                    result = json.get("ticket").getAsString();
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
            return result;
        }
    }

    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static Map<String, String> getTokenAndJsapiTicket() {
        Map<String, String> ret = new HashMap<String, String>();
        String accessToken = "";
        String jsapi_ticket = "";
        WxConfig wxConfig = wxPubUtil.wxConfigService.selectTop1();
        if(wxConfig==null){
            accessToken = getToken();// 获取token
            jsapi_ticket = getJsapiTicket(accessToken);
            WxConfig wx = new WxConfig();
            wx.setStatus(EnumCommonStatus.NORMAL.getId());
            wx.setAccessToken(accessToken);
            wx.setJsapiTicket(jsapi_ticket);
            wx.setExpireTime(System.currentTimeMillis()+60*60*1000);
            wxPubUtil.wxConfigService.insertSelective(wx);
        }else{
            if(wxConfig.getExpireTime()>System.currentTimeMillis()){
                accessToken = wxConfig.getAccessToken();// 获取token
                jsapi_ticket = wxConfig.getJsapiTicket();
                String ipList = wxPubUtil.getcallbackip(accessToken);
                if(ipList==null){//token过期
                    accessToken = getToken();// 获取token
                    jsapi_ticket = getJsapiTicket(accessToken);
                    WxConfig wx = new WxConfig();
                    wx.setStatus(EnumCommonStatus.NORMAL.getId());
                    wx.setAccessToken(accessToken);
                    wx.setJsapiTicket(jsapi_ticket);
                    wx.setExpireTime(System.currentTimeMillis()+60*60*1000);
                    wxPubUtil.wxConfigService.insertSelective(wx);
                }
            }else{
                accessToken = getToken();// 获取token
                jsapi_ticket = getJsapiTicket(accessToken);
                WxConfig wx = new WxConfig();
                wx.setStatus(EnumCommonStatus.NORMAL.getId());
                wx.setAccessToken(accessToken);
                wx.setJsapiTicket(jsapi_ticket);
                wx.setExpireTime(System.currentTimeMillis()+60*60*1000);
                wxPubUtil.wxConfigService.insertSelective(wx);
            }
        }
        ret.put("accessToken", accessToken);
        ret.put("jsapi_ticket", jsapi_ticket);
        return ret;
    }



    public static Map<String, String> sign(String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        Map<String, String> tokenAndTicket = getTokenAndJsapiTicket();
        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + tokenAndTicket.get("jsapi_ticket").toString() +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        ret.put("accessToken", tokenAndTicket.get("accessToken").toString());
        ret.put("url", url);
        ret.put("jsapi_ticket", tokenAndTicket.get("jsapi_ticket").toString());
        ret.put("appid",WxConstants.APP_ID);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    /**
     *
     * @param mediaId
     * @return
     */
    public  static  InputStream getInputStream(String mediaId) {
        Map<String, String> tokenAndTicket = getTokenAndJsapiTicket();
        InputStream is = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
                + tokenAndTicket.get("accessToken").toString() + "&media_id=" + mediaId;
        log.debug("mediaId:"+ mediaId);
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet
                    .openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            is = http.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;

    }

}
