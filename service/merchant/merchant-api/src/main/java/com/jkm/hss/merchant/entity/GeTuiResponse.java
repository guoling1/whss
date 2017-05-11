package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/5/5.
 */
@Data
public class GeTuiResponse {

    /**
     * 应用ID
     */
    private String appid;

    /**
     * 接收人CID
     */
    private String cid;

    /**
     * 任务ID（即ContentID）
     */
    private String taskid;

    /**
     * 消息ID
     */
    private String msgid;

    /**
     *
     * 结果码(200 -- 成功，400 -- 推送苹果接口失败， 401 -- 用户不存在，402 -- 非活跃用户，500 -- 系统内部异常)
     *目前只有200和400，后续版本扩展。
     */
    private String code;

    /**
     * 推送结果描述
     */
    private String desc;

    /**
     * 推送结果描述
     */
    private String descr;

    /**
     * Sign=MD5（appid+cid+taskid+msgid+masterSecret）
     * 第三方通过该公式使用mastersecret计算出自己的Sign’值，
     * 如果sign == sign’则表示该请求合法。
     *
     */
    private String sign;

    /**
     * 回执ID
     */
    private String actionId;

}
