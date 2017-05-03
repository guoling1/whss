package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.TemplateInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/5/2.
 */
@Repository
public interface TemplateInfoDao {
    /**
     * 新增
     * @param templateInfo
     */
    void insert(TemplateInfo templateInfo);

    /**
     * 根据O单编码
     * @param oemId
     * @return
     */
    List<TemplateInfo> selectByOemId(@Param("oemId") long oemId);
}
