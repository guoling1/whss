package com.jkm.hss.version;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 2017/5/12.
 */
public class V1o6BizMapper {
    public static Map<String,String[]> bizMapper=new HashMap<String, String[]>();
    static{
        bizMapper.put("HSY001001", new String[]{"hsyUserService","insertHsyUser","注册成功"});
        bizMapper.put("HSY001002", new String[]{"hsyUserService","login","登陆成功"});
        bizMapper.put("HSY001003", new String[]{"hsyUserService","insertAndSendVerificationCode","发送验证码成功"});
        bizMapper.put("HSY001004", new String[]{"hsyUserService","updateHsyUserForSetPassword","找回密码成功"});
        bizMapper.put("HSY001005", new String[]{"hsyShopService","updateHsyShop","修改店铺信息成功"});
        bizMapper.put("HSY001006", new String[]{"hsyShopService","updateHsyShopContact1o6","修改店铺信息成功"});
        bizMapper.put("HSY001007", new String[]{"hsyShopService","insertHsyCard","保存结算账户成功"});
        bizMapper.put("HSY001008", new String[]{"hsyShopService","findHsyCardBankByBankNO"});
        bizMapper.put("HSY001009", new String[]{"hsyShopService","findDistrictByParentCode"});
        bizMapper.put("HSY001010", new String[]{"hsyShopService","findShopList"});
        bizMapper.put("HSY001011", new String[]{"hsyFileService","insertFileShopAndUpload","上传商铺相关照片成功"});
        bizMapper.put("HSY001012", new String[]{"hsyShopService","findShopDetail"});
        bizMapper.put("HSY001013", new String[]{"hsyShopService","findIndustryList"});
        bizMapper.put("HSY001014", new String[]{"hsyShopService","insertBranchShop","保存店铺信息成功"});
        bizMapper.put("HSY001015", new String[]{"hsyShopService","findContractInfo"});
        bizMapper.put("HSY001016", new String[]{"hsyUserService","insertTokenDeviceClientInfoAndReturnKey"});
        bizMapper.put("HSY001017", new String[]{"hsyUserService","updateClientID"});
        bizMapper.put("HSY001018", new String[]{"hsyUserService","logout","退出成功"});
        bizMapper.put("HSY001019", new String[]{"hsyUserService","inserHsyUserViaCorporation","保存店员信息成功"});
        bizMapper.put("HSY001020", new String[]{"hsyQrCodeService","bindQrCode","绑定成功"});
        bizMapper.put("HSY001021", new String[]{"hsyUserService","findHsyUserViaCorporation"});
        bizMapper.put("HSY001022", new String[]{"hsyUserService","findHsyUserListViaCorporation"});
        bizMapper.put("HSY001023", new String[]{"hsyUserService","updateHsyUserViaCorporation","修改店员信息成功"});
        bizMapper.put("HSY001024", new String[]{"hsyUserService","updateHsyShopUserViaCorporation","分配店铺成功"});
        bizMapper.put("HSY001025", new String[]{"hsyUserService","updateHsyUserStatusViaCorporation","修改状态成功"});
        bizMapper.put("HSY001026", new String[]{"hsyAppVersionService","getAppVersion"});
        bizMapper.put("HSY001027", new String[]{"hsyAppVersionService","getAppVersionAndroid"});
        bizMapper.put("HSY001028", new String[]{"hsyUserService","findLoginInfo"});
        bizMapper.put("HSY001029", new String[]{"hsyUserService","findLoginInfoShort"});
        /**app收款*/
        bizMapper.put("HSY001030", new String[]{"hsyTradeService", "appReceipt"});
        /**app提现*/
        bizMapper.put("HSY001031", new String[]{"hsyTradeService", "appWithdraw"});
        /**app获取提现页面信息*/
        bizMapper.put("HSY001032", new String[]{"hsyTradeService", "getWithdrawInfo"});
        /** 账户余额信息*/
        bizMapper.put("HSY001033", new String[]{"hsyAccountService", "getAccount"});
        /**获取提现验证码*/
        bizMapper.put("HSY001034", new String[]{"hsyAccountService", "getVerifyCode"});
        /**结算记录*/
        bizMapper.put("HSY001035", new String[]{"accountSettleAuditRecordService", "appSettleRecordList"});
        /**交易记录-version1*/
        bizMapper.put("HSY001036", new String[]{"hsyTradeService", "tradeList"});
        bizMapper.put("HSY001037", new String[]{"hsyAppVersionService","findVersionDetailByVersionCode"});
        /**结算详情*/
        bizMapper.put("HSY001038", new String[]{"accountSettleAuditRecordService", "appSettleRecordDetail"});
        /**结算记录对应的交易列表*/
        bizMapper.put("HSY001039", new String[]{"accountSettleAuditRecordService", "appGetOrderListByRecordId"});
        bizMapper.put("HSY001040", new String[]{"hsyAppVersionService","findAllPageComponent"});
        bizMapper.put("HSY001041", new String[]{"hsyShopService","findBankBranchList"});
        bizMapper.put("HSY001042", new String[]{"hsyShopService","findBankList"});
        /**
         * 公告列表
         */
        bizMapper.put("HSY001043", new String[]{"HsyNoticeService","noticeList"});
        /**退款*/
        bizMapper.put("HSY001044", new String[]{"hsyTradeService", "appRefund1o6"});
        /**交易详情*/
        bizMapper.put("HSY001045", new String[]{"HsyOrderService", "appOrderDetail"});
        /**交易记录-version1.5*/
        bizMapper.put("HSY001046", new String[]{"HsyOrderService", "orderListst"});
        /**创建会员卡*/
        bizMapper.put("HSY001047", new String[]{"hsyMembershipService", "insertMemshipCard"});
        /**刷新登录*/
        bizMapper.put("HSY001048", new String[]{"hsyUserService","refreshlogin","刷新登录"});
        /**好收银店铺对账邮件保存*/
        bizMapper.put("HSY001049", new String[]{"hsyShopService","updateHsyShopEmail"});
        /**首页广告*/
        bizMapper.put("HSY001050", new String[]{"AdvertisementService","getvalidList","首页广告"});
        /**查询会员卡*/
        bizMapper.put("HSY001051", new String[]{"hsyMembershipService","findMemshipCards"});
        /**查询会员卡开卡二维码*/
        bizMapper.put("HSY001052", new String[]{"hsyMembershipService","findMemberQr"});
        /**查询会员卡详情*/
        bizMapper.put("HSY001053", new String[]{"hsyMembershipService","findMemshipCardsInfo"});
        /**停止会员卡*/
        bizMapper.put("HSY001054", new String[]{"hsyMembershipService","updateMemshipCardsStatus"});

    }
}
