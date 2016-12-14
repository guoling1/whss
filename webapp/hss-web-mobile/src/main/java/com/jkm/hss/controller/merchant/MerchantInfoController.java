package com.jkm.hss.controller.merchant;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.merchant.helper.request.MerchantInfoAddRequest;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.merchant.service.VerifyIdService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/merchantInfo")
public class MerchantInfoController extends BaseController {

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private BankCardBinService bankCardBinService;

    @Autowired
    private SmsAuthService smsAuthService;

    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private VerifyIdService verifyIdService;


    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse save(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final MerchantInfoAddRequest merchantInfo){
        Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
        merchantInfo.setId(userInfoOptional.get().getMerchantId());
//        merchantInfo.setId(49);
        merchantInfo.setIdentity(MerchantSupport.encryptIdenrity(merchantInfo.getIdentity()));
        merchantInfo.setStatus(EnumMerchantStatus.ONESTEP.getId());
        final String reserveMobile = merchantInfo.getReserveMobile();
        final String verifyCode = merchantInfo.getCode();
        final String store = merchantInfo.getMerchantName();
        final String address = merchantInfo.getAddress();
        final String bankNo = merchantInfo.getBankNo();
        final String bankPic = merchantInfo.getBankPic();
        final String name = merchantInfo.getName();
        final String identity =merchantInfo.getIdentity();
        if (StringUtils.isBlank(store)) {
            return CommonResponse.simpleResponse(-1, "店铺名不能为空");
        }
        if (StringUtils.isBlank(address)) {
            return CommonResponse.simpleResponse(-1, "地址不能为空");
        }
        if (StringUtils.isBlank(bankNo)) {
            return CommonResponse.simpleResponse(-1, "结算卡号不能为空");
        }
        if (StringUtils.isBlank(bankPic)) {
            return CommonResponse.simpleResponse(-1, "上传照片不能为空");
        }
        if (StringUtils.isBlank(name)) {
            return CommonResponse.simpleResponse(-1, "开户名不能为空");
        }
        if (StringUtils.isBlank(identity)) {
            return CommonResponse.simpleResponse(-1, "身份证号不能为空");
        }
        if (StringUtils.isBlank(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "验证码不能为空");
        }
        if (!ValidateUtils.isMobile(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (!ValidateUtils.verifyCodeCheck(verifyCode)) {
            return CommonResponse.simpleResponse(-1, "请输入正确的6位数字验证码");
        }
       final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(reserveMobile, verifyCode, EnumVerificationCodeType.BIND_CARD_MERCHANT);
        if (1 != checkResult.getLeft()) {
            return CommonResponse.simpleResponse(-1, checkResult.getRight());
        }
        InputStream inputStream = WxPubUtil.getInputStream(merchantInfo.getBankPic());
        if (inputStream ==null){

            return CommonResponse.simpleResponse(-1, "获取图片失败");

        }
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/octet-stream ");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        SimpleDateFormat sdf1 =   new SimpleDateFormat("yyyyMMddHHmmss");
        String nowDate1 = sdf1.format(new Date());
        String photoName ="hsy/"+ nowDate + "/" + nowDate1 + ".jpg";
        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream,meta);
//        Date expiration = new Date(new Date().getTime() + 30*60*1000);
//        URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
//        String urls = url.getPath();
//        String urlss = urls.substring(1);
        merchantInfo.setReserveMobile(MerchantSupport.encryptMobile(merchantInfo.getReserveMobile()));
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankNo);
        merchantInfo.setBankBin(bankCardBinOptional.get().getShorthand());
        merchantInfo.setBankName(bankCardBinOptional.get().getBankName());
        merchantInfo.setBankNoShort(merchantInfo.getBankNo().substring((merchantInfo.getBankNo()).length()-4,(merchantInfo.getBankNo()).length()));
        merchantInfo.setBankNo(MerchantSupport.encryptBankCard(merchantInfo.getBankNo()));
        merchantInfo.setBankPic(photoName);
        int res = this.merchantInfoService.update(merchantInfo);
        if (res<=0) {
            return CommonResponse.simpleResponse(-1, "资料添加失败");
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "资料添加成功");

    }


    @ResponseBody
    @RequestMapping(value = "/savePic", method = RequestMethod.POST)
    public CommonResponse savePic(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final MerchantInfoAddRequest merchantInfo){
        Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
        merchantInfo.setId(userInfoOptional.get().getMerchantId());
        merchantInfo.setStatus(EnumMerchantStatus.REVIEW.getId());
        InputStream inputStream = WxPubUtil.getInputStream(merchantInfo.getIdentityFacePic());
        InputStream inputStream1 = WxPubUtil.getInputStream(merchantInfo.getIdentityOppositePic());
        InputStream inputStream2 = WxPubUtil.getInputStream(merchantInfo.getIdentityHandPic());
        InputStream inputStream3 = WxPubUtil.getInputStream(merchantInfo.getBankHandPic());
        if (inputStream==null || inputStream1==null || inputStream2==null ||inputStream3==null){
            return CommonResponse.simpleResponse(-1, "获取图片失败");
        }
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/octet-stream ");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        SimpleDateFormat sdf1 =   new SimpleDateFormat("yyyyMMddHHmmss");
        String nowDate1 = sdf1.format(new Date());
        String photoName = "hsy/"+  nowDate + "/" +  nowDate1 +".jpg";
        String photoName1 = "hsy/"+ nowDate + "/" + nowDate1 +".jpg";
        String photoName2 = "hsy/"+ nowDate + "/" + nowDate1 +".jpg";
        String photoName3 = "hsy/"+ nowDate + "/" + nowDate1 +".jpg";
        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream, meta);
        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName1, inputStream1, meta);
        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName2, inputStream2, meta);
        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName3, inputStream3, meta);
//        Date expiration = new Date(new Date().getTime() + 30*60*1000);
//        URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
//        URL url1 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName1,expiration);
//        URL url2 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName2,expiration);
//        URL url3 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName3,expiration);
//        String urls = url.getPath();
//        String urls1 = url1.getPath();
//        String urls2 = url2.getPath();
//        String urls3 = url3.getPath();
        merchantInfo.setIdentityFacePic(photoName);
        merchantInfo.setIdentityHandPic(photoName1);
        merchantInfo.setIdentityOppositePic(photoName2);
        merchantInfo.setBankHandPic(photoName3);
        this.merchantInfoService.updatePic(merchantInfo);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"照片添加成功");
    }

    @ResponseBody
    @RequestMapping(value = "/queryBank", method = RequestMethod.POST)
    public CommonResponse queryBank(@RequestBody final MerchantInfo merchantInfo){

        String bankNo = merchantInfo.getBankNo();
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankNo);


        return CommonResponse.objectResponse(1,"success",bankCardBinOptional.get().getCardTypeCode());
    }



    @ResponseBody
    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
    public CommonResponse sendVerifyCode(@RequestBody final MerchantInfo merchantInfo, final HttpServletRequest request){
        final Optional<UserInfo> userInfoOptional = userInfoService.selectById(super.getUserId(request));
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        final String reserveMobile = merchantInfo.getReserveMobile();
        if (StringUtils.isBlank(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(reserveMobile)) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        //校验身份4要素
        final Pair<Integer, String> verifyPair =
                this.verifyID4Element(MerchantSupport.decryptMobile(merchantInfoOptional.get().getMobile()), merchantInfo);
        if (0 != verifyPair.getLeft()) {
            return CommonResponse.simpleResponse(-1, verifyPair.getRight());
        }

        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(reserveMobile, EnumVerificationCodeType.BIND_CARD_MERCHANT);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(reserveMobile)
                    .uid(merchantInfo.getId() + "")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.BIND_CARD_MERCHANT)
                    .build()
            );
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "发送验证码成功");
        }
        return CommonResponse.simpleResponse(-1, verifyCode.getRight());
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public void test1(){
        InputStream inputStream = WxPubUtil.getInputStream("R4DKmoNFwJZPdRvTTz5d39A8Lri-4kAsmXLc2jJfg1y2Cy6cfVI84dJtV0qEIi4v");
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("image/*");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String photoName =  nowDate + "/" + ".jpeg";
        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream, meta);
        Date expiration = new Date(new Date().getTime() + 30*60*1000);
        URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
        String urls = url.getHost() + url.getPath();
        }


    /**
     * 获取随机文件名
     *
     * @param originalFilename
     * @return
     */
    private String getFileName(final String originalFilename) {
        final String dateFileName = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMdd);
        final String extName = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss)+".jpg";
        return "hsy/" + dateFileName + "/" + extName;
    }

    /**
     * 校验身份4要素
     *
     * @return
     */
    private Pair<Integer, String> verifyID4Element(final String mobile, final MerchantInfo merchantInfo) {
        final String bankcard = merchantInfo.getBankNo();
        final String idCard = merchantInfo.getIdentity();
        final String bankReserveMobile = merchantInfo.getReserveMobile();
        final String realName = merchantInfo.getName();
        final Pair<Integer, String> pair = this.verifyIdService.verifyID(mobile, bankcard, idCard, bankReserveMobile, realName);
        return pair;
    }
}
