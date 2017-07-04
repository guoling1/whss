package com.jkm.base.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/15.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<M extends BaseEntity> {

    /**
     * 成功返回码
     */
    public static final int SUCCESS_CODE = 1;
    /**
     * 返回值
     */
    private final int code;
    /**
     * 返回描述信息
     */
    @JsonProperty("msg")
    private final String message;
    /**
     * 成功回复报文包含的对象
     */
    private Object result;

    private CommonResponse(final int code, final String message, final Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取map形式result的builder
     *
     * @param code
     * @param message
     * @return
     */
    public static MapResultBuilder builder4MapResult(final int code, final String message) {
        return new MapResultBuilder(code, message);
    }

    /**
     * 简单形式的response
     *
     * @param code
     * @param message
     * @return
     */
    public static CommonResponse<BaseEntity> simpleResponse(final int code, final String message) {
        return new CommonResponse<BaseEntity>(code, message);
    }

    /**
     * result 为 object的response
     *
     * @param code
     * @param message
     * @param object
     * @return
     */
    public static CommonResponse<BaseEntity> objectResponse(final int code, final String message, final Object object) {
        return new CommonResponse<BaseEntity>(code, message, object);
    }

    public static final class MapResultBuilder {
        /**
         * 返回值
         */
        private final int retCode;
        /**
         * msg
         */
        private final String message;
        /**
         * map形式的result
         */
        private final Map<String, Object> result;

        public MapResultBuilder(int retCode, String message) {
            this.retCode = retCode;
            this.message = message;
            this.result = new HashMap<>();
        }

        public MapResultBuilder addParam(final String key, final Object value) {
            this.result.put(key, value);
            return this;
        }

        public MapResultBuilder addParams(final Map<String, Object> paramMap) {
            this.result.putAll(paramMap);
            return this;
        }

        public CommonResponse<BaseEntity> build() {
            return new CommonResponse<BaseEntity>(retCode, message, result);
        }
    }
}
