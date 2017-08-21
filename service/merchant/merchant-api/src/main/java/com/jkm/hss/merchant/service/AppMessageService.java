package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.enums.EnumMessageTemplate;
import com.jkm.hss.merchant.enums.EnumMessageType;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.helper.AppParam;

import java.util.Map;

/**
 * Created by Allen on 2017/8/14.
 */
public interface AppMessageService {
    public String getMessageList(String dataParam, AppParam appParam) throws ApiHandleException;
    public String updateReadStatus(String dataParam, AppParam appParam)throws ApiHandleException;
    /**
     * 保存消息
     * @param uid 分润的ID
     * @param type 消息类型
     * @param template 模板类型
     * @param param 替换参数 按模板中的参数来定义 有{amount}类似字段 则为amount
     */
    public void insertMessageInfoAndPush(Long uid, EnumMessageType type, EnumMessageTemplate template, Map<String,String> param);
}
