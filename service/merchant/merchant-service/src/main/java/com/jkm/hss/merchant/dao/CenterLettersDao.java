package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.CenterImage;
import com.jkm.hss.merchant.entity.CenterLetters;
import com.jkm.hss.merchant.helper.request.GetLettersListRequest;
import com.jkm.hss.merchant.helper.response.LettersListResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/10.
 */
@Repository
public interface CenterLettersDao {
    Long insertContent(CenterLetters centerLetters);
    void insertImage(List<CenterImage> list);
    void update(CenterLetters centerLetters);
    List<LettersListResponse> getLettersList(GetLettersListRequest getLettersListRequest);
    List<CenterImage> getImageList(@Param("lettersId")Long lettersId);
    Long getLettersCount(GetLettersListRequest getLettersListRequest);
    void PlusCount(@Param("lettersId")Long lettersId);
    void deleteImage(@Param("lettersId")Long lettersId);
    CenterLetters selectById(@Param("lettersId")Long lettersId);

}
