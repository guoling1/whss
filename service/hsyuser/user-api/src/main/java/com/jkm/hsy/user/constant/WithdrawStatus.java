package com.jkm.hsy.user.constant;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by Allen on 2017/7/31.
 */
public enum WithdrawStatus {
    NORMAL(1,"审核成功"),
    REGISTING(2,"注册中"),
    COMMIT_CHECKING(3,"提交结算待审核"),
    COMMIT_NOT_PASS(4,"审核驳回"),
    REJECT(5,"审核拒绝"),
   ;
    @Getter
    public int key;
    @Getter
    public String value;

    WithdrawStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private static final ImmutableMap<Integer, WithdrawStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, WithdrawStatus> builder = new ImmutableMap.Builder<>();
        for (WithdrawStatus status : WithdrawStatus.values()) {
            builder.put(status.getKey(), status);
        }
        INIT_MAP = builder.build();
    }

    public static WithdrawStatus of(final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }
}
