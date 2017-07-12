package com.jkm.hss.admin.helper.responseparam;

import com.jkm.base.common.entity.PageModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class QrCodeListPageResponse<T>  {
    /**
     * 未分配个数
     */
    private int unDistributeCount;
    /**
     * 已分配个数
     */
    private int distributeCount;
    /**
     * 未激活个数
     */
    private int unActivateCount;
    /**
     * 已激活个数
     */
    private int activateCount;
    /**
     * 返回集合
     */
    PageModel<T> pageModel;
}
