package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Data
public class CenterLetters extends BaseEntity {
    private Long adminId;
    private String title;
    private String content;
    private Long downloadCount;
}
