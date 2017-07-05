package com.jkm.hss.helper.request;

import lombok.Data;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/16.
 *
 * 批量结算
 */
@Data
public class BatchSettleRequest {

    private List<Long> recordIds;
}
