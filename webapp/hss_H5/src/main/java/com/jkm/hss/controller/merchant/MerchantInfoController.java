package com.jkm.hss.controller.merchant;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import com.jkm.hss.dealer.service.PartnerShallProfitDetailService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.PartnerShallRequest;
import com.jkm.hss.helper.response.PartnerShallResponse;
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
import org.apache.commons.lang3.RandomStringUtils;
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
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private PartnerShallProfitDetailService partnerShallProfitDetailService;

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse save(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final MerchantInfoAddRequest merchantInfo){
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
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
        Date date = new Date();
        long nousedate =  date.getTime();
        String photoName = "hss/"+  nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +".jpg";
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream,meta);
            merchantInfo.setReserveMobile(MerchantSupport.encryptMobile(merchantInfo.getReserveMobile()));
            final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankNo);
            merchantInfo.setBankBin(bankCardBinOptional.get().getShorthand());
            merchantInfo.setBankName(bankCardBinOptional.get().getBankName());
            merchantInfo.setBankNoShort(merchantInfo.getBankNo().substring((merchantInfo.getBankNo()).length()-4,(merchantInfo.getBankNo()).length()));
            merchantInfo.setBankNo(MerchantSupport.encryptBankCard(merchantInfo.getBankNo()));
            merchantInfo.setBankPic(photoName);
        }catch (Exception e){
            log.debug("上传文件失败",e);
            return CommonResponse.simpleResponse(-1, "图片上传失败");
        }
//        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream,meta);
//        merchantInfo.setReserveMobile(MerchantSupport.encryptMobile(merchantInfo.getReserveMobile()));
//        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankNo);
//        merchantInfo.setBankBin(bankCardBinOptional.get().getShorthand());
//        merchantInfo.setBankName(bankCardBinOptional.get().getBankName());
//        merchantInfo.setBankNoShort(merchantInfo.getBankNo().substring((merchantInfo.getBankNo()).length()-4,(merchantInfo.getBankNo()).length()));
//        merchantInfo.setBankNo(MerchantSupport.encryptBankCard(merchantInfo.getBankNo()));
//        merchantInfo.setBankPic(photoName);
        int res = this.merchantInfoService.update(merchantInfo);
        if (res<=0) {
            return CommonResponse.simpleResponse(-1, "资料添加失败");
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "资料添加成功");

    }


    @ResponseBody
    @RequestMapping(value = "/savePic", method = RequestMethod.POST)
    public CommonResponse savePic(final HttpServletRequest request, final HttpServletResponse response,@RequestBody final MerchantInfoAddRequest merchantInfo){
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
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
        Date date = new Date();
        long nousedate =  date.getTime();
        String photoName = "hss/"+  nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +".jpg";
        String photoName1 = "hss/"+ nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +".jpg";
        String photoName2 = "hss/"+ nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +".jpg";
        String photoName3 = "hss/"+ nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +".jpg";
//        String photoName = "hsy/"+  nowDate + "/" + SnGenerator.generate("",5) +".jpg";
//        String photoName1 = "hsy/"+ nowDate + "/" + SnGenerator.generate("",5) +".jpg";
//        String photoName2 = "hsy/"+ nowDate + "/" + SnGenerator.generate("",5) +".jpg";
//        String photoName3 = "hsy/"+ nowDate + "/" + SnGenerator.generate("",5) +".jpg";
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream, meta);
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName1, inputStream1, meta);
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName2, inputStream2, meta);
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName3, inputStream3, meta);
            merchantInfo.setIdentityFacePic(photoName);
            merchantInfo.setIdentityHandPic(photoName1);
            merchantInfo.setIdentityOppositePic(photoName2);
            merchantInfo.setBankHandPic(photoName3);
        }catch (Exception e){
            log.debug("上传文件失败",e);
            return CommonResponse.simpleResponse(-1, "图片上传失败");
        }
//        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream, meta);
//        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName1, inputStream1, meta);
//        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName2, inputStream2, meta);
//        ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName3, inputStream3, meta);
//        merchantInfo.setIdentityFacePic(photoName);
//        merchantInfo.setIdentityHandPic(photoName1);
//        merchantInfo.setIdentityOppositePic(photoName2);
//        merchantInfo.setBankHandPic(photoName3);
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
        final Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
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

    @ResponseBody
    @RequestMapping(value = "/queryShall", method = RequestMethod.POST)
    public CommonResponse queryShall(final HttpServletRequest request, @RequestBody final PartnerShallRequest shallRequest){

        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getStatus()!=EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!=EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }

        shallRequest.setMerchantId(merchantInfo.get().getId());
        PageModel<PartnerShallProfitDetail> pageModel = this.partnerShallProfitDetailService.
                getPartnerShallProfitList(shallRequest.getMerchantId(), shallRequest.getShallId(),shallRequest.getPageSize());
        final BigDecimal totalProfit = this.partnerShallProfitDetailService.selectTotalProfitByMerchantId(shallRequest.getMerchantId());

        final List<PartnerShallProfitDetail> records = pageModel.getRecords();

        List<JSONObject> list = Lists.transform(records, new Function<PartnerShallProfitDetail, JSONObject>() {
            @Override
            public JSONObject apply(PartnerShallProfitDetail input) {
                JSONObject jsonObject = new JSONObject();
                if (input.getFirstMerchantId() == shallRequest.getMerchantId()){
                    jsonObject.put("type","1");
                    jsonObject.put("name",input.getMerchantName());
                    jsonObject.put("date", input.getCreateTime());
                    jsonObject.put("money", input.getFirstMerchantShallAmount());
                    jsonObject.put("shallId", input.getId());
                }else{
                    jsonObject.put("type","2");
                    jsonObject.put("name",getInDirectName(input.getMerchantName()));
                    jsonObject.put("date", input.getCreateTime());
                    jsonObject.put("money", input.getSecondMerchantShallAmount());
                    jsonObject.put("shallId", input.getId());
                }

                return jsonObject;
            }
        });
        PageModel<JSONObject> model = new PageModel<>(shallRequest.getPageNo(),shallRequest.getPageSize());
        model.setRecords(list);
        model.setCount(pageModel.getCount());
        model.setHasNextPage(pageModel.isHasNextPage());
        model.setPageSize(pageModel.getPageSize());
        PartnerShallResponse response = new PartnerShallResponse();
        response.setPageModel(model);
        response.setTotalShall(String.valueOf(totalProfit));

        return CommonResponse.objectResponse(1,"success", response);
    }


    private String getInDirectName(String name){
        final int length = name.length();
        if (length <= 2){
            return "*" + name.charAt(1);
        }else if (length == 3){

            return "**" + name.charAt(2);
        }
          return "**" + name.charAt(length - 1);
    }
}
