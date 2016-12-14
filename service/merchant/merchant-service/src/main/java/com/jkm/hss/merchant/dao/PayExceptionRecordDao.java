package com.jkm.hss.merchant.dao;



import com.jkm.hss.merchant.entity.PayExceptionRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayExceptionRecordDao {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    PayExceptionRecord selectByPrimaryKey(@Param("id") Long id);

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(PayExceptionRecord record);

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective(PayExceptionRecord record);
}
