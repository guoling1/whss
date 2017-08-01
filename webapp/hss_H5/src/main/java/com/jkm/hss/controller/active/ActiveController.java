package com.jkm.hss.controller.active;

import com.google.gson.Gson;
import com.jkm.base.common.spring.core.SpringContextHolder;
import com.jkm.hss.merchant.entity.AppAuUserToken;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.exception.ResultCode;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.merchant.service.AppAuTokenService;
import com.jkm.hss.push.entity.AppResult;
import com.jkm.hss.version.ExcludeServiceCode;
import com.jkm.hss.version.VersionMapper;
import com.jkm.hsy.user.util.AppAesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/active")
public class ActiveController {
    @Autowired
    private AppAuTokenService appAuTokenService;

    @RequestMapping("rest")
    public void rest(@ModelAttribute AppParam appParam, HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws Exception {
        long startTime=System.currentTimeMillis();
        String privateKey = "6w3W8OOgnRZrkBGS2AdpFTpOykcUsvfI";
        AppResult result=new AppResult();
        if(appParam==null)
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,"");
            return;
        }
        if(this.isLackOfParam(appParam))
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,"");
            return;
        }
        log.info("请求参数为{}",appParam);
        Map<String,String[]> bizMapper= VersionMapper.versionMap.get(appParam.getV());
        if(bizMapper==null)
        {
            result.setResultCode(ResultCode.VERSION_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.VERSION_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
            return;
        }

        String[] strs=bizMapper.get(appParam.getServiceCode());
        if(strs==null)
        {
            result.setResultCode(ResultCode.CLASS_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.CLASS_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
            return;
        }

        if(!appParam.getServiceCode().equals("HSS001001"))
        {
            if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().trim().equals("")))
            {
                result.setResultCode(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultCode);
                result.setResultMessage(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultMessage);
                this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                return;
            }

            if(!isExcludeServiceCode(appParam.getServiceCode())) {
                AppAuUserToken appAuUserToken = appAuTokenService.findLoginInfoByAccessToken(appParam.getAccessToken());
                if (!(appAuUserToken != null && appAuUserToken.getOutTime() != null)) {
                    result.setResultCode(ResultCode.USER_NOT_LOGIN.resultCode);
                    result.setResultMessage(ResultCode.USER_NOT_LOGIN.resultMessage);
                    this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                    return;
                } else {
                    if (appAuUserToken.getOutTime().before(new Date())) {
                        result.setResultCode(ResultCode.USER_LOGIN_OUTTIME.resultCode);
                        result.setResultMessage(ResultCode.USER_LOGIN_OUTTIME.resultMessage);
                        this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                        return;
                    }
                }
            }
            privateKey = appParam.getAccessToken();
        }
        try {
            if(appParam.getRequestData()!=null&&!"".equals(appParam.getRequestData())){
                String httpDecoder = URLDecoder.decode(appParam.getRequestData(),"utf-8");
                appParam.setRequestData(AppAesUtil.decryptCBC_NoPaddingFromBase64String(httpDecoder, "utf-8", privateKey.substring(0,16), privateKey.substring(16,32)));
            }
        } catch (Exception e) {
            log.error("解密[{}]异常", e.getMessage());
            throw new Exception("解密异常");
        }
        log.info("解密之后参数是{}",appParam);
        ApplicationContext ac=SpringContextHolder.getApplicationContext();
        Object obj=ac.getBean(strs[0]);
        Class<? extends Object> clazz = obj.getClass();
        Method method=null;
        String appResult=null;
        try {
            method = clazz.getMethod(strs[1],String.class,AppParam.class);
            appResult = (String) method.invoke(obj, appParam.getRequestData(), appParam);
        } catch (Exception e) {
            log.error("调用接口异常", e);
            e.printStackTrace();
            if(e.getCause() instanceof ApiHandleException)
            {
                ApiHandleException ahe=((ApiHandleException)e.getCause());
                result.setResultCode(ahe.getResultCode().resultCode);
                String msg="";
                if(ahe.getMsg()!=null)
                    msg=":"+ahe.getMsg();
                if(ahe.getResultCode().resultCode==ResultCode.CUSTOM_EXCEPTION.resultCode){
                    result.setResultMessage(msg);
                }else{
                    result.setResultMessage(ahe.getResultCode().resultMessage+msg);
                }
                this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                return;
            }
            result.setResultCode(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultCode);
            result.setResultMessage(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
            return;
        }

        result.setResultCode(ResultCode.SUCCESS.resultCode);
        if(strs.length==3)
            result.setResultMessage(strs[2]);
        else
            result.setResultMessage(ResultCode.SUCCESS.resultMessage);
        log.info("明文返回结果是："+appResult);
        if(appResult!=null&&!"".equals(appResult)){
            String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(appResult, "utf-8", privateKey.substring(0,16), privateKey.substring(16,32));
            log.info("加密返回结果是{}",base64E);
            String httpEncode= URLEncoder.encode(base64E,"utf-8");
            log.info("URLEncode之后返回结果是："+httpEncode);
            result.setEncryptDataResult(httpEncode);
        }
        this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
        return;
    }

    @RequestMapping("file")
    public void file(@RequestParam(value = "fileA", required = false) MultipartFile fileA,@RequestParam(value = "fileB", required = false) MultipartFile fileB,@RequestParam(value = "fileC", required = false) MultipartFile fileC,@RequestParam(value = "fileD", required = false) MultipartFile fileD, @ModelAttribute AppParam appParam, HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws Exception {
        long startTime=System.currentTimeMillis();
        String privateKey = "6w3W8OOgnRZrkBGS2AdpFTpOykcUsvfI";
        AppResult result=new AppResult();
        if(appParam==null)
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,"");
            return;
        }
        if(this.isLackOfParam(appParam))
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,"");
            return;
        }
        log.info(">>>>--"+appParam.getServiceCode()+"--start-->>>>业务代码为："+appParam.getServiceCode()+"--版本号为："+appParam.getV()+"--token为："+appParam.getAccessToken()+"--appType为："+appParam.getAppType());
        log.info("请求参数是："+appParam.getRequestData());

        Map<String,String[]> bizMapper= VersionMapper.versionMap.get(appParam.getV());
        if(bizMapper==null)
        {
            result.setResultCode(ResultCode.VERSION_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.VERSION_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
            return;
        }

        String[] strs=bizMapper.get(appParam.getServiceCode());
        if(strs==null)
        {
            result.setResultCode(ResultCode.CLASS_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.CLASS_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
            return;
        }

        if(!appParam.getServiceCode().equals("HSS001001"))
        {
            if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().trim().equals("")))
            {
                result.setResultCode(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultCode);
                result.setResultMessage(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultMessage);
                this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                return;
            }

            if(!isExcludeServiceCode(appParam.getServiceCode())) {
                AppAuUserToken appAuUserToken = appAuTokenService.findLoginInfoByAccessToken(appParam.getAccessToken());
                if (!(appAuUserToken != null && appAuUserToken.getOutTime() != null)) {
                    result.setResultCode(ResultCode.USER_NOT_LOGIN.resultCode);
                    result.setResultMessage(ResultCode.USER_NOT_LOGIN.resultMessage);
                    this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                    return;
                } else {
                    if (appAuUserToken.getOutTime().before(new Date())) {
                        result.setResultCode(ResultCode.USER_LOGIN_OUTTIME.resultCode);
                        result.setResultMessage(ResultCode.USER_LOGIN_OUTTIME.resultMessage);
                        this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                        return;
                    }
                }
            }
            privateKey = appParam.getAccessToken();
        }

        try {
            if(appParam.getRequestData()!=null&&!"".equals(appParam.getRequestData())){
                appParam.setRequestData(AppAesUtil.decryptCBC_NoPaddingFromBase64String(appParam.getRequestData(), "utf-8", privateKey.substring(0,16), privateKey.substring(16,32)));
            }
        } catch (Exception e) {
            log.error("解密[{}]异常", e.getMessage());
            throw new Exception("解密异常");
        }
        log.info("解密之后请求参数是："+appParam.getRequestData());
        ApplicationContext ac=SpringContextHolder.getApplicationContext();
        Object obj=ac.getBean(strs[0]);
        Class<? extends Object> clazz = obj.getClass();
        Method method=null;
        String appResult=null;
        try {
            Map<String,MultipartFile> fileMap=new HashMap<String,MultipartFile>();
            fileMap.put("fileA",fileA);
            fileMap.put("fileB",fileB);
            fileMap.put("fileC",fileC);
            if(fileD!=null&&!fileD.isEmpty()) {
                fileMap.put("fileD", fileD);
            }
            method = clazz.getMethod(strs[1],String.class,AppParam.class,Map.class);
            appResult = (String) method.invoke(obj, appParam.getRequestData(), appParam, fileMap);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getCause() instanceof ApiHandleException)
            {
                ApiHandleException ahe=((ApiHandleException)e.getCause());
                result.setResultCode(ahe.getResultCode().resultCode);
                String msg="";
                if(ahe.getMsg()!=null)
                    msg=":"+ahe.getMsg();
                result.setResultMessage(ahe.getResultCode().resultMessage+msg);
                this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
                return;
            }
            result.setResultCode(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultCode);
            result.setResultMessage(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultMessage);
            this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
            return;
        }

        result.setResultCode(ResultCode.SUCCESS.resultCode);
        if(strs.length==3)
            result.setResultMessage(strs[2]);
        else
            result.setResultMessage(ResultCode.SUCCESS.resultMessage);
        log.info("明文返回结果是："+appResult);
        if(appResult!=null&&!"".equals(appResult)){
            String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(appResult, "utf-8", privateKey.substring(0,16), privateKey.substring(16,32));
            log.info("加密返回结果是："+base64E);
            result.setEncryptDataResult(base64E);
        }
        this.writeJsonToRrsponse(result, response, pw,startTime,appParam.getServiceCode());
        return;
    }

    public boolean isLackOfParam(AppParam appParam){
        if((appParam.getServiceCode()==null||(appParam.getServiceCode()!=null&&appParam.getServiceCode().trim().equals("")))
                ||(appParam.getV()==null||(appParam.getV()!=null&&appParam.getV().trim().equals("")))
                ||(appParam.getAppType()==null||(appParam.getAppType()!=null&&appParam.getAppType().trim().equals("")))
                ||(appParam.getDeviceId()==null||(appParam.getDeviceId()!=null&&appParam.getDeviceId().trim().equals("")))
                )
            return true;
        if(!(appParam.getAppType().equals("ios")||appParam.getAppType().equals("android")))
            return true;
        return false;
    }

    public boolean isExcludeServiceCode(String serviceCode){
        for(int i=0; i<ExcludeServiceCode.excludeCode.length;i++)
        {
            if(serviceCode.equals(ExcludeServiceCode.excludeCode[i]))
                return true;
        }
        return false;
    }

    public void writeJsonToRrsponse(Object obj,HttpServletResponse response,PrintWriter pw,long startTime,String serviceCode){
        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        response.setContentType("application/json;charset=utf-8");
        Gson gson=new Gson();
        String json=gson.toJson(obj);
        log.info("返回结果是："+json);
        log.info("<<<<--"+serviceCode+"--end--<<<<执行时间是："+excTime+"s");
        pw.write(json);
    }
}
