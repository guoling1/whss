package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * 一级代理商 给自己和二级级代理商分配二维码   入参
 */
@Data
public class DistributeQRCodeRequest {

    /**
     * 需要二维码的代理商id
     */
    private long dealerId;

    /**
     * 是否分配给自己（0：非，1：是）
     */
    private int isSelf;

    /**
     * 个数
     */
    private int count;
}
