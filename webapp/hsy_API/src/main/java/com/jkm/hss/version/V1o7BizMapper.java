package com.jkm.hss.version;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 2017/6/20.
 */
public class V1o7BizMapper {
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
        bizMapper.put("HSY001031", new String[]{"hsyTradeService", "withdraw"});
        /**app获取提现页面信息*/
        bizMapper.put("HSY001032", new String[]{"hsyTradeService", "getWithdrawInfo"});
        /** 账户余额信息*/
        bizMapper.put("HSY001033", new String[]{"hsyTradeService", "getAccount"});
        /**提现记录*/
        bizMapper.put("HSY001034", new String[]{"hsyTradeService", "withdrawOrderList"});
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
        bizMapper.put("HSY001046", new String[]{"HsyOrderService", "orderList"});
        /**创建会员卡*/
        bizMapper.put("HSY001047", new String[]{"hsyMembershipService", "insertMembershipCard"});
        /**刷新登录*/
        bizMapper.put("HSY001048", new String[]{"hsyUserService","refreshlogin","刷新登录"});
        /**好收银店铺对账邮件保存*/
        bizMapper.put("HSY001049", new String[]{"hsyShopService","updateHsyShopEmail"});
        /**首页广告*/
        bizMapper.put("HSY001050", new String[]{"AdvertisementService","getvalidList","首页广告"});
        /**查询会员卡*/
        bizMapper.put("HSY001051", new String[]{"hsyMembershipService","findMembershipCards"});
        /**查询会员卡开卡二维码*/
        bizMapper.put("HSY001052", new String[]{"hsyMembershipService","findMemberQr"});
        /**查询会员卡详情*/
        bizMapper.put("HSY001053", new String[]{"hsyMembershipService","findMembershipCardsInfo"});
        /**停止(启用)开通会员卡*/
        bizMapper.put("HSY001054", new String[]{"hsyMembershipService","updateMembershipCardsStatus"});
        /**修改会员卡*/
        bizMapper.put("HSY001055", new String[]{"hsyMembershipService","updateMembershipCard"});
        /**更改协议查看状态*/
        bizMapper.put("HSY001056", new String[]{"hsyUserService","updateProtocolSeenStatus"});
        /**主扫创建订单*/
        bizMapper.put("HSY001057", new String[]{"hsyOrderScanService","insertHsyOrder"});
        /**更新订单并支付*/
        bizMapper.put("HSY001058", new String[]{"hsyOrderScanService","updateHsyOrderPay"});
        /**查找订单信息*/
        bizMapper.put("HSY001059", new String[]{"hsyOrderScanService","findHsyOrderRelateInfo"});


        bizMapper.put("HSY001060", new String[]{"hsyBalanceAccountEmailService","updateAutoSendBalanceAccountEmail"});
        bizMapper.put("HSY001061", new String[]{"hsyBalanceAccountEmailService","sendBalanceAccountEmail"});
        bizMapper.put("HSY001062", new String[]{"hsyBalanceAccountEmailService","checkAutoSendBalanceAccountEmail"});
        /**修改密码*/
        bizMapper.put("HSY001063", new String[]{"hsyUserService","updatePassword"});
        /**发送语音验证码*/
        bizMapper.put("HSY001064", new String[]{"hsyUserService","insertAndSendVoiceVerificationCode"});
        /**开启停用提示音*/
        bizMapper.put("HSY001065", new String[]{"hsyUserService","updateIsAvoidingTone"});
        /**查找会员列表*/
        bizMapper.put("HSY001066", new String[]{"hsyMembershipService","findMemberList"});
        /**查找会员详情*/
        bizMapper.put("HSY001067", new String[]{"hsyMembershipService","findMemberInfo"});
        /**查找消费列表*/
        bizMapper.put("HSY001068", new String[]{"hsyOrderScanService","findConsumeOrderList"});
        /**查找消费具体信息*/
        bizMapper.put("HSY001069", new String[]{"hsyOrderScanService","findConsumeOrderInfo"});
        /**查找充值记录*/
        bizMapper.put("HSY001070", new String[]{"hsyMembershipService","findRechargeOrderList"});
        /**查找充值记录详情*/
        bizMapper.put("HSY001071", new String[]{"hsyMembershipService","findRechargeOrderInfo"});
        /**查找统计值*/
        bizMapper.put("HSY001072", new String[]{"hsyMembershipService","findMemberStatistic"});

    }
}
