package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by wayne on 17/5/4.
 */
public class AppNotice {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
