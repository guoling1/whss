package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by Thinkpad on 2016/12/30.
 * 全局设置
 * tb_global_config
 */
@Data
public class GlobalConfig extends BaseEntity {
    /**
     *配置key
     */
    private String configKey;

    /**
     *配置值
     */
    private String configValue;
}
