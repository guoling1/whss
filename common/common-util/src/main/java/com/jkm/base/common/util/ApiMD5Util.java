package com.jkm.base.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 类描述：API MD5工具类
 * 
 * @author lxl
 * @since 1.0, 2016年08月08日
 */
@Slf4j
public class ApiMD5Util {
	
	/** 签名属性名 sign **/
	private static final String SIGN_KEY = "sign";
	
	/** 密钥属性名key**/
	private static final String SECRET_KEY = "key";

	/**
	 * 计算签名
	 * 
	 * @param map
	 *            要参与签名的map数据
	 * @param md5Key
	 *            密钥
	 * @return 签名
	 */
	public static String getSign(Map<String, ?> map, String md5Key) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.putAll(map);
		return getSign(jsonObj, md5Key);
	}

	/**
	 * 计算签名
	 * 
	 * @param jsonObj
	 *            要参与签名的json数据
	 * @param md5Key
	 *            密钥
	 * @return 签名
	 */
	public static String getSign(JSONObject jsonObj, String md5Key) {
		if (jsonObj == null || jsonObj.isEmpty()) {
			return null;
		}
		String str2Sign = buildParam4Sign(jsonObj, SIGN_KEY, md5Key);
		//String str2Sign = "agentNo=2017041200002&merchantInfo={\"addressInfo\":{\"address\":\"大剧院嘉宾花园 A 座 27H\",\"city\":\"深圳市\",\"district\":\"罗湖区\",\"province\":\"广东省\"},\"bankCardInfo\":{\"bankAccountAddress\":\"中国工商银行莲花北支行\",\"bankAccountLineNo\":\"102100000089\",\"bankAccountName\":\"周瑾\",\"bankAccountNo\":\"6212264000062521428\",\"bankAccountType\":\"1\",\"idCard\":\"362202199403308113\"},\"businessLicense\":\"320483000067847\",\"businessLicenseType\":\"NATIONAL_LEGAL\",\"contactInfo\":{\"contactEmail\":\"183400826@qq.com\",\"contactName\":\"周瑾\",\"contactPersonType\":\"LEGAL_PERSON\",\"contactPhone\":\"13267141053\",\"商户地址区\":\"362202199403308113\"},\"fullName\":\"balabala 巴辣香锅会展中心店\",\"servicePhone\":\"95188\",\"shortName\":\"巴辣香锅\"}&randomStr=710af59e-bce8-4e2d-a334-089e9612afc4&signType=MD5&key=166d6c2dff164b479fa2df30533230ad";
		String result = DigestUtils.md5Hex(str2Sign).toUpperCase();
		log.info("MD5签名原始串：{}，签名结果：{}", new Object[] { str2Sign, result });
		return result;
	}

	public static String getSignForM(Map map, String md5Key) {

		if (map == null || map.isEmpty()) {
			return null;
		}
		String str2Sign = buildParam4SignForM(map, SIGN_KEY, md5Key);
		String result = DigestUtils.md5Hex(str2Sign).toUpperCase();
		log.info("MD5签名原始串：{}，签名结果：{}", new Object[] { str2Sign, result });
		return result;
	}
	/**
	 * 验证签名
	 * 
	 * @param map
	 *            要参与签名的map数据
	 * @param md5Key
	 *            密钥
	 * @param sign
	 *            签名
	 * @return
	 */
	public static boolean verifySign(Map<String, ?> map, String md5Key, String sign) {
		String md5Text = getSign(map, md5Key);
		return md5Text.equalsIgnoreCase(sign);
	}

	/**
	 * 验证签名
	 * 
	 * @param jsonObject
	 *            要参与签名的json数据
	 * @param md5Key
	 *            密钥
	 * @param sign
	 *            签名
	 * @return
	 */
	public static boolean verifySign(JSONObject jsonObject, String md5Key, String sign) {
		String md5Text = getSign(jsonObject, md5Key);
		return md5Text.equalsIgnoreCase(sign);
	}
	
	/**
	 * 拼接用于签名的参数
	 * @param jsonObj
	 * @return
	 */
	private static String buildParam4Sign(JSONObject jsonObj, String signKey, String md5Key) {
		Set<String> keySet = jsonObj.keySet();
		StringBuilder param = new StringBuilder(20 * keySet.size());
		String[] keys = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (String key : keys) {
			// 排除sign
			if (signKey.equals(key)) {
				continue;
			}
			Object value = jsonObj.get(key);
			// 排除值为null的情况
			if (value != null) {
				param.append(key).append("=").append(value).append("&");
			}
		}
		param.append(SECRET_KEY).append("=").append(md5Key);
		return param.toString();
	}

	private static String buildParam4SignForM(Map map, String signKey, String md5Key) {
		Set<String> keySet = map.keySet();
		StringBuilder param = new StringBuilder(20 * keySet.size());
		String[] keys = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (String key : keys) {
			// 排除sign
			if (signKey.equals(key)) {
				continue;
			}
			Object value = map.get(key);
			if ("merchantInfo".equals(key)){
				value = JSONObject.toJSONString(value);
			}
			if ("merchantProduct".equals(key)){
				value = JSONObject.toJSONString(value);
			}
			// 排除值为null的情况
			if (value != null) {
				param.append(key).append("=").append(value).append("&");
			}
		}
		param.append(SECRET_KEY).append("=").append(md5Key);
		return param.toString();
	}
	
}
