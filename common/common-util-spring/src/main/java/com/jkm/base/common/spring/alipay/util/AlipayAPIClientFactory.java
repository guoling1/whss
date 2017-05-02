/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.jkm.base.common.spring.alipay.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.jkm.base.common.spring.alipay.constant.AlipayServiceConstants;


/**
 * API调用客户端工厂
 * 
 * @author taixu.zqq
 * @version $Id: AlipayAPIClientFactory.java, v 0.1 2014年7月23日 下午5:07:45 taixu.zqq Exp $
 */
public class AlipayAPIClientFactory {

    /** API调用客户端 */
    private static AlipayClient alipayClient;
    
    /**
     * 获得API调用客户端
     * 
     * @return
     */
    public static AlipayClient getAlipayClient(){
        
        if(null == alipayClient){
            alipayClient = new DefaultAlipayClient(AlipayServiceConstants.ALIPAY_GATEWAY, AlipayServiceConstants.APP_ID,
                AlipayServiceConstants.PRIVATE_KEY, "json", AlipayServiceConstants.CHARSET,AlipayServiceConstants.ALIPAY_PUBLIC_KEY, AlipayServiceConstants.SIGN_TYPE);
        }
        return alipayClient;
    }
}
