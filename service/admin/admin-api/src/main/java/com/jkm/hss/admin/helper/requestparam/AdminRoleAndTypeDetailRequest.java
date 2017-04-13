package com.jkm.hss.admin.helper.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/3/27.
 */
@Data
public class AdminRoleAndTypeDetailRequest {
    /**
     * 角色编码
     */
    private long id;
    /**
     * 代理商编码
     */
    private int type;
}
