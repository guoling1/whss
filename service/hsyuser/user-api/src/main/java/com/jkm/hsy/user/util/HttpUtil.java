package com.jkm.hsy.user.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpUtil {

	public static String POST="POST";
	public static String GET="GET";

	public static String FORM="application/x-www-form-urlencoded";
	public static String TEXT="text/html";

	/**
	 * 进行http发送内容
	 *
	 * @param url 请求参数地址
	 * @param requestMethod 请求方法例如GET、POST
	 * @param contentType 内容类型例如：application/x-www-form-urlencoded（默认发送类型，表单）、text/html（文本）等
	 * @param encode 编码格式 例如utf-8、gbk
	 * @param message 发送内容
	 * @param timeout 读取超时时间 单位为秒 如果为null则默认读取时间
	 *
	 * @return 从流中取出的String
	 */
	public static String httpRequest(String url,String requestMethod,String contentType,String encode,String message,Integer timeout)throws Exception{
		HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod(requestMethod);
		if(timeout!=null)
			httpConnection.setReadTimeout(timeout*1000);
		httpConnection.setDoInput(true);
		httpConnection.setRequestProperty("Content-type", contentType);
		httpConnection.setRequestProperty("Accept-Charset", encode);
		httpConnection.setRequestProperty("accept", "*/*");
		httpConnection.setRequestProperty("connection", "Keep-Alive");
		httpConnection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		OutputStream output = httpConnection.getOutputStream();

		output.write(message.getBytes(encode));
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
		String result = new String(outStream.toByteArray(),encode);
		return result;
	}

	/**
	 * 进行http发送内容 以表单形式
	 *
	 * @param url 请求参数地址
	 * @param encode 编码格式 例如utf-8、gbk
	 * @param keyValueForm 需要传的参数
	 */
	@SuppressWarnings("rawtypes")
	public static String httpRequestWithForm(String url,String encode,Map keyValueForm)throws Exception{
		StringBuffer message=new StringBuffer();
		Set keyset=keyValueForm.keySet();
		Iterator it=keyset.iterator();
		while(it.hasNext())
		{
			String key= (String)it.next();
			message.append(key+"="+escapeSpecial(keyValueForm.get(key).toString())+"&");
		}
		if(message.length()!=0)
			message.deleteCharAt(message.length() - 1);
		return httpRequest(url,HttpUtil.POST,HttpUtil.FORM,encode,message.toString(),null);
	}

	/**
	 * 进行http发送内容 以报文形式
	 *
	 * @param url 请求参数地址
	 * @param encode 编码格式 例如utf-8、gbk
	 * @param message 报文
	 */
	public static String httpRequestWithForm(String url,String encode,String message)throws Exception{
		message=escapeSpecial(message);
		return httpRequest(url,HttpUtil.POST,HttpUtil.TEXT,encode,message,null);
	}

	/**
	 * 处理http转义字符
	 */
	public static String escapeSpecial(String message){
		message=message.replace("+", "%2B")
				.replace(" ", "%20")
				.replace("\"","%22")
				.replace("&", "%26")
				.replace("(", "%28")
				.replace(")", "%29")
				.replace(",", "%2C")
				.replace(":", "%3A")
				.replace(";", "%3B")
				.replace("<", "%3C")
				.replace("=", "%3D")
				.replace(">", "%3E")
				.replace("?", "%3F")
				.replace("@", "%40")
				.replace("\\","%5C")
				.replace("|", "%7C");
		message=message.replace("%", "%25");
		return message;
	}

	public static void main(String[] args) throws Exception{
//		String response=HttpUtil.httpRequest("http://localhost/test/fuck", "POST", "application/x-www-form-urlencoded", "utf-8", "data=1", null);
		Map<String,String> keyValueForm=new HashMap<String, String>();
		keyValueForm.put("data", "你猜");
		String response=HttpUtil.httpRequestWithForm("http://localhost/test/fuck", "utf-8", keyValueForm);
		System.out.println(response);
	}
}
