package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.DealerRecommendDao;
import com.jkm.hss.merchant.dao.RecommendDao;
import com.jkm.hss.merchant.entity.DealerRecommend;
import com.jkm.hss.merchant.entity.Recommend;
import com.jkm.hss.merchant.entity.RecommendAndMerchant;
import com.jkm.hss.merchant.entity.RecommendShort;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.enums.EnumRecommendType;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.DealerRecommendRequest;
import com.jkm.hss.merchant.helper.request.RecommendRequest;
import com.jkm.hss.merchant.service.DealerRecommendService;
import com.jkm.hss.merchant.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Service
public class DealerRecommendServiceImpl implements DealerRecommendService {
    @Autowired
    private DealerRecommendDao dealerRecommendDao;
    /**
     * 初始化
     *
     * @param dealerRecommend
     */
    @Override
    public int insert(DealerRecommend dealerRecommend) {
        return dealerRecommendDao.insert(dealerRecommend);
    }


    /**
     * 按分页查询好友列表
     *
     * @param dealerRecommendRequest
     * @return
     */
    @Override
    public RecommendAndMerchant selectRecommend(DealerRecommendRequest dealerRecommendRequest) {
        int direct = 0;
        int indirect = 0;
        List<RecommendShort> list = dealerRecommendDao.selectRecommendByPage(dealerRecommendRequest);
        RecommendAndMerchant recommendAndMerchant = new RecommendAndMerchant();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                if (EnumRecommendType.INDIRECT.getId()==list.get(i).getType()){//间接推广人
                    if(list.get(i).getName()!=null&&!"".equals(list.get(i).getName())){
                        list.get(i).setName("*"+list.get(i).getName().substring(list.get(i).getName().length()-1,list.get(i).getName().length()));
                    }else{
                        String mobile = MerchantSupport.decryptMobile(list.get(i).getMobile());
                        list.get(i).setName(mobile.substring(0,3)+"****"+mobile.substring(mobile.length()-4,mobile.length()));
                    }
                    list.get(i).setStatusName(getStatusName(list.get(i).getStatus()));
                    indirect++;
                }else{//直接推广人
                    if(list.get(i).getName()!=null&&!"".equals(list.get(i).getName())){
                        list.get(i).setName(list.get(i).getName());
                    }else{
                        String mobile = MerchantSupport.decryptMobile(list.get(i).getMobile());
                        list.get(i).setName(mobile);
                    }
                    list.get(i).setStatusName(getStatusName(list.get(i).getStatus()));
                    direct++;
                }
            }
        }
        recommendAndMerchant.setRecommends(list);
        recommendAndMerchant.setDirectCount(direct);
        recommendAndMerchant.setIndirectCount(indirect);
        return recommendAndMerchant;
    }

    private String getStatusName(int status){
        String name = "";
        if(status==EnumMerchantStatus.LOGIN.getId()){
            name=EnumMerchantStatus.LOGIN.getName();
        }
        if(status==EnumMerchantStatus.INIT.getId()){
            name=EnumMerchantStatus.INIT.getName();
        }
        if(status==EnumMerchantStatus.ONESTEP.getId()){
            name=EnumMerchantStatus.ONESTEP.getName();
        }
        if(status==EnumMerchantStatus.REVIEW.getId()){
            name=EnumMerchantStatus.REVIEW.getName();
        }
        if(status==EnumMerchantStatus.PASSED.getId()){
            name=EnumMerchantStatus.PASSED.getName();
        }
        if(status==EnumMerchantStatus.UNPASSED.getId()){
            name=EnumMerchantStatus.UNPASSED.getName();
        }
        if(status==EnumMerchantStatus.FRIEND.getId()){
            name=EnumMerchantStatus.FRIEND.getName();
        }
        return name;
    }
}
