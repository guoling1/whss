package com.jkm.hss.merchant.dao;


import com.jkm.hss.merchant.entity.WxConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * WxConfigDao数据库操作接口类
 * 
 **/
@Repository
public interface WxConfigDao{

	WxConfig selectTop1();
	WxConfig selectTop1ByAppId(@Param("appId") String appId);
	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	WxConfig selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(WxConfig record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(WxConfig record);

}