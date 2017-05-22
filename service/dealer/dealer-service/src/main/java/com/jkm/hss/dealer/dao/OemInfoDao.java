package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.OemInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/5/2.
 */
@Repository
public interface OemInfoDao {
    /**
     * 新增
     * @param oemInfo
     */
    void insert(OemInfo oemInfo);
    /**
     * 修改
     * @param oemInfo
     */
    void update(OemInfo oemInfo);

    /**
     *根据分公司编码查询分公司O单配置
     * @return
     */
    OemInfo selectByDealerId(@Param("dealerId") long dealerId);

    /**
     * 根据自生成号查询分公司信息
     * @param oemNo
     * @return
     */
    OemInfo selectByOemNo(@Param("oemNo") String oemNo);
}
