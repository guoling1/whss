package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import com.jkm.hss.admin.enums.EnumAdminType;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/7.
 *
 * 代理商列表  入参
 */
@Data
public class AdminDealerUserListRequest extends PageQueryParams {
    /**
     * 代理商名称
     */
    private long dealerName;

    /**
     * 员工编号
     */
    private String markCode;
    /**
     * 用户姓名
     */
    private String realname;
    /**
     * 手机号
     */
    private String mobile;


    private int offset;

    private int count;
}
