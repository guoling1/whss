package com.jkm.api.helper.sdk.serialize.annotation;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

/**
 * Created by yulong.zhang on 2017/2/10.
 */
@UtilityClass
public class SdkAnnotationSupporter {
    public static String getFieldAlias(final Field field) {
        final SdkSerializeAlias aliasAnnotation = field.getAnnotation(SdkSerializeAlias.class);
        if (aliasAnnotation == null) {
            return field.getName();
        }
        return aliasAnnotation.name();
    }
}
