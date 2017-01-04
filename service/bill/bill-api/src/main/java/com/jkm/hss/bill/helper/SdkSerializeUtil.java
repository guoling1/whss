package com.jkm.hss.bill.helper;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/21.
 */
@Slf4j
@UtilityClass
public class SdkSerializeUtil {

    /**
     * convert obj to map
     *
     * 不支持应用类型
     *
     * @param obj
     * @return
     */
    public static <T> Map<String, String> convertObjToMap(final T obj) {
        return convertObjToMapImpl(obj);
    }

    private static <T> Map<String, String> convertObjToMapImpl(final T obj) {
        final List<Field> fields = FieldUtils.getAllFieldsList(obj.getClass());
        final Map<String, String> result = new HashMap<>();
        for (final Field field : fields) {
            final String key = field.getName();
            final Object fieldValue = getFieldValue(field, obj);
            if (Objects.equal(fieldValue, null) || Objects.equal(field, "")) {
                continue;
            }
            final String value = fieldValue.toString();
            result.put(key, value);
        }
        return result;
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
