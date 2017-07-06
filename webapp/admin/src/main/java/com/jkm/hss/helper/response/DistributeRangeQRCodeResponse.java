package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/14.
 *
 * 按码段分配二维码 入参
 */
@Data
public class DistributeRangeQRCodeResponse {

    /**
     * 一级代理商id
     */
    private long dealerId;

    /**
     * 代理商名称
     */
    private String name;

    /**
     * 代理商手机号
     */
    private String mobile;

    /**
     * 分配时间
     */
    private Date distributeDate;

    /**
     * 分配个数
     */
    private int count;

    private List<Code> codes;

    @Data
    public class Code{
        /**
         * 开始码段
         */
        private String startCode;

        /**
         * 结束码段
         */
        private String endCode;
    }
}
