/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.jkm.base.common.spring.alipay.executor;


import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;
import com.jkm.base.common.spring.alipay.util.MyException;

/**
 * 开通服务窗开发者功能处理器
 * 
 * @author taixu.zqq
 * @version $Id: InAlipayOpenExecutor.java, v 0.1 2014年7月24日 下午5:05:13 taixu.zqq Exp $
 */
public class InAlipayVerifyExecutor implements ActionExecutor {

    /** 
     *
     */
    @Override
    public String execute() throws MyException {
        return this.setResponse();
    }

    /**
     * 设置response返回数据
     * 
     * @return
     */
    private String setResponse() throws MyException {
        //固定响应格式，必须按此格式返回
        StringBuilder builder = new StringBuilder();
        builder.append("<success>").append(Boolean.TRUE.toString()).append("</success>");
        builder.append("<biz_content>").append(AlipayServiceConstants.PUBLIC_KEY)
            .append("</biz_content>");
        return builder.toString();
    }
}
