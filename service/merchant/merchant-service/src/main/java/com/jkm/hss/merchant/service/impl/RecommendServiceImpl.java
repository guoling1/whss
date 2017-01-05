package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.RecommendDao;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;
import com.jkm.hss.merchant.entity.RecommendShort;
import com.jkm.hss.merchant.enums.EnumRecommendType;
import com.jkm.hss.merchant.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Service
public class RecommendServiceImpl implements RecommendService{
    @Autowired
    private RecommendDao recommendDao;
    /**
     * 初始化
     *
     * @param recommend
     */
    @Override
    public int insert(Recommend recommend) {
        return recommendDao.insert(recommend);
    }

    /**
     * 修改
     *
     * @param recommend
     * @return
     */
    @Override
    public int update(Recommend recommend) {
        return recommendDao.update(recommend);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Recommend> selectById(long id) {
        return Optional.fromNullable(recommendDao.selectById(id));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<Recommend> selectAll() {
        return recommendDao.selectAll();
    }

    /**
     * 我推广的好友
     *
     * @param merchantId
     * @return
     */
    @Override
    public RecommendAndMerchant myRecommend(long merchantId) {
        List<RecommendShort> list = recommendDao.myRecommend(merchantId);
        RecommendAndMerchant recommendAndMerchant = new RecommendAndMerchant();
        int direct = 0;
        int indirect = 0;
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                if (EnumRecommendType.INDIRECT.getId()==list.get(i).getType()){
                    list.get(i).setName("*"+list.get(i).getName().substring(list.get(i).getName().length()-1,list.get(i).getName().length()));
                    indirect++;
                }else{
                    direct++;
                }
            }
        }
        recommendAndMerchant.setRecommends(list);
        recommendAndMerchant.setDirectCount(direct);
        recommendAndMerchant.setIndirectCount(indirect);
        return recommendAndMerchant;
    }

    /**
     * 我的真实好友数量（验证通过并且消费达标）
     *
     * @param merchantId
     * @return
     */
    @Override
    public int selectFriendCount(long merchantId) {
        return recommendDao.selectFriendCount(merchantId);
    }

}
