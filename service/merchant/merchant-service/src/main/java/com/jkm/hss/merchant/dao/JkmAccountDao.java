package com.jkm.hss.merchant.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface JkmAccountDao{
	/**
	 * 
	 * 入账
	 * 
	 **/
	int  income(@Param("money") BigDecimal money,@Param("totalMoney") BigDecimal totalMoney,@Param("id") long id);


	/**
	 *
	 * 出账
	 *
	 **/
	int  outMoney(@Param("money") BigDecimal money,@Param("id") long id);
}