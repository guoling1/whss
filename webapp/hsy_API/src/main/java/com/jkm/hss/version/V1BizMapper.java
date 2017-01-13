package com.jkm.hss.version;

import java.util.HashMap;
import java.util.Map;

public class V1BizMapper {
	public static Map<String,String[]> bizMapper=new HashMap<String, String[]>();
	static{
		bizMapper.put("HSY001001", new String[]{"hsyUserService","insertHsyUser","注册成功"});
		bizMapper.put("HSY001002", new String[]{"hsyUserService","login","登陆成功"});
		bizMapper.put("HSY001003", new String[]{"hsyUserService","insertAndSendVerificationCode","发送验证码成功"});
		bizMapper.put("HSY001004", new String[]{"hsyUserService","updateHsyUserForSetPassword","找回密码成功"});
		bizMapper.put("HSY001005", new String[]{"hsyShopService","insertHsyShop","保存店铺信息成功"});
		bizMapper.put("HSY001006", new String[]{"hsyShopService","updateHsyShopContact","修改店铺信息成功"});
		bizMapper.put("HSY001007", new String[]{"hsyShopService","insertHsyCard","保存结算账户成功"});
	}
}
