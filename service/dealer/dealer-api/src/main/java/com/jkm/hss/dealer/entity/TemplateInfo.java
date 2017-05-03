package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumSignCode;
import lombok.Data;

/**
 * Created by xingliujie on 2017/5/2.
 */
@Data
public class TemplateInfo extends BaseEntity {
    /**
     * 模板标示
     * {@link EnumSignCode}
     */
    private int signCode;
    /**
     * O单编码
     */
    private long oemId;
    /**
     * 模板id
     */
    private String templateId;
    /**
     * 模板名称
     */
    private String templateName;
}
