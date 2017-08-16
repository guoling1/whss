package com.jkm.api.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/8/15.
 */
public enum JKMTradeErrorCode {

    /**成功*/
    SUCCESS("S0000","交易成功"),
    ACCEPT_SUCCESS("S0001","接受成功"),

    /**业务相关*/
    FAIL("E1001","交易失败"),
    REQUEST_MESSAGE_ERROR("E1002","报文不合法"),
    MERCHANT_NOT_EXIST("E1003","商户不存在"),
    CHECK_SIGN_FAIL("E1004","验证签名失败"),
    PROCESSING("E1005","交易处理中"),
    MERCHANT_TRADE_NO_EXIST("E1006","商户订单号重复"),
    NEED_PWD("E1007","用户支付中，需要输入密码"),
    CHANNEL_NOT_SUPPORT("E1008","暂不支持该通道"),
    ORDER_EXSIT_SUCC("E1009","订单已存在且支付成功"),
    ORDER_CLOSED("E1010","订单已关闭"),
    ORDER_NOT_EXIST("E1011","订单不存在"),
    NO_VALID_CHANNEL("E1012","无可用通道"),
    SURPLUS_CHANNEL("E1013","多余的错误通道数据"),
    NOT_ALLOW_USE("E1014","不具备接口使用权限"),
    AMOUNT_NOT_ENOUGH("E1015","金额不足"),
    ORDER_EXSIT("E1016","订单已存在"),
    FEE_CAL_ERROR("E1017","手续费计算异常"),
    MCT_NOT_REPORT("E1018","商户未报备"),
    NOT_SUPORT_CARD("E1019","不支持卡类型"),
    BANK_ERROR("E1020","银行系统异常"),
    REFUND_FEE_INVALID("E1021","退款金额大于支付金额"),
    ORDER_REFUND("E1022","订单已退款"),
    AUTH_CODE_EXPIRED("E1023","授权码失效，请重试"),
    ORDER_REVOKED("E1024","订单已撤销"),
    NEED_ORDER_NO("E1025","请至少传递一个查询流水"),
    PMT_NOT_EXSIT("E1026","支付记录不存在"),
    CHANNEL_NOT_EXSIT("E1027","通道不存在"),
    REFUNDING_EXIST("E1028","退款失败，存在退款中的订单"),
    REFUNDING_ORDER_STATUS_ERROR("E1029","退款失败，订单状态不符，请确认订单状态"),
    DECRYPT_ERROR("E1030","解密失败"),
    ENCRPYT_ERROR("E1031","加密失败"),
    SIGN_ERROR("E1032","签名失败"),
    ACCOUNT_NOT_EXIST("E1033","账户信息不存在"),
    NEED_REF_NO("E1034","退款请求号不能为空"),
    REFUND_NOT_EXSIT("E1035","退款记录不存在"),
    NEED_REFUND_ORDER_NO("E1036","请至少传递一个订单号用于退款"),
    REFUND_AMOUNT_ILLEGAL("E1037","退款金额不合法"),
    AMOUNT_NOT_SAME("E1038","与原交易金额不一致"),
    MERCHANT_BUS_NOT_EXIST("E1039","商户业务信息不存在"),
    REVERSE_EXPIRE("E1040","订单已超出撤销截止时间"),
    NOT_SUPPORT_PAY_METHOD("E1041","不支持的支付方法"),
    KEY_NOT_EXIST("E1042","秘钥未配置"),
    NO_THIS_CHECK_FILE("E1043","无对账文件"),
    QUERY_FREQUENT("E1044","查询过于频繁"),
    LOSE_MONEY("E1045","商户手续费低于银行成本"),
    QUERY_LATER("E1046","支付时间未超5分钟，请稍后查询"),
    INIT_QUERY_LATER("E1047","订单初始化中，请稍后查询"),
    MER_IS_FROZEN("E1048","商户被冻结"),
    PARAM_NOT_NULL("E1049","参数不能为空"),
    FORMAT_ERROR("E1050","格式错误"),
    DEALER_NOT_EXIST("E1051","代理商不存在"),
    MCT_EXIST("E1052","商户已入网"),
    PRODUCT_NOT_EXIST("E1053","产品不存在"),


    /**数据库*/
    INSERT_ERROR("E2001","插入数据异常"),
    UPDATE_ERROR("E2002","更新数据异常"),
    DELETE_ERROR("E2003","删除数据异常"),
    QUERY_ERROR("E2004","查询数据异常"),

    /**系统层面 或无法定位的异常*/
    SYS_ERROR("E3001","系统异常"),
    SERVICE_ERROR("E3002","服务异常"),
    SYS_RET_NULL("E3003","系统返回为空"),
    READ_PARAM_ERROR("E3004","解析数据异常"),
    IO_EXCEPTOIN("E3005","网络异常"),
    UNKOWN_ERROR("E3006","未知错误"),
    COMMON_ERROR("E3007","自定义错误信息"),//使用该错误码  需要自定义错误信息 并配合固定的TradeException构造方法

    /**结算相关**/
    MER_BUS_NOT_EXIST("E4001","商户业务信息不存在"),
    MER_ACT_NOT_EXIST("E4002","商户账户信息不存在"),
	UNKNOW_BANK_ERROR("E4004","银行其他未知异常"),
    ;

    @Getter
    private String errorCode;
    @Getter
    private String errorMessage;

     JKMTradeErrorCode(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
