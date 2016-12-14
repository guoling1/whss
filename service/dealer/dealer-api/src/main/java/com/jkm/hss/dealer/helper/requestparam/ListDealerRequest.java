package com.jkm.hss.dealer.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/7.
 *
 * 代理商列表  入参
 */
@Data
public class ListDealerRequest extends PageQueryParams {

    /**
     * 代理商id
     */
    private long id;
    /**
     * 代理商名字
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 所属区域
     */
    private String belongArea;
    /**
     * 代理商级别
     * {@link com.jkm.hss.dealer.enums.EnumDealerLevel}
     */
    private int level;

    private int offset;

    private int count;
}
