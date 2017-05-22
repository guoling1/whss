package com.jkm.hss.controller.active;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.base.common.spring.alipay.service.AlipayOauthService;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.account.sevice.MemberAccountService;
import com.jkm.hss.account.sevice.ReceiptMemberMoneyAccountService;
import com.jkm.hss.entity.AuthInfo;
import com.jkm.hss.entity.AuthParam;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.helper.WxPubUtil;
import com.jkm.hss.notifier.dao.MessageTemplateDao;
import com.jkm.hss.notifier.dao.SendMessageRecordDao;
import com.jkm.hss.notifier.entity.SendMessageRecord;
import com.jkm.hss.notifier.entity.SmsTemplate;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.constant.AppPolicyConstant;
import com.jkm.hsy.user.constant.MemberStatus;
import com.jkm.hsy.user.constant.VerificationCodeType;
import com.jkm.hsy.user.entity.AppAuVerification;
import com.jkm.hsy.user.entity.AppPolicyConsumer;
import com.jkm.hsy.user.entity.AppPolicyMember;
import com.jkm.hsy.user.entity.AppPolicyMembershipCard;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyMembershipService;
import com.jkm.hsy.user.util.AppAesUtil;
import com.jkm.hsy.user.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2017/5/10.
 */
@Slf4j
@Controller
@RequestMapping(value = "/membership")
public class MembershipController {

    @Autowired
    private MessageTemplateDao messageTemplateDao;
    @Autowired
    private SendMessageRecordDao sendMessageRecordDao;
    @Autowired
    private AlipayOauthService alipayOauthService;
    @Autowired
    private HsyMembershipService hsyMembershipService;
    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private ReceiptMemberMoneyAccountService receiptMemberMoneyAccountService;

    @RequestMapping("getAuth/{uidEncode}")
    public String getAuth(HttpServletRequest request, HttpServletResponse response,Model model,@PathVariable String uidEncode){
        String agent = request.getHeader("User-Agent").toLowerCase();
        if (agent.indexOf("micromessenger") > -1){
            try {
                uidEncode=URLEncoder.encode(uidEncode,AppPolicyConstant.enc);
            }catch(Exception e){
                log.info("http转义失败");
                model.addAttribute("tips","转义失败,请稍后再试！");
                return "/tips";
            }
            return "redirect:"+ WxConstants.WEIXIN_HSY_MEMBERSHIP_AUTHINFO+uidEncode+"%7CWX"+ WxConstants.WEIXIN_USERINFO_REDIRECT;
        }
        if (agent.indexOf("aliapp") > -1) {
            return "redirect:"+ AlipayServiceConstants.OAUTH_URL+uidEncode+"%7CZFB"+AlipayServiceConstants.OAUTH_URL_LATER+AlipayServiceConstants.MEMBERSHIP_REDIRECT_URI;
        }
        model.addAttribute("tips","请使用微信或支付宝");
        return "/tips";
    }

    @RequestMapping("getAuthInfo")
    public String getAuthInfo(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, AuthParam authParam){
        log.info("回跳参数:"+authParam.toString());
        boolean successFlag=true;
        String info="";
        if(authParam.getState()!=null) {
            if (authParam.getState().endsWith("WX")) {
                Map<String,String> ret = WxPubUtil.getOpenid(authParam.getCode(), WxConstants.APP_HSY_ID,WxConstants.APP_HSY_SECRET);
                String openID=ret.get("openid");
                redirectAttributes.addAttribute("openID",openID);
                String[] str=authParam.getState().split("\\|");
                redirectAttributes.addAttribute("source",str[1]);
                try {
                    redirectAttributes.addAttribute("uidEncode", URLEncoder.encode(str[0], AppPolicyConstant.enc));
                }catch(Exception e){
                    log.info("http转义失败");
                    successFlag = false;
                    info = "转义失败,请稍后再试！";
                }
                if(openID==null||openID.equals(""))
                {
                    successFlag = false;
                    info = "获取微信OPENID失败";
                }
            } else if (authParam.getState().endsWith("ZFB")) {
                try {
                    String userID = alipayOauthService.getUserId(authParam.getAuth_code());
                    redirectAttributes.addAttribute("userID", userID);
                    String[] str=authParam.getState().split("\\|");
                    redirectAttributes.addAttribute("source",str[1]);
                    try {
                        redirectAttributes.addAttribute("uidEncode", URLEncoder.encode(str[0], AppPolicyConstant.enc));
                    }catch(Exception e){
                        log.info("http转义失败");
                        successFlag = false;
                        info = "转义失败,请稍后再试！";
                    }
                    if(userID==null||userID.equals(""))
                    {
                        successFlag = false;
                        info = "获取支付宝USERID失败";
                    }
                } catch (Exception e) {
                    successFlag = false;
                    info = "获取支付宝USERID失败";
                }
            } else {
                successFlag = false;
                info = "不支持该支付类型";
            }
        }
        else
        {
            successFlag = false;
            info = "授权失败";
        }
        redirectAttributes.addAttribute("successFlag", successFlag);
        redirectAttributes.addAttribute("infoDetail", info);
        return "redirect:/membership/checkMember";
    }

    @RequestMapping("checkMember")
    public String checkMember(HttpServletRequest request, HttpServletResponse response, Model model,AuthInfo authInfo){
        if(authInfo!=null&&authInfo.getInfoDetail()!=null&&!authInfo.getInfoDetail().equals("")) {
            try {
                authInfo.setInfoDetail(new String(authInfo.getInfoDetail().getBytes("iso-8859-1"), "utf-8"));
            } catch (Exception e) {
                log.info("http转义失败");
                model.addAttribute("tips","请稍后再试！");
                return "/tips";
            }
        }
        if(!authInfo.isSuccessFlag()) {
            model.addAttribute("tips",authInfo.getInfoDetail());
            return "/tips";
        }

        Long uid;
        try {
            String uidHttp=URLDecoder.decode(authInfo.getUidEncode(),AppPolicyConstant.enc);
            String uidAES=AppAesUtil.decryptCBC_NoPaddingFromBase64String(uidHttp, AppPolicyConstant.enc, AppPolicyConstant.secretKey, AppPolicyConstant.ivKey);
            uid=Long.parseLong(uidAES.trim());
        } catch (Exception e) {
            log.info("http转义失败");
            model.addAttribute("tips","请稍后再试！");
            return "/tips";
        }

        List<AppPolicyMembershipCard> cardList=hsyMembershipService.findMemberCardByUID(uid);
        model.addAttribute("cardList",cardList);

        AppPolicyConsumer appPolicyConsumer=null;
        if(authInfo.getSource().equals("WX")){
            appPolicyConsumer=hsyMembershipService.findConsumerByOpenID(authInfo.getOpenID());
            if(appPolicyConsumer==null){
                model.addAttribute("authInfo",authInfo);
                return "/createMember";
            }
        }else if(authInfo.getSource().equals("ZFB")){
            appPolicyConsumer=hsyMembershipService.findConsumerByOpenID(authInfo.getOpenID());
            if(appPolicyConsumer==null){
                model.addAttribute("authInfo",authInfo);
                return "/createMember";
            }
        }else{
            model.addAttribute("tips","请使用微信或支付宝");
            return "/tips";
        }

        AppPolicyMember appPolicyMember=hsyMembershipService.findMemberByCIDAndUID(appPolicyConsumer.getId(),uid);
        if(appPolicyMember==null) {
            model.addAttribute("authInfo", authInfo);
            model.addAttribute("appPolicyConsumer", appPolicyConsumer);
            return "/createMember";
        }

        if(appPolicyMember.getStatus()==2)
        {
            model.addAttribute("mid",appPolicyMember.getId());
            model.addAttribute("cellphone",appPolicyConsumer.getConsumerCellphone());
            return "/needRecharge";
        }

        model.addAttribute("mid", appPolicyMember.getId());
        return "redirect:/membership/createMemberSuccess";
    }

    @RequestMapping("createMember")
    public void createMember(HttpServletRequest request, HttpServletResponse response,PrintWriter pw, AppPolicyConsumer appPolicyConsumer,String source,String vcode,Long mcid,Long cid,Integer isDeposited){
        Map<String,String> map=new HashMap<String,String>();
        if(!(appPolicyConsumer.getConsumerCellphone()!=null&&!appPolicyConsumer.getConsumerCellphone().equals("")))
        {
            map.put("flag","fail");
            map.put("result","手机号不能为空！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }
        if (!ValidateUtils.isMobile(appPolicyConsumer.getConsumerCellphone()))
        {
            map.put("flag","fail");
            map.put("result","请填写正确的手机号！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }
        if(!(vcode!=null&&!vcode.equals("")))
        {
            map.put("flag","fail");
            map.put("result","验证码不能为空！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }
        AppAuVerification verification=hsyMembershipService.findRightVcode(appPolicyConsumer.getConsumerCellphone());
        if(verification==null)
        {
            map.put("flag","fail");
            map.put("result","验证码未发送或已失效！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }
        if(!verification.getCode().equals(vcode))
        {
            map.put("flag","fail");
            map.put("result","验证码错误！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }

        Integer status;
        if(isDeposited==0)
            status=MemberStatus.ACTIVE.key;
        else
            status=MemberStatus.NOT_ACTIVE_FOR_RECHARGE.key;
        AppPolicyMember appPolicyMember=null;
        if(cid!=null)//判断是否注册过消费者
            appPolicyConsumer.setId(cid);
        else{
            AppPolicyConsumer consumerCheck=hsyMembershipService.findConsumerByCellphone(appPolicyConsumer.getConsumerCellphone());
            if(consumerCheck!=null){//判断微信或支付宝以前是否注册过消费者
                appPolicyConsumer.setId(consumerCheck.getId());
                hsyMembershipService.insertOrUpdateConsumer(appPolicyConsumer);
                appPolicyMember=hsyMembershipService.findMemberByCIDAndMCID(appPolicyConsumer.getId(),mcid);
                if(appPolicyMember==null)//判断是否有该店会员卡
                {
                    Long accountID= memberAccountService.init();
                    Long receiptAccountID=receiptMemberMoneyAccountService.init();
                    appPolicyMember = hsyMembershipService.saveMember(appPolicyConsumer.getId(), mcid, status,accountID,receiptAccountID);
                }
                map.put("flag","success");
                map.put("mid",appPolicyMember.getId()+"");
                writeJsonToRrsponse(map,response,pw);
                return;
            }
        }
        hsyMembershipService.insertOrUpdateConsumer(appPolicyConsumer);
        Long accountID= memberAccountService.init();
        Long receiptAccountID=receiptMemberMoneyAccountService.init();
        appPolicyMember=hsyMembershipService.saveMember(appPolicyConsumer.getId(),mcid,status,accountID,receiptAccountID);
        map.put("flag","success");
        map.put("mid",appPolicyMember.getId()+"");
        map.put("status",status+"");
        writeJsonToRrsponse(map,response,pw);
        return;
    }

    @RequestMapping("createMemberSuccess")
    public String createMemberSuccess(HttpServletRequest request, HttpServletResponse response,Model model,Long mid){
        model.addAttribute("mid",mid);
        return "/createMemberSuccess";
    }

    @RequestMapping("memberInfo")
    public String memberInfo(HttpServletRequest request, HttpServletResponse response,Model model,Long mid){
        AppPolicyMember appPolicyMember=hsyMembershipService.findMemberInfoByID(mid);
        model.addAttribute("appPolicyMember",appPolicyMember);
        return "/memberInfo";
    }

    @RequestMapping("needRecharge")
    public String needRecharge(HttpServletRequest request, HttpServletResponse response,Model model,Long mid,String cellphone){
        model.addAttribute("mid",mid);
        model.addAttribute("cellphone",cellphone);
        return "/needRecharge";
    }

    @RequestMapping("toRecharge")
    public String toRecharge(HttpServletRequest request, HttpServletResponse response,Model model,Long mid){
        AppPolicyMember appPolicyMember=hsyMembershipService.findMemberInfoByID(mid);
        model.addAttribute("appPolicyMember",appPolicyMember);
        return "/toRecharge";
    }

    @RequestMapping("sendVcode")
    public void sendVcode(HttpServletRequest request, HttpServletResponse response, String cellphone, PrintWriter pw){
        Map<String,String> map=new HashMap<String,String>();
        if(!(cellphone!=null&&!cellphone.equals("")))
        {
            map.put("flag","fail");
            map.put("result","手机号不能为空！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }
        if (!ValidateUtils.isMobile(cellphone))
        {
            map.put("flag","fail");
            map.put("result","请填写正确的手机号！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }

        SmsTemplate messageTemplate = messageTemplateDao.getTemplateByType(AppPolicyConstant.REGISTER_VERIFICATION_NOTICE_TYPE_ID);
        String template="";
        if(messageTemplate!=null&&messageTemplate.getMessageTemplate()!=null&&!messageTemplate.getMessageTemplate().trim().equals(""))
            template=messageTemplate.getMessageTemplate();
        else
            template=AppPolicyConstant.REGISTER_VERIFICATION_MESSAGE;
        String code=(int)((Math.random()*9+1)*100000)+"";

        String content= template.replace("${code}",code);
        Map<String,String> keyValueForm=new HashMap<String, String>();
        keyValueForm.put("sn", AppPolicyConstant.SN);
        keyValueForm.put("pwd", AppPolicyConstant.PWD);
        keyValueForm.put("mobile", cellphone);
        keyValueForm.put("content", content);
        String sn="";
        try {
            sn=HttpUtil.httpRequestWithForm(AppPolicyConstant.SMSURL, "utf-8", keyValueForm);
        }catch(Exception e){
            map.put("flag","fail");
            map.put("result","发送验证码失败，请稍后再试！");
            writeJsonToRrsponse(map,response,pw);
            return;
        }

        SendMessageRecord sendRecord = new SendMessageRecord();
        sendRecord.setUserType(1);
        sendRecord.setMobile(cellphone);
        sendRecord.setContent(content);
        sendRecord.setMessageTemplateId(messageTemplate.getId());
        sendRecord.setSn(sn);
        sendRecord.setStatus(1);
        sendMessageRecordDao.insert(sendRecord);
        hsyMembershipService.insertVcode(sn,code,cellphone, VerificationCodeType.MEMBER_REGISTER.verificationCodeKey);
        map.put("flag","success");
        map.put("result","发送成功！");
        writeJsonToRrsponse(map,response,pw);
    }

    public void writeJsonToRrsponse(Object obj,HttpServletResponse response,PrintWriter pw){
        response.setContentType("application/json;charset=utf-8");
        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json=gson.toJson(obj);
        pw.write(json);
    }

}
