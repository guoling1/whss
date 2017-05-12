package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by wayne on 17/5/4.
 */
@Data
public class HsyNoticeResponse {
    /**
     * 公告id
     */
    private int id;

    /**
     * 发布时间
     */
    private String createTime;

    /**
     * 状态
     * tinyint
     */
    private int status;

    /**
     *  发布状态
     */
    private String pushStatus;

    /**
     * 发布标题
     */
    private String title;
    /**
     * 跳转链接
     */
    private String url;
}
