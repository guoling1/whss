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
     * 修改
     * @param templateInfo
     */
    void update(TemplateInfo templateInfo);

    /**
     * 根据O单编码
     * @param oemId
     * @return
     */
    List<TemplateInfo> selectByOemId(@Param("oemId") long oemId);

    /**
     * 根据标示和O单编码查询
     * @param signCode
     * @param oemId
     * @return
     */
    TemplateInfo selectBySignCodeAndOemId(@Param("signCode") int signCode,@Param("oemId") long oemId);
}
