package com.jkm.hss.merchant.dao;


import com.jkm.hss.merchant.entity.AccountInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 
 * AccountInfoDao数据库操作接口类
 * 
 **/
@Repository
public interface AccountInfoDao{


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	AccountInfo selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(AccountInfo record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(AccountInfo record);

	int addAvailableMoney(@Param("id") Long id,@Param("available") BigDecimal available);

	int addUnsettledMoney(@Param("id") Long id,@Param("unsettled") BigDecimal unsettled);

	int frozenMoney(@Param("frozenAmount") BigDecimal frozenAmount,@Param("id") Long id);

	int inCome(@Param("frozenAmount") BigDecimal frozenAmount,@Param("id") Long id);

	/**
	 * 代付异常，退款
	 * @param frozenAmount
	 * @param id
	 * @return
	 */
	int backMoney(@Param("frozenAmount") BigDecimal frozenAmount,@Param("id") Long id);

}