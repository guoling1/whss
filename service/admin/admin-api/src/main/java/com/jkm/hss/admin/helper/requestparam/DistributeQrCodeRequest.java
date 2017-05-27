package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType2;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class DistributeQrCodeRequest  extends PageQueryParams {
    /**
     * 代理商编号
     */
    private String markCode;
    /**
     * 代理商名称
     */
    private String name;
    /**
     *上级代理编码
     */
    private String firstMarkCode;
    /**
     *上级代理名称
     */
    private String firstName;
    /**
     * 操作人（0全部 1boss 2代理商 3分公司）
     * {@link EnumQRCodeDistributeType2}
     */
    private int type;
    /**
     *
     */
    private int offset;
    /***
     *
     */
    private int count;
}
