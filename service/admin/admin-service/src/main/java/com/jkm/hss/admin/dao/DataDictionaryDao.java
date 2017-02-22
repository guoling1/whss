package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.DataDictionary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/22.
 */
@Repository
public interface DataDictionaryDao {
    /**
     * 新增
     * @param dataDictionary
     */
    void insert(DataDictionary dataDictionary);

    /**
     * 根据类型查询列表
     * @param dictType
     * @return
     */
    List<DataDictionary> selectAllByType(@Param("dictType") String dictType);
}
