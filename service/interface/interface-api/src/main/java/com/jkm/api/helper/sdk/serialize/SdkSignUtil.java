package com.jkm.api.helper.sdk.serialize;

import com.google.common.base.*;
import com.google.common.collect.Lists;
import com.jkm.api.helper.sdk.serialize.annotation.SdkAnnotationSupporter;
import com.jkm.base.common.util.BytesHexConverterUtil;
import com.jkm.base.common.util.Md5Util;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Objects;

/**
 * Created by yulong.zhang on 2017/2/10.
 */
@Slf4j
@UtilityClass
public class SdkSignUtil {

    public static final String SIGN_KEY = "sign";

    public static <T> String sign2(final T obj, final String key) {
        final List<Field> fields = FieldUtils.getAllFieldsList(obj.getClass());
        final Map<Integer, Field> signMap = new HashMap<>();
        for (final Field field : fields) {
            if (SdkAnnotationSupporter.getFieldNeedSign(field)) {
                signMap.put(SdkAnnotationSupporter.getFieldSignSort(field), field);
            }
        }
        final List<Integer> paramsKeyList = Lists.newArrayList(signMap.keySet());
        Collections.sort(paramsKeyList);
        final StringBuilder content = new StringBuilder();
        for (final Integer paramsKey : paramsKeyList) {
            final Field field = signMap.get(paramsKey);
            final String fieldKey = SdkAnnotationSupporter.getFieldAlias(field);
            final Object fieldValue = getFieldValue(field, obj);
            content.append(fieldKey).append("=").append(Objects.equals(fieldValue, null) ? "" : fieldValue.toString()).append("&");
        }
        content.append("key=").append(key);
        log.debug("签名字符串:[{}]", content.toString());
        return BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest(content.toString()
                .getBytes(Charset.forName("utf-8"))));
    }



    public static String sign(@NonNull final Map<String, String> requestParamMap, @NonNull final String key) {
        final String signedStr = getSignedStr(requestParamMap, key);
        log.debug("签名字符串:{" + signedStr + "}");
        return BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest(signedStr
                .getBytes(Charset.forName("utf-8"))));
    }

    /**
     * 获得签名字符串
     *
     * @param params
     * @param key
     * @return
     */
    private static String getSignedStr(final Map<String, String> params,
                                       final String key) {
        Preconditions.checkArgument(!params.isEmpty(), "待签名数据不能为空");
        final List<String> paramsKeyList = Lists.newArrayList(params.keySet());
        Collections.sort(paramsKeyList);
        final StringBuilder content = new StringBuilder();
        for (final String paramsKey : paramsKeyList) {
            if (!Objects.equals(paramsKey, SIGN_KEY) && !Objects.equals("", params.get(paramsKey))) {
                content.append(paramsKey).append("=").append(params.get(paramsKey)).append("&");
            }
        }
        content.append("key=").append(key);
        return content.toString();
    }

    private static final Object getFieldValue(final Field field,
                                              final Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}
