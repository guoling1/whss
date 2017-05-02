package com.jkm.hss.admin.helper.responseparam;

import com.jkm.hss.admin.enums.EnumIsSelected;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/27.
 */
@Data
public class AdminOptRelResponse {
    /**
     * 编码
     */
    private long id;

    /**
     * 展示名称
     */
    private String showName;

    /**
     * 是否选中（1选中 2未选中）
     * {@link EnumIsSelected}
     */
    private int isSelected;
}
