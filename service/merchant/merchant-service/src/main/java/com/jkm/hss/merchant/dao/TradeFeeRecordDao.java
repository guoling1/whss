package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.TradeFeeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * TradeFeeRecordDao数据库操作接口类
 * 
 **/
@Repository
public interface TradeFeeRecordDao{


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	TradeFeeRecord selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(TradeFeeRecord record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKey(TradeFeeRecord record);

}