package com.jkm.hss.dealer.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/2/15.
 */
@Data
public class DistributeRecordResponse {
    /**
     *分配时间
     */
    private Date distributeTime;
    /**
     * 代理名称(唯一)
     */
    private String proxyName;
    /**
     * 代理商标示
     */
    private String markCode;
    /**
     * 个数
     */
    private int count;
    /**
     * 开始码号
     */
    private String startCode;

    /**
     * 结束码号
     */
    private String endCode;
    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeDistributeType}
     */
    private int type;
    /**
     * 操作人
     */
    private String operateUser;
}
