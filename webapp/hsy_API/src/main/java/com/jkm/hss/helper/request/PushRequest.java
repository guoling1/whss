package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by Administrator on 2017/1/18.
 */

@Data
public class PushRequest {


   // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动。

    private String setType;

    //1: 单发， 2：多发， 3： 群发   。
    private String pushType;

    // 发送消息的内容。
    private String content;

    //客户端ID
    private String clientId;

    //用户ID
    private String uid;

    //店铺ID
    private String sid;
}
