package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/19.
 *
 * 查询代理商某一二维码范围下的未分配的二维码个数  入参
 */
@Data
public class FindUnDistributeCodeCountRequest {

    private String startCode;

    private String endCode;
}
