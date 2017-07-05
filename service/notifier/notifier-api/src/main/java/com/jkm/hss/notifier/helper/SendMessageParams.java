package com.jkm.hss.notifier.helper;

import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16/8/7.
 *
 * @author hutao
 * @version 1.0
 */
@Getter
@Builder
@ToString
public class SendMessageParams {
    private String uid;
    private EnumUserType userType;
    private EnumNoticeType noticeType;
    private String mobile;
    @Singular(value = "addMapData")
    private Map<String, ?> data = new HashMap<>();
    private Date sendTime;

    public Pair<Boolean, String> checkParamsCorrect() {
        if (getNoticeType() == null) {
            return Pair.of(false, "通知类型不能为空");
        }
        if (getUserType() == null) {
            return Pair.of(false, "用户类型不能为空");
        }

        if (getMobile() == null) {
            return Pair.of(false, "手机号不能为空");
        }

//        if (getUid() < 0) {
//            return Pair.of(false, "用户id不正确");
//        }

        return Pair.of(true, "");
    }
}
