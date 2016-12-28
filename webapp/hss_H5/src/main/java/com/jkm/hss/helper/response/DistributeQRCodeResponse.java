package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * 一级代理商 给自己和一级代理商分配二维码  出参
 *
 */
@Data
public class DistributeQRCodeResponse {

    /**
     * 一级代理商id
     */
    private long firstLevelDealerId;

    /**
     * 把二维码分配的代理商id
     */
    private long toDealerId;

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
    private String distributeDate;

    /**
     * 分配个数
     */
    private int count;

    /**
     * code
     */
    private List<Code> codes;

    @Data
    public class Code {
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
