package com.jkm.hss.push.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by longwen.jiang on 2017/01/10.
 *
 * 消息推送
 *
 * tb_push_message_record
 */
@Data
public class Push extends BaseEntity {




    private String pid;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 模板类型
     */
    private String tempType;

    /**
     * 推送类型
     */
    private String pushType;

    /**
     * clientID
     */
    private String clientId;

    /**
     * targets
     */
    private String targets;

    /**
     * 任务id
     */
    private String taskId;

}