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
		bizMapper.put("HSY001005", new String[]{"hsyShopService","updateHsyShop","修改店铺信息成功"});
		bizMapper.put("HSY001006", new String[]{"hsyShopService","updateHsyShopContact","修改店铺信息成功"});
		bizMapper.put("HSY001007", new String[]{"hsyShopService","insertHsyCard","保存结算账户成功"});
		bizMapper.put("HSY001008", new String[]{"hsyShopService","findHsyCardBankByBankNO"});
		bizMapper.put("HSY001009", new String[]{"hsyShopService","findDistrictByParentCode"});
//		bizMapper.put("HSY001010", new String[]{"hsyFileService","insertFileLicenceAndUpload","上传营业执照成功"});
		bizMapper.put("HSY001011", new String[]{"hsyFileService","insertFileShopAndUpload","上传商铺相关照片成功"});
//		bizMapper.put("HSY001012", new String[]{"hsyFileService","insertFileUserAndUpload","上传用户身份证相关照片成功"});
		bizMapper.put("HSY001013", new String[]{"hsyShopService","findIndustryList"});

	}
}
