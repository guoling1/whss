package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.BankBranch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/23.
 */
@Repository
public interface BankBranchDao {
    /**
     *根据支行名称和查询条件查询
     * @param bankName
     * @param contions
     * @return
     */
    List<BankBranch> findByBankName(@Param("bankName") String bankName,@Param("contions") String contions,@Param("district") String district);
}
