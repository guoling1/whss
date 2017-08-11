package com.jkm.hss.merchant.service;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.merchant.entity.CenterImage;
import com.jkm.hss.merchant.entity.CenterLetters;
import com.jkm.hss.merchant.helper.request.CenterLettersPublishRequest;
import com.jkm.hss.merchant.helper.request.GetLettersListRequest;
import com.jkm.hss.merchant.helper.response.LettersListResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/10.
 */
public interface CenterLettersService {
    void publish(CenterLettersPublishRequest centerLettersPublishRequest);
    PageModel<LettersListResponse> getList(GetLettersListRequest getLettersListRequest);
    void update(CenterLettersPublishRequest centerLettersPublishRequest);
    CenterLetters selectById(Long id);
    List<CenterImage> getImageList(Long lettersId);
}
