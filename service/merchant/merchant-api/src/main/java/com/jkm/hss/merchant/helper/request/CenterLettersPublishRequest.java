package com.jkm.hss.merchant.helper.request;

import com.jkm.hss.merchant.entity.CenterImage;
import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Data
public class CenterLettersPublishRequest {
    private Long id;
    private Long adminId;
    private String title;
    private String content;
    private List<CenterImage> centerImages;
}
