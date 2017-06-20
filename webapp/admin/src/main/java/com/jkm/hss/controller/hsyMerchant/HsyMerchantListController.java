package com.jkm.hss.controller.hsyMerchant;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.Enum.EnumIsOpen;
import com.jkm.hsy.user.Enum.EnumOpt;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.dao.HsyCmbcDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.help.requestparam.*;
import com.jkm.hsy.user.service.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/1/20.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/hsyMerchantList")
public class HsyMerchantListController extends BaseController {

    @Autowired
    private HsyMerchantAuditService hsyMerchantAuditService;
    @Autowired
    private OSSClient ossClient;
    @Autowired
    private UserTradeRateService userTradeRateService;
    @Autowired
    private UserWithdrawRateService userWithdrawRateService;
    @Autowired
    private UserCurrentChannelPolicyService userCurrentChannelPolicyService;
    @Autowired
    private UserChannelPolicyService userChannelPolicyService;
    @Autowired
    private HsyCmbcDao hsyCmbcDao;
    @Autowired
    private HsyShopService hsyShopService;
    @Autowired
    private HsyCmbcService hsyCmbcService;


    @ResponseBody
    @RequestMapping(value = "/getMerchantList",method = RequestMethod.POST)
    public CommonResponse getMerchantList(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest) throws ParseException {
        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(hsyMerchantAuditRequest.getEndTime()!=null&&!"".equals(hsyMerchantAuditRequest.getEndTime())){
            Date dt = sdf.parse(hsyMerchantAuditRequest.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            hsyMerchantAuditRequest.setEndTime(sdf.format(rightNow.getTime()));
        }
        if(hsyMerchantAuditRequest.getAuditTime1()!=null&&!"".equals(hsyMerchantAuditRequest.getAuditTime1())){
            Date dt = sdf.parse(hsyMerchantAuditRequest.getAuditTime1());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            hsyMerchantAuditRequest.setAuditTime1(sdf.format(rightNow.getTime()));
        }
        List<HsyMerchantAuditResponse> list = hsyMerchantAuditService.getMerchant(hsyMerchantAuditRequest);
        int count = hsyMerchantAuditService.getCount(hsyMerchantAuditRequest);
        if (list == null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        pageModel.setCount(count);
        pageModel.setRecords(list);
        String downLoadHsyMerchant = downLoadHsyMerchant(hsyMerchantAuditRequest);
        pageModel.setExt(downLoadHsyMerchant);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    /**
     * 导出全部
     * @return
     */
    private String downLoadHsyMerchant(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest) throws ParseException {
        final String fileZip = this.hsyMerchantAuditService.downLoadHsyMerchant(hsyMerchantAuditRequest, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "hsyMerchant.xls";
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url = null;
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
            return url.getHost() + url.getFile();
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/getDetails",method = RequestMethod.POST)
    public JSONObject getDetails(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){


        JSONObject jsonObject = new JSONObject();
        HsyMerchantAuditResponse res = hsyMerchantAuditService.getDetails(hsyMerchantAuditRequest.getId());
        Date expiration = new Date(new Date().getTime() + 30*60*1000);
        if(res!=null){
            final String photoName = res.getLicenceID();
            final String photoName1 = res.getStorefrontID();
            final String photoName2 = res.getCounterID();
            final String photoName3 = res.getIndoorID();
            final String photoName4 = res.getIdcardf();
            final String photoName5 = res.getIdcardb();
            final String photoName6 = res.getIdcardc();
            final String photoName7 = res.getContractId();
            if (photoName!=null&&!"".equals(res.getLicenceID())) {
                URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, expiration);
                String urls =url.toString();
                res.setLicenceID(urls);
            }
            if (photoName1!=null&&!"".equals(res.getStorefrontID())) {
                URL url1 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName1,expiration);
                String urls1 =url1.toString();
                res.setStorefrontID(urls1);
            }
            if (photoName2!=null&&!"".equals(res.getCounterID())) {
                URL url2 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName2,expiration);
                String urls2 =url2.toString();
                res.setCounterID(urls2);
            }
            if (photoName3!=null&&!"".equals(res.getIndoorID())) {
                URL url3 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName3,expiration);
                String urls3 =url3.toString();
                res.setIndoorID(urls3);
            }
            if (photoName4!=null&&!"".equals(res.getIdcardf())) {
                URL url4 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName4,expiration);
                String urls4 =url4.toString();
                res.setIdcardf(urls4);
            }
            if (photoName5!=null&&!"".equals(res.getIdcardb())) {
                URL url5 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName5,expiration);
                String urls5 =url5.toString();
                res.setIdcardb(urls5);
            }
            if (photoName6!=null&&!"".equals(res.getIdcardc())) {
                URL url6 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName6,expiration);
                String urls6 =url6.toString();
                res.setIdcardc(urls6);
            }
            if (photoName7!=null&&!"".equals(res.getContractId())) {
                URL url7 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName7,expiration);
                String urls7 =url7.toString();
                res.setContractId(urls7);
            }

        }
        List<HsyMerchantInfoCheckRecord> list = hsyMerchantAuditService.getLog(hsyMerchantAuditRequest.getId());
        jsonObject.put("code",1);
        jsonObject.put("msg","success");
        JSONObject jo = new JSONObject();
        jo.put("list",list);
        jo.put("res",res);
        List<UserTradeRateListResponse> userTradeRateListResponses = userTradeRateService.getUserRate(res.getUid());
        jo.put("rateList",userTradeRateListResponses);
        Optional<UserCurrentChannelPolicy> userCurrentChannelPolicyOptional = userCurrentChannelPolicyService.selectByUserId(res.getUid());
        if(userCurrentChannelPolicyOptional.isPresent()){
            UserChannelListResponse userChannelListResponse = userChannelPolicyService.getCurrentChannelName(res.getUid());
            jo.put("userChannelList",userChannelListResponse);
        }else{
            UserChannelListResponse userChannelListResponse = new UserChannelListResponse();
            jo.put("userChannelList",userChannelListResponse);
        }
        List<UserChannelPolicyResponse> userChannelPolicyResponses = userChannelPolicyService.getUserChannelList(res.getUid());
        jo.put("channelList",userChannelPolicyResponses);
        jsonObject.put("result",jo);
        return jsonObject;
    }


    /**
     * 更改注册手机号
     * @param hsyMerchantAuditRequest
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/changeMobile",method = RequestMethod.POST)
    public CommonResponse changeMobile(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest) throws ParseException {
        HsyMerchantAuditResponse req = this.hsyMerchantAuditService.getCellphon(hsyMerchantAuditRequest.getId());
//        if (req.getChangePhone()==null) {
//            this.hsyMerchantAuditService.changeMobile(req.getUid(),hsyMerchantAuditRequest.getChangePhone());
//        }else {
//            this.hsyMerchantAuditService.updatePhone(req.getChangePhone(),req.getUid());
//            this.hsyMerchantAuditService.changeMobile(req.getUid(),hsyMerchantAuditRequest.getChangePhone());
//        }
        this.hsyMerchantAuditService.changeMobile(req.getUid(),hsyMerchantAuditRequest.getChangePhone());
        return CommonResponse.simpleResponse(1, "更改成功");

    }




    @ResponseBody
    @RequestMapping(value = "/getCheckPending",method = RequestMethod.POST)
    public CommonResponse getCheckPending(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
        List<HsyMerchantAuditResponse> list = hsyMerchantAuditService.getCheckPending(hsyMerchantAuditRequest);
        int count = hsyMerchantAuditService.getCheckPendingCount(hsyMerchantAuditRequest);
        if (list == null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    /**
     *修改费率
     * @param userTradeRateListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateRate",method = RequestMethod.POST)
    public CommonResponse updateRate(@RequestBody List<UserTradeRateListRequest> userTradeRateListRequest){
        if(userTradeRateListRequest.size()!=3){
            return CommonResponse.simpleResponse(-1, "请填写全参数");
        }
        for(int i=0;i<userTradeRateListRequest.size();i++){
            if(userTradeRateListRequest.get(i).getTradeRateT1()==null){
                return CommonResponse.simpleResponse(-1, EnumPolicyType.of(userTradeRateListRequest.get(i).getPolicyType()).getName()+"T1不能为空");
            }
            if(userTradeRateListRequest.get(i).getTradeRateD0()==null){
                return CommonResponse.simpleResponse(-1, EnumPolicyType.of(userTradeRateListRequest.get(i).getPolicyType()).getName()+"D0不能为空");
            }
            if(!(EnumPolicyType.WITHDRAW.getId()).equals(userTradeRateListRequest.get(i).getPolicyType())){
                Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userTradeRateListRequest.get(i).getUserId(),userTradeRateListRequest.get(i).getPolicyType());
                if(userTradeRateOptional.isPresent()){
                    UserTradeRate userTradeRate = new UserTradeRate();
                    userTradeRate.setId(userTradeRateOptional.get().getId());
                    userTradeRate.setUserId(userTradeRateListRequest.get(i).getUserId());
                    userTradeRate.setPolicyType(userTradeRateListRequest.get(i).getPolicyType());
                    if(userTradeRateListRequest.get(i).getTradeRateT1()!=null&&!"".equals(userTradeRateListRequest.get(i).getTradeRateT1())){
                        userTradeRate.setTradeRateT1(userTradeRateListRequest.get(i).getTradeRateT1().divide(new BigDecimal("100")));
                    }
                    if(userTradeRateListRequest.get(i).getTradeRateD1()!=null&&!"".equals(userTradeRateListRequest.get(i).getTradeRateD1())){
                        userTradeRate.setTradeRateD1(userTradeRateListRequest.get(i).getTradeRateD1().divide(new BigDecimal("100")));
                    }
                    if(userTradeRateListRequest.get(i).getTradeRateD0()!=null && !"".equals(userTradeRateListRequest.get(i).getTradeRateD0())){
                        userTradeRate.setTradeRateD0(userTradeRateListRequest.get(i).getTradeRateD0().divide(new BigDecimal("100")));
                    }
                    userTradeRate.setIsOpen(EnumIsOpen.OPEN.getId());
                    userTradeRate.setStatus(EnumStatus.NORMAL.getId());
                    userTradeRateService.update(userTradeRate);
                }else{
                    UserTradeRate userTradeRate = new UserTradeRate();
                    userTradeRate.setUserId(userTradeRateListRequest.get(i).getUserId());
                    userTradeRate.setPolicyType(userTradeRateListRequest.get(i).getPolicyType());
                    if(userTradeRateListRequest.get(i).getTradeRateT1()!=null&&!"".equals(userTradeRateListRequest.get(i).getTradeRateT1())){
                        userTradeRate.setTradeRateT1(userTradeRateListRequest.get(i).getTradeRateT1().divide(new BigDecimal("100")));
                    }
                    if(userTradeRateListRequest.get(i).getTradeRateD1()!=null&&!"".equals(userTradeRateListRequest.get(i).getTradeRateD1())){
                        userTradeRate.setTradeRateD1(userTradeRateListRequest.get(i).getTradeRateD1().divide(new BigDecimal("100")));
                    }
                    if(userTradeRateListRequest.get(i).getTradeRateD0()!=null && !"".equals(userTradeRateListRequest.get(i).getTradeRateD0())){
                        userTradeRate.setTradeRateD0(userTradeRateListRequest.get(i).getTradeRateD0().divide(new BigDecimal("100")));
                    }
                    userTradeRate.setIsOpen(EnumIsOpen.OPEN.getId());
                    userTradeRate.setStatus(EnumStatus.NORMAL.getId());
                    userTradeRateService.insert(userTradeRate);
                }
            }else{
                Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userTradeRateListRequest.get(i).getUserId());
                if(userWithdrawRateOptional.isPresent()){
                    UserWithdrawRate userWithdrawRate = new UserWithdrawRate();
                    userWithdrawRate.setId(userWithdrawRateOptional.get().getId());
                    userWithdrawRate.setUserId(userTradeRateListRequest.get(i).getUserId());
                    userWithdrawRate.setWithdrawRateT1(userTradeRateListRequest.get(i).getTradeRateT1());
                    userWithdrawRate.setWithdrawRateD1(userTradeRateListRequest.get(i).getTradeRateD1());
                    userWithdrawRate.setWithdrawRateD0(userTradeRateListRequest.get(i).getTradeRateD0());
                    userWithdrawRate.setStatus(EnumStatus.NORMAL.getId());
                    userWithdrawRateService.update(userWithdrawRate);
                }else{
                    UserWithdrawRate userWithdrawRate = new UserWithdrawRate();
                    userWithdrawRate.setUserId(userTradeRateListRequest.get(i).getUserId());
                    userWithdrawRate.setWithdrawRateT1(userTradeRateListRequest.get(i).getTradeRateT1());
                    userWithdrawRate.setWithdrawRateD1(userTradeRateListRequest.get(i).getTradeRateD1());
                    userWithdrawRate.setWithdrawRateD0(userTradeRateListRequest.get(i).getTradeRateD0());
                    userWithdrawRate.setStatus(EnumStatus.NORMAL.getId());
                    userWithdrawRateService.insert(userWithdrawRate);
                }
            }
        }
        boolean b = hsyCmbcService.merchantInfoModify(userTradeRateListRequest.get(0).getUserId(),userTradeRateListRequest.get(0).getShopId(),super.getAdminUser().getId(), EnumOpt.MODIFYRATES.getMsg());
        if (b==false){
            return CommonResponse.simpleResponse(-1, "修改上游银行卡失败，请务必联系技术解决！！");
        }
        return CommonResponse.simpleResponse(1, "修改成功");
    }
    /**
     *查询成功通道列表
     * @param useChannelRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/useChannel",method = RequestMethod.POST)
    public CommonResponse useChannel(@RequestBody UseChannelRequest useChannelRequest){
        List<UserChannelPolicyUseResponse> userChannelPolicyUseResponses = userChannelPolicyService.getUserChannelByUserIdAndPolicyType(useChannelRequest.getUserId(),useChannelRequest.getPolicyType());
        return CommonResponse.objectResponse(1, "查询成功",userChannelPolicyUseResponses);
    }
    /**
     *修改当前使用通道
     * @param userCurrentChannelPolicy
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeUseChannel",method = RequestMethod.POST)
    public CommonResponse changeUseChannel(@RequestBody UserCurrentChannelPolicy userCurrentChannelPolicy){
        if(userCurrentChannelPolicy.getWechatChannelTypeSign()== EnumPayChannelSign.XMMS_WECHAT_D0.getId()){
            return CommonResponse.simpleResponse(-1, "暂不支持民生微信D0通道");
        }
        if(userCurrentChannelPolicy.getWechatChannelTypeSign()== EnumPayChannelSign.XMMS_ALIPAY_D0.getId()){
            return CommonResponse.simpleResponse(-1, "暂不支持民生支付宝D0通道");
        }
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userCurrentChannelPolicy.getUserId());
        if(appAuUser==null){
            return CommonResponse.simpleResponse(-1, "商户不存在");
        }
        userCurrentChannelPolicyService.updateByUserId(userCurrentChannelPolicy);
        return CommonResponse.simpleResponse(1, "修改成功");
    }

    @ResponseBody
    @RequestMapping(value = "/changeSettlementCard",method = RequestMethod.POST)
    public CommonResponse changeSettlementCard(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        if ("".equals(hsyMerchantAuditRequest.getCardNo())&&hsyMerchantAuditRequest.getCardNo()==null){
            return CommonResponse.simpleResponse(-1, "账号不能为空");
        }
        if ("".equals(hsyMerchantAuditRequest.getBankName())&&hsyMerchantAuditRequest.getBankName()==null){
            return CommonResponse.simpleResponse(-1, "开户行不能为空");
        }
        if ("".equals(hsyMerchantAuditRequest.getDistrictCode())&&hsyMerchantAuditRequest.getDistrictCode()==null){
            return CommonResponse.simpleResponse(-1, "省市不能为空");
        }
        if ("".equals(hsyMerchantAuditRequest.getBankAddress())&&hsyMerchantAuditRequest.getBankAddress()==null){
            return CommonResponse.simpleResponse(-1, "支行不能为空");
        }

        long userId = hsyMerchantAuditService.getUid(hsyMerchantAuditRequest.getId());

        this.hsyShopService.changeSettlementCard(hsyMerchantAuditRequest.getCardNo(),hsyMerchantAuditRequest.getBankName(),
                hsyMerchantAuditRequest.getDistrictCode(),hsyMerchantAuditRequest.getBankAddress(),hsyMerchantAuditRequest.getId());

        boolean b = hsyCmbcService.merchantInfoModify(userId, hsyMerchantAuditRequest.getId(),super.getAdminUser().getId(), EnumOpt.MODIFYDEFAULTCARD.getMsg());
        if (b==false){
            return CommonResponse.simpleResponse(-1, "修改上游银行卡失败，请务必联系技术解决！！");
        }

        return CommonResponse.simpleResponse(1, "修改成功");
    }

    /**
     * 查询开户行列表BOSS后台对公
     */
    @ResponseBody
    @RequestMapping(value = "/getBankNameList",method = RequestMethod.POST)
    public CommonResponse getBankNameList(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        List<AppBizBankBranch> list = this.hsyShopService.getBankNameList(hsyMerchantAuditRequest.getBankName());
        return CommonResponse.objectResponse(1,"SUCCESS",list);
    }

    /**
     * 查询开户行列表BOSS后台对私
     */
    @ResponseBody
    @RequestMapping(value = "/getPersonalBankNameList",method = RequestMethod.POST)
    public CommonResponse getPersonalBankNameList(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        if ("".equals(hsyMerchantAuditRequest.getCardNo())&&hsyMerchantAuditRequest.getCardNo()==null){
            return CommonResponse.simpleResponse(-1, "账号不能为空");
        }
        JSONObject jsonObject = new JSONObject();

        String list = this.hsyShopService.getPersonalBankNameList(hsyMerchantAuditRequest.getCardNo());
        jsonObject.put("bankName",list);
        List list1 = new ArrayList();
        list1.add(jsonObject);
        return CommonResponse.objectResponse(1,"SUCCESS",list1);
    }
}
