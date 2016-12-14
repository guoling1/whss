package com.jkm.base.common.spring.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by huangwei on 6/3/15.
 * FIXME 读取多配置文件
 */
@Component
@Lazy(value = false)
public final class ConfigurationUtils {
    private static Properties properties;

    /**
     * Get a string associated with the given configuration key
     *
     * @param key key
     * @return string
     */
    public static String getString(final String key) {
        return properties.getProperty(key);
    }

    /**
     * Get a string associated with the given configuration key
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return string
     */
    public static String getString(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 是否有key
     *
     * @param key key
     * @return string
     */
    public static boolean containsKey(final String key) {
        return properties.containsKey(key);
    }

    /**
     * 可通过spring配置文件设置properties
     * //TODO 解除对spring的依赖，扫描所有jar包的properties格式文件
     *
     * @param properties
     */
    public void setProperties(final Properties properties) {
        ConfigurationUtils.properties = properties;
    }
}
