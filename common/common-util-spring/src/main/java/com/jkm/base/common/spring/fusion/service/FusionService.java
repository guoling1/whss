package com.jkm.base.common.spring.fusion.service;



import com.jkm.base.common.spring.fusion.helper.fusion.CardAuthData;
import com.jkm.base.common.spring.fusion.helper.fusion.QueryCardAuthData;

import java.util.Map;

/**
 * Created by xingliujie on 2017/2/7.
 */
public interface FusionService {
    /**
     * 通过原始报文和商户号对报文进行验签
     * @param orgMsg 系统请求报文
     * @return
     */
    public boolean isSignature(String orgMsg);

    /**
     * 响应报文加签处理
     * @param orgMsg
     * @return
     */
    public String addSignatrue(String orgMsg);

    /**
     * 银行卡鉴权
     * @param cardAuthData
     * @return
     */
    public Map<String, Object> cardAuth(CardAuthData cardAuthData) throws Exception;
    /**
     * 银行卡鉴权
     * @param queryCardAuthData
     * @return
     */
    public Map<String, Object> queryCardAuth(QueryCardAuthData queryCardAuthData) throws Exception;
}
