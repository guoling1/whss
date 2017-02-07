package com.jkm.hss.dealer.service.impl;

import com.google.gson.Gson;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActiveControllerTester {
	
	public static Gson gson=new Gson();
	public static String url="http://127.0.0.1:8080/user/findUserShopListById";
//	public static String url="http://hss.api.test.haodai.com/api/active/rest";
	
	public static final String CONSULTKEY ="12345678900987654321farv";
	public static final String ENCRYPTKEY="yaYsMVglQAghVyZSfpozKYKO";
	public static final String ACCESSTOKEN="1a26659e-ad1e-4f1e-966e-0d6c846c855b";
	
	
	
	
	@Test
	public void testFindUserShopListById()throws Exception{

		url="http://127.0.0.1:8080/user/findUserShopListById";

		String param="{\"uid\": \"123456\",\"pageNo\": \"1\",\"pageSize\": \"10\"}";


		ActiveControllerTester.testRest(param);
	}

	@Test
	public void testFindUserShopDetailById()throws Exception{


		url="http://127.0.0.1:8080/user/findUserShopDetailById";


		String param="{\"uid\": \"123456\"}";


		ActiveControllerTester.testRest(param);
	}


	@Test
	public void testAddUser()throws Exception{


		url="http://127.0.0.1:8080/user/addUser";


		String param="{\"uname\": \"test1\"  ,\"role\": \"0\",  \"cellphone\": \"15201255282\" ,  \"sid\": \"1\" }";


		ActiveControllerTester.testRest(param);
	}


	@Test
	public void updateUser()throws Exception{


		url="http://127.0.0.1:8080/user/updateUser";


		String param="{  \"uid\": \"123456\",\"uname\": \"test2\"  ,\"role\": \"0\",  \"cellphone\": \"15201255282\" ,  \"sid\": \"123456\"   }";


		ActiveControllerTester.testRest(param);
	}



	@Test
	public void findShopByMerId()throws Exception{


		url="http://127.0.0.1:8080/user/findShopByMerId";


		String param="{  \"uid\": \"18\"   }";


		ActiveControllerTester.testRest(param);
	}





	@Test
	public void pushSY()throws Exception{


		url="http://127.0.0.1:8080/pushMsg/pushSY";


		String param="{  \"sid\": \"123456\", \"content\":\"test\", \"setType\":\"1\"}";


		ActiveControllerTester.testRest(param);
	}
	

	
//	@Test
//	public void testSendSms()throws Exception{
//		HttpURLConnection httpConnection = (HttpURLConnection)new URL("http://api.bs.haodai.com/api/notify").openConnection();
//		httpConnection.setDoOutput(true);
//		httpConnection.setRequestMethod("POST");
//		httpConnection.setReadTimeout(0);
//		httpConnection.setDoInput(true);
//		httpConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//		httpConnection.setRequestProperty("Accept-Charset", "utf-8");
//		httpConnection.setRequestProperty("accept", "*/*");
//		httpConnection.setRequestProperty("connection", "Keep-Alive");
//		httpConnection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//
//		String data="{\"mobiles\":[\"13521691431\"],\"message\":\"������֤����:334455,�����κ��˽�����ȡ,����й¶,�뼰ʱ��֤!\",\"provider\":9}";
//
//		System.out.println(data.length());
//
//		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//        int blockSize = cipher.getBlockSize();
//		byte[] dataBytes = data.getBytes("utf-8");
//        int plaintextLength = dataBytes.length;
//        if (plaintextLength % blockSize != 0) {
//            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
//        }
//        byte[] plaintext = new byte[plaintextLength];
//        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
//
//		SecretKeySpec keySpec = new SecretKeySpec("61243d4fa76d5a64".getBytes("utf-8"), "AES");
//        IvParameterSpec iv = new IvParameterSpec("1234567812345678".getBytes("utf-8"));
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
//        byte[] result=cipher.doFinal(plaintext);
//
//        System.out.println(data);
//		data = new BASE64Encoder().encode(result);
//		System.out.println("BASE64----"+data);
//		data=data.replace("+", "%2B");
//		System.out.println("BASE64----   "+data);
//
//		byte[] dataByte=new BASE64Decoder().decodeBuffer(data.replace("%2B", "+"));
//		cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
//        System.out.println("----"+new String(cipher.doFinal(dataByte),"utf-8"));
//
//
//		String s="module=4&product_id=17&version=1.0&service_name=sms&data=";
//		s=s+data;
//		System.out.println(s);
//		OutputStream output = httpConnection.getOutputStream();
//        output.write(s.getBytes("utf-8"));
//        output.flush();
//        output.close();
//        InputStream inStream = httpConnection.getInputStream();
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        while( (len=inStream.read(buffer)) != -1 ){
//            outStream.write(buffer, 0, len);
//        }
//        inStream.close();
//        output.close();
//        outStream.close();
//        String json = new String(outStream.toByteArray(),"utf-8");
//        System.out.println(json);
//	}
	
	public static void testRest(String test)throws Exception{
		HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
//		httpConnection.setReadTimeout(5*1000);
		httpConnection.setDoInput(true);
		httpConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpConnection.setRequestProperty("accept", "*/*");
		httpConnection.setRequestProperty("connection", "Keep-Alive");
		httpConnection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		httpConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");


		String s=test;
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

	}

	

	
}
