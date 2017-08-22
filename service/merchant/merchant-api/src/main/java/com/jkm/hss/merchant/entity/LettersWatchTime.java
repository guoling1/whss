package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/8/22.
 */
@Data
public class LettersWatchTime {
    private Long id;
    private Long merchantId;
    private Date lastTime;
}
