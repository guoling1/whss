package com.jkm.base.common.util;


import com.jkm.base.common.enums.EnumGlobalAdminUserLevel;
import com.jkm.base.common.enums.EnumGlobalDealerLevel;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import lombok.experimental.UtilityClass;

/**
 * Created by lk on 29/12/2016.
 */
@UtilityClass
public class GlobalID {

    /**
     * 生成全局对外ID
     * @param enumGlobalIDType
     * @param enumGlobalIDPro
     * @param id
     * @return
     */
    public static String GetGlobalID(EnumGlobalIDType enumGlobalIDType, EnumGlobalIDPro
            enumGlobalIDPro,String id){

        StringBuffer sb = new StringBuffer();
        sb.append(enumGlobalIDType.getValue());
        sb.append(enumGlobalIDPro.getValue());
        sb.append("0000000000");

        int begin = sb.length()-id.length();
        int end = sb.length();
        sb.replace(begin,end,id);

        return sb.toString();
    }

    /**
     * 生成代理商邀请码
     * @param enumGlobalDealerLevel
     * @param id
     * @return
     */
    public static String GetInviteID(EnumGlobalDealerLevel enumGlobalDealerLevel, String id){
        StringBuffer sb = new StringBuffer();
        sb.append(enumGlobalDealerLevel.getValue());
        sb.append("00000");

        int begin = sb.length()-id.length();
        int end = sb.length();
        sb.replace(begin,end,id);

        return sb.toString();
    }

    /**
     * 生成员工编码
     * @param enumGlobalAdminUserLevel
     * @param id
     * @return
     */
    public static String GetAdminUserID(EnumGlobalAdminUserLevel enumGlobalAdminUserLevel, String id){
        StringBuffer sb = new StringBuffer();
        sb.append(enumGlobalAdminUserLevel.getValue());
        sb.append("00000");
        int begin = sb.length()-id.length();
        int end = sb.length();
        sb.replace(begin,end,id);
        return sb.toString();
    }
}
