package com.jkm.hss;

import com.google.gson.Gson;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.entity.AppResult;
import com.jkm.hsy.user.util.AppDateUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

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
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cellphone\": \"13521691431\",\"password\":\"123456\",\"code\":\"272683\",\"shopName\":\"店铺名称\",\"industryCode\":\"1000\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testLogin()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001002");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
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
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
//        String param="{\"cellphone\": \"13041250755\",\"type\":\"2\"}";
        String param="{\"cellphone\": \"13521691431\",\"type\":\"1\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testUpdateHsyUserForSetPassword()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001004");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cellphone\": \"13041250755\",\"password\":\"654321\",\"code\":\"227959\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    /*@Test
    public void testUpdateHsyShop()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001005");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"id\":10,\"shortName\":\"商铺简称\",\"districtCode\":\"110000\",\"address\":\"街道\",\"isPublic\":1}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }*/
    /*
    @Test
    public void testUpdateHsyShopContact()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001006");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"id\":5,\"contactName\":\"联系人\",\"contactCellphone\":\"13521691431\",\"uid\":18}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }*/

    @Test
    public void testInsertHsyCard()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001007");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cardNO\":\"4033930019071753\",\"cardBank\":\"1000000\",\"cardAccountName\":\"开户名\",\"sid\":5,\"bankAddress\":\"开户行支行\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testFindHsyCardBankByBankNO()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001008");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
        String param="{\"cardNO\":\"4033930019071753\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testFindDistrictByParentCode()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001009");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
//        String param="{\"parentCode\":\"0\"}";
        String param="{\"parentCode\":\"110000\"}";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void testFindIndustryList()throws Exception{
        AppParam p=new AppParam();
        p.setServiceCode("HSY001013");
        p.setAccessToken("");
        p.setAppType("android");
        p.setTimeStamp(AppDateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        p.setV("v1.0");
//        String param="{\"parentCode\":\"0\"}";
        String param="";
        p.setRequestData(param);
        ActiveControllerTester.testRest(p);
    }

    @Test
    public void generateSQL()throws Exception{
        FileChannel channel=new FileInputStream("D:/a.txt").getChannel();
        int size=(int) channel.size();
        ByteBuffer buffer=ByteBuffer.allocate(57199);
        channel.read(buffer);
        Buffer bf= buffer.flip();
        System.out.println("limt:"+bf.limit());
        byte[] bt=buffer.array();
        String text=new String(bt,0,size,"GBK");
        text=text.replace("\r","").replace("\n","");
        List<String> list= Arrays.asList(text.split(","));
        Map<String,String> map=new LinkedHashMap<String,String>();
        for(String s:list)
        {
            String[] x=s.split(":");
            map.put(x[0],x[1]);
        }
        Set<String> set=map.keySet();
        Iterator<String> it=set.iterator();
        while(it.hasNext())
        {
            String key=it.next();
            String value=map.get(key);
            if(key.substring(2).equals("0000")) {
                String sql="insert into app_biz_district values('"+key+"','"+value+"',"+key.substring(0,2)+",1,'0');";
                System.out.println(sql);
            }
            else{
                if(key.substring(4).equals("00"))
                {
                    String parentid=key.substring(0,2)+"0000";
                    String sql="insert into app_biz_district values('"+key+"','"+value+"',"+key.substring(2,4)+",2,'"+parentid+"');";
                    System.out.println(sql);
                }
                else
                {
                    String parentkey=key.substring(0,4)+"00";
                    if(map.get(parentkey)!=null)
                    {
                        String sql="insert into app_biz_district values('"+key+"','"+value+"',"+key.substring(4,6)+",3,'"+parentkey+"');";
                        System.out.println(sql);
                    }
                    else
                    {
                        String sql="insert into app_biz_district values('"+key+"','"+value+"',"+key.substring(4,6)+",2,'"+key.substring(0,2)+"0000"+"');";
                        System.out.println(sql);
                    }
                }
            }
            //System.out.println(key);
        }
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
        System.out.println("dataResult---"+result.getEncryptDataResult());
    }
}
