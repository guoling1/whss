package com.jkm.hss.merchant.service.impl;

import com.google.gson.*;
import com.jkm.base.common.util.Page;
import com.jkm.base.common.util.PageUtils;
import com.jkm.hss.merchant.constant.AppConstant;
import com.jkm.hss.merchant.dao.MessageInfoDao;
import com.jkm.hss.merchant.entity.AppAuUserToken;
import com.jkm.hss.merchant.entity.MessageInfo;
import com.jkm.hss.merchant.enums.EnumMessageTemplate;
import com.jkm.hss.merchant.enums.EnumMessageType;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.exception.ResultCode;
import com.jkm.hss.merchant.helper.AppParam;
import com.jkm.hss.merchant.service.AppAuTokenService;
import com.jkm.hss.merchant.service.AppMessageService;
import com.jkm.hss.notifier.dao.MessageTemplateDao;
import com.jkm.hss.notifier.entity.SmsTemplate;
import com.jkm.hss.push.sevice.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Allen on 2017/8/14.
 */
@Slf4j
@Service("appMessageService")
public class AppMessageServiceImpl implements AppMessageService {

    @Autowired
    private MessageTemplateDao messageTemplateDao;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private AppAuTokenService appAuTokenService;
    @Autowired
    private PushService pushService;

    /**
     * 得到消息列表  HSS001013
     */
    public String getMessageList(String dataParam, AppParam appParam) throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        MessageInfo messageInfo=null;
        try{
            messageInfo=gson.fromJson(dataParam, MessageInfo.class);
        } catch(Exception e){
            throw new ApiHandleException(ResultCode.PARAM_TRANS_FAIL);
        }

        /**参数验证*/
        if(!(messageInfo.getCurrentPage()!=null&&!messageInfo.getCurrentPage().equals("")))
            throw new ApiHandleException(ResultCode.PARAM_LACK,"当前页数");
//        if(!(messageInfo.getUid()!=null&&!messageInfo.getUid().equals("")))
//            throw new ApiHandleException(ResultCode.PARAM_LACK,"用户ID");
        if(messageInfo.getCurrentPage()<=0)
            throw new ApiHandleException(ResultCode.CURRENT_PAGE_MUST_BE_BIGGER_THAN_ZERO);

        AppAuUserToken appAuUserToken = appAuTokenService.findLoginInfoByAccessToken(appParam.getAccessToken());
        messageInfo.setUid(appAuUserToken.getUid());

        PageUtils page=new PageUtils();
        page.setCurrentPage(messageInfo.getCurrentPage());
        page.setPageSize(AppConstant.PAGE_SIZE);
        Page<MessageInfo> pageAll=new Page<MessageInfo>();
        pageAll.setObjectT(messageInfo);
        pageAll.setPage(page);
        pageAll.getPage().setTotalRecord(messageInfoDao.findMessageInfoListByPageCount(pageAll.getObjectT()));
        pageAll.setList(messageInfoDao.findMessageInfoListByPage(pageAll));
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes f) {
                boolean flag=false;
                if(f.getName().contains("objectT"))
                    return true;
                if(f.getName().contains("viewpagecount"))
                    return true;
                if(f.getName().contains("startPageIndex"))
                    return true;
                if(f.getName().contains("endPageIndex"))
                    return true;
                return flag;
            }
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
                return new JsonPrimitive(date.getTime());
            }
        }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.toJson(pageAll);
    }

    /**
     * 更改已读状态  HSS001014
     */
    public String updateReadStatus(String dataParam, AppParam appParam)throws ApiHandleException {
        Date date=new Date();
        AppAuUserToken appAuUserToken = appAuTokenService.findLoginInfoByAccessToken(appParam.getAccessToken());
        MessageInfo messageInfo=new MessageInfo();
        messageInfo.setUid(appAuUserToken.getUid());
        messageInfo.setCreateTime(date);
        messageInfoDao.updateReadStatusBeyondTime(messageInfo);
        return "";
    }

    /**
     * 保存消息
     * @param uid 分润的ID
     * @param type 消息类型
     * @param template 模板类型
     * @param param 替换参数 按模板中的参数来定义 有{amount}类似字段 则为amount
     */
    public void insertMessageInfoAndPush(Long uid, EnumMessageType type, EnumMessageTemplate template, Map<String,String> param){
        String content="";
        SmsTemplate messageTemplate = messageTemplateDao.getTemplateByType(template.key);
        if(messageTemplate!=null&&messageTemplate.getMessageTemplate()!=null&&!messageTemplate.getMessageTemplate().trim().equals(""))
            content=messageTemplate.getMessageTemplate();
        else
            content=template.value;
        Iterator<String> it=param.keySet().iterator();
        while(it.hasNext()){
            String key=it.next();
            content=content.replace("{"+key+"}",param.get(key));
        }

        Date date=new Date();
        MessageInfo messageInfo=new MessageInfo();
        messageInfo.setUid(uid);
        messageInfo.setStatus(1);
        messageInfo.setType(type.key);
        messageInfo.setTitle(type.value);
        messageInfo.setContent(content);
        messageInfo.setReadStatus(0);
        messageInfo.setCreateTime(date);
        messageInfo.setUpdateTime(date);
        messageInfoDao.insert(messageInfo);

        try {
            pushService.pushMessage(uid, content);
        }catch(Exception e){
            log.info("发送消息失败"+e.getMessage());
        }

    }
}
