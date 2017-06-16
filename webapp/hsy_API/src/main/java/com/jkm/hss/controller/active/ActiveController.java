package com.jkm.hss.controller.active;

import com.google.gson.Gson;
import com.jkm.base.common.spring.core.SpringContextHolder;
import com.jkm.hss.version.ExcludeServiceCode;
import com.jkm.hss.version.VersionMapper;
import com.jkm.hsy.user.entity.AppAuUserToken;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.entity.AppResult;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyActiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/active")
public class ActiveController {

    @Autowired
    private HsyActiveService hsyActiveService;

    @RequestMapping("rest")
    public void rest(@ModelAttribute AppParam appParam, HttpServletRequest request, HttpServletResponse response, PrintWriter pw){
        AppResult result=new AppResult();
        if(appParam==null)
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }
        if(this.isLackOfParam(appParam))
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        Map<String,String[]> bizMapper= VersionMapper.versionMap.get(appParam.getV());
        if(bizMapper==null)
        {
            result.setResultCode(ResultCode.VERSION_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.VERSION_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        String[] strs=bizMapper.get(appParam.getServiceCode());
        if(strs==null)
        {
            result.setResultCode(ResultCode.CLASS_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.CLASS_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        if(!appParam.getServiceCode().equals("HSY001016"))
        {
            if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().trim().equals("")))
            {
                result.setResultCode(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultCode);
                result.setResultMessage(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultMessage);
                this.writeJsonToRrsponse(result, response, pw);
                return;
            }

            if(!isExcludeServiceCode(appParam.getServiceCode())) {
                AppAuUserToken appAuUserToken = hsyActiveService.findLoginInfoByAccessToken(appParam.getAccessToken());
                if (!(appAuUserToken != null && appAuUserToken.getOutTime() != null)) {
                    result.setResultCode(ResultCode.USER_NOT_LOGIN.resultCode);
                    result.setResultMessage(ResultCode.USER_NOT_LOGIN.resultMessage);
                    this.writeJsonToRrsponse(result, response, pw);
                    return;
                } else {
                    if (appAuUserToken.getOutTime().before(new Date())) {
                        result.setResultCode(ResultCode.USER_LOGIN_OUTTIME.resultCode);
                        result.setResultMessage(ResultCode.USER_LOGIN_OUTTIME.resultMessage);
                        this.writeJsonToRrsponse(result, response, pw);
                        return;
                    }
                }
            }
        }

        log.info("业务代码为："+appParam.getServiceCode()+"-版本号为："+appParam.getV()+"-token为："+appParam.getAccessToken());
        log.info("请求参数是："+appParam.getRequestData());

        ApplicationContext ac=SpringContextHolder.getApplicationContext();
//        ApplicationContext ac= WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
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
                result.setResultMessage(ahe.getResultCode().resultMessage+msg);
                this.writeJsonToRrsponse(result, response, pw);
                return;
            }
            result.setResultCode(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultCode);
            result.setResultMessage(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        result.setResultCode(ResultCode.SUCCESS.resultCode);
        if(strs.length==3)
            result.setResultMessage(strs[2]);
        else
            result.setResultMessage(ResultCode.SUCCESS.resultMessage);
        result.setEncryptDataResult(appResult);
        this.writeJsonToRrsponse(result, response, pw);
        return;
    }

    @RequestMapping("file")
    public void file(@RequestParam(value = "fileA", required = false) MultipartFile fileA,@RequestParam(value = "fileB", required = false) MultipartFile fileB,@RequestParam(value = "fileC", required = false) MultipartFile fileC,@RequestParam(value = "fileD", required = false) MultipartFile fileD, @ModelAttribute AppParam appParam, HttpServletRequest request, HttpServletResponse response, PrintWriter pw){
        AppResult result=new AppResult();
        if(appParam==null)
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }
        if(this.isLackOfParam(appParam))
        {
            result.setResultCode(ResultCode.PARAM_EXCEPTION.resultCode);
            result.setResultMessage(ResultCode.PARAM_EXCEPTION.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        Map<String,String[]> bizMapper= VersionMapper.versionMap.get(appParam.getV());
        if(bizMapper==null)
        {
            result.setResultCode(ResultCode.VERSION_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.VERSION_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        String[] strs=bizMapper.get(appParam.getServiceCode());
        if(strs==null)
        {
            result.setResultCode(ResultCode.CLASS_NOT_EXIST.resultCode);
            result.setResultMessage(ResultCode.CLASS_NOT_EXIST.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        if(!appParam.getServiceCode().equals("HSY001016"))
        {
            if(!(appParam.getAccessToken()!=null&&!appParam.getAccessToken().trim().equals("")))
            {
                result.setResultCode(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultCode);
                result.setResultMessage(ResultCode.TOKEN_CAN_NOT_BE_NULL.resultMessage);
                this.writeJsonToRrsponse(result, response, pw);
                return;
            }

            if(!isExcludeServiceCode(appParam.getServiceCode())) {
                AppAuUserToken appAuUserToken = hsyActiveService.findLoginInfoByAccessToken(appParam.getAccessToken());
                if (!(appAuUserToken != null && appAuUserToken.getOutTime() != null)) {
                    result.setResultCode(ResultCode.USER_NOT_LOGIN.resultCode);
                    result.setResultMessage(ResultCode.USER_NOT_LOGIN.resultMessage);
                    this.writeJsonToRrsponse(result, response, pw);
                    return;
                } else {
                    if (appAuUserToken.getOutTime().before(new Date())) {
                        result.setResultCode(ResultCode.USER_LOGIN_OUTTIME.resultCode);
                        result.setResultMessage(ResultCode.USER_LOGIN_OUTTIME.resultMessage);
                        this.writeJsonToRrsponse(result, response, pw);
                        return;
                    }
                }
            }
        }

        ApplicationContext ac=SpringContextHolder.getApplicationContext();
//        ApplicationContext ac= WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
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
                this.writeJsonToRrsponse(result, response, pw);
                return;
            }
            result.setResultCode(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultCode);
            result.setResultMessage(ResultCode.FUNCTION_REMOTE_CALL_FAILED.resultMessage);
            this.writeJsonToRrsponse(result, response, pw);
            return;
        }

        result.setResultCode(ResultCode.SUCCESS.resultCode);
        if(strs.length==3)
            result.setResultMessage(strs[2]);
        else
            result.setResultMessage(ResultCode.SUCCESS.resultMessage);
        result.setEncryptDataResult(appResult);
        this.writeJsonToRrsponse(result, response, pw);
        return;
    }

    public boolean isLackOfParam(AppParam appParam){
        if((appParam.getServiceCode()==null||(appParam.getServiceCode()!=null&&appParam.getServiceCode().trim().equals("")))
                ||(appParam.getV()==null||(appParam.getV()!=null&&appParam.getV().trim().equals("")))
                ||(appParam.getAppType()==null||(appParam.getAppType()!=null&&appParam.getAppType().trim().equals("")))
                ||(appParam.getDeviceid()==null||(appParam.getDeviceid()!=null&&appParam.getDeviceid().trim().equals("")))
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

    public void writeJsonToRrsponse(Object obj,HttpServletResponse response,PrintWriter pw){
        response.setContentType("application/json;charset=utf-8");
        Gson gson=new Gson();
        String json=gson.toJson(obj);
        pw.write(json);
    }
}
