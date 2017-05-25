package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.UpgradeRulesDao;
import com.jkm.hss.product.helper.response.UpgradeResult;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumIsUpGrade;
import com.jkm.hss.product.servcie.UpgradeRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Service
public class UpgradeRulesServiceImpl implements UpgradeRulesService{
    @Autowired
    private UpgradeRulesDao upgradeRulesDao;
    /**
     * 初始化
     *
     * @param upgradeRules
     */
    @Override
    public int insert(UpgradeRules upgradeRules) {
        return upgradeRulesDao.insert(upgradeRules);
    }

    /**
     * 修改
     *
     * @param upgradeRules
     * @return
     */
    @Override
    public int update(UpgradeRules upgradeRules) {
        return upgradeRulesDao.update(upgradeRules);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<UpgradeRules> selectById(long id) {
        return Optional.fromNullable(upgradeRulesDao.selectById(id));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<UpgradeRules> selectAll(long productId) {
        return upgradeRulesDao.selectByProductId(productId);
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<UpgradeResult> selectUpgradeList(long productId,int level) {
        List<UpgradeResult> result = new ArrayList<UpgradeResult>();
        List<UpgradeRules> list = upgradeRulesDao.selectByProductId(productId);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                UpgradeResult upgradeResult = new UpgradeResult();
                upgradeResult.setId(list.get(i).getId());
                upgradeResult.setName(list.get(i).getName());
                upgradeResult.setType(list.get(i).getType());
                if(list.get(i).getType()>=level){
                    upgradeResult.setIsUpgrade(EnumIsUpGrade.UPGRADE.getId());
                }else{
                    upgradeResult.setIsUpgrade(EnumIsUpGrade.NOTUPGRADE.getId());
                }
                result.add(upgradeResult);
            }
        }
        return result;
    }

    /**
     * 根据产品编码和类型查询
     * @param productId
     * @param type
     * @return
     */
    @Override
    public Optional<UpgradeRules> selectByProductIdAndType(long productId, int type) {
        return Optional.fromNullable(upgradeRulesDao.selectByProductIdAndType(productId,type));
    }
}
