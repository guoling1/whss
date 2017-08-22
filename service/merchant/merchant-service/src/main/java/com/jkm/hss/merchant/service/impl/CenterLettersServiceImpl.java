package com.jkm.hss.merchant.service.impl;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.merchant.dao.CenterLettersDao;
import com.jkm.hss.merchant.entity.CenterImage;
import com.jkm.hss.merchant.entity.CenterLetters;
import com.jkm.hss.merchant.enums.EnumCommonStatus;
import com.jkm.hss.merchant.helper.request.CenterLettersPublishRequest;
import com.jkm.hss.merchant.helper.request.GetLettersListRequest;
import com.jkm.hss.merchant.helper.response.LettersListResponse;
import com.jkm.hss.merchant.service.CenterLettersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Slf4j
@Service
public class CenterLettersServiceImpl implements CenterLettersService {
    @Autowired
    private CenterLettersDao centerLettersDao;
    @Override
    public void publish(CenterLettersPublishRequest centerLettersPublishRequest) {
        CenterLetters centerLetters = new CenterLetters();
        centerLetters.setAdminId(centerLettersPublishRequest.getAdminId());
        centerLetters.setTitle(centerLettersPublishRequest.getTitle());
        centerLetters.setContent(centerLettersPublishRequest.getContent());
        centerLetters.setStatus(EnumCommonStatus.NORMAL.getId());
        centerLettersDao.insertContent(centerLetters);
        for(int i=0;i<centerLettersPublishRequest.getCenterImages().size();i++){
            centerLettersPublishRequest.getCenterImages().get(i).setLettersId(centerLetters.getId());
        }
        centerLettersDao.insertImage(centerLettersPublishRequest.getCenterImages());
    }

    @Override
    public PageModel<LettersListResponse> getList(GetLettersListRequest getLettersListRequest) {
        PageModel<LettersListResponse> pageModel = new PageModel<LettersListResponse>(getLettersListRequest.getPageNo(), getLettersListRequest.getPageSize());
        getLettersListRequest.setOffset(pageModel.getFirstIndex());
        Long count = centerLettersDao.getLettersCount(getLettersListRequest);
        List<LettersListResponse> lettersListResponses =  centerLettersDao.getLettersList(getLettersListRequest);
        pageModel.setCount(count);
        pageModel.setRecords(lettersListResponses);
        return pageModel;
    }

    @Override
    public void update(CenterLettersPublishRequest centerLettersPublishRequest) {
        CenterLetters centerLetters = new CenterLetters();
        centerLetters.setId(centerLettersPublishRequest.getId());
        centerLetters.setTitle(centerLettersPublishRequest.getTitle());
        centerLetters.setContent(centerLettersPublishRequest.getContent());
        centerLetters.setStatus(EnumCommonStatus.NORMAL.getId());
        centerLettersDao.update(centerLetters);
        centerLettersDao.deleteImage(centerLettersPublishRequest.getId());
        for(int i=0;i<centerLettersPublishRequest.getCenterImages().size();i++){
            centerLettersPublishRequest.getCenterImages().get(i).setLettersId(centerLetters.getId());
        }
        centerLettersDao.insertImage(centerLettersPublishRequest.getCenterImages());
    }

    @Override
    public CenterLetters selectById(Long id) {
        return centerLettersDao.selectById(id);
    }

    @Override
    public List<CenterImage> getImageList(Long lettersId) {
        return centerLettersDao.getImageList(lettersId);
    }

}
