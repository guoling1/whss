package com.jkm.base.common.spring.aliyun.service.impl;

import com.alibaba.fastjson.JSON;
import com.jkm.base.common.spring.aliyun.entity.VerifyID4ElementResponse;
import com.jkm.base.common.spring.aliyun.enums.EnumVerifyID4ElementErrorCode;
import com.jkm.base.common.spring.aliyun.enums.EnumVerifyID4ElementStatus;
import com.jkm.base.common.spring.aliyun.helper.Constants;
import com.jkm.base.common.spring.aliyun.service.VerifyID4ElementService;
import com.jkm.base.common.spring.aliyun.util.HttpUtils;
import com.jkm.base.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Slf4j
@Service
public class VerifyID4ElementServiceImpl implements VerifyID4ElementService {
    /**
     * {@inheritDoc}
     *
     * @param bankcard  银行卡
     * @param idCard    身份证
     * @param mobile    银行预留手机号
     * @param realName  真实姓名
     * @return
     */
    @Override
    public Pair<Integer, String> verify(final String bankcard, final String idCard,
                                        final String mobile, final String realName) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + Constants.appCode);
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("bankcard", StringUtils.trim(bankcard));
        queryParams.put("idcard", StringUtils.trim(idCard));
        queryParams.put("mobile", StringUtils.trim(mobile));
        queryParams.put("realname", StringUtils.trim(realName));
        try {
            final HttpResponse response = HttpUtils.doGet(Constants.host, Constants.path, "GET", headers, queryParams);
            final String resultString = EntityUtils.toString(response.getEntity());
            if (StringUtils.isEmpty(resultString)) {
                return Pair.of(-1, "验证服务器异常");
            }
            final Map<String, String> map = JSON.parseObject(resultString, Map.class);
            if ("".equals(map.get("result"))) {
                map.remove("result");
            }
            final VerifyID4ElementResponse verifyID4ElementResponse = JSON.parseObject(JSON.toJSONString(map), VerifyID4ElementResponse.class);
            final String status = verifyID4ElementResponse.getStatus();
            final EnumVerifyID4ElementErrorCode code = EnumVerifyID4ElementErrorCode.of(status);
            switch (code) {
                case NORMAL:
                    final VerifyID4ElementResponse.message result = verifyID4ElementResponse.getResult();
                    final String verifyStatus = result.getVerifyStatus();
                    if (EnumVerifyID4ElementStatus.PASS.getCode().equals(verifyStatus)) {
                        return Pair.of(0, result.getVerifyMsg());
                    } else if (EnumVerifyID4ElementStatus.NOT_PASS.getCode().equals(verifyStatus)) {
                        return Pair.of(-1, result.getVerifyMsg());
                    }
                    return Pair.of(-1, "验证服务器异常");
                case EMPTY_BANK_CARD:
                    return Pair.of(-1, EnumVerifyID4ElementErrorCode.EMPTY_BANK_CARD.getMsg());
                case EMPTY_REAL_NAME:
                    return Pair.of(-1, EnumVerifyID4ElementErrorCode.EMPTY_REAL_NAME.getMsg());
                case ERROR_BANK_CARD:
                    return Pair.of(-1, EnumVerifyID4ElementErrorCode.ERROR_BANK_CARD.getMsg());
                case ERROR_REAL_NAME:
                    return Pair.of(-1, EnumVerifyID4ElementErrorCode.ERROR_REAL_NAME.getMsg());
                case ERROR_ID:
                    return Pair.of(-1, EnumVerifyID4ElementErrorCode.ERROR_ID.getMsg());
                case NO_MESSAGE:
                    return Pair.of(-1, EnumVerifyID4ElementErrorCode.NO_MESSAGE.getMsg());
                default:
                    return Pair.of(-1, "验证服务器异常");
            }
        } catch (final Exception e) {
            log.error("request aliyun verify ID4Element error", e);
            e.printStackTrace();
        }
        return Pair.of(-1, "验证服务器异常");
    }
}
