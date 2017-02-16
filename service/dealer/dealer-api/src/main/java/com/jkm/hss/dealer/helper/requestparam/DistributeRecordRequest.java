package com.jkm.hss.dealer.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/8.
 */
@Data
public class DistributeRecordRequest extends PageQueryParams {
    /**
     * 代理商编号
     */
    private String markCode;
    /**
     * 代理商名称
     */
    private String name;
    /**
     * 上级代理商编号
     */
    private String firstMarkCode;
    /**
     * 上级代理商名称
     */
    private String firstName;

}
