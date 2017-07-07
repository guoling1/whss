package com.jkm.hss.merchant.helper.request;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/24.
 */
@Data
public class BankBranchRequest extends PageQueryParams {
    /**
     *开户行
     */
    private String bankName;
    /**
     * 地区编码
     */
    private String districtCode;
    /**
     * 查询条件
     */
    private String contions;
    /**
     *
     */
    private long offset;
    /**
     *
     */
    private int count;


}
