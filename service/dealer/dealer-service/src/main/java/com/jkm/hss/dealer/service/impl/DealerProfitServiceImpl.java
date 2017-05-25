package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerProfitDao;
import com.jkm.hss.dealer.entity.DealerProfit;
import com.jkm.hss.dealer.helper.response.DealerProfitSettingResponse;
import com.jkm.hss.dealer.service.DealerProfitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xingliujie on 2017/5/8.
 */
@Slf4j
@Service
public class DealerProfitServiceImpl implements DealerProfitService {
    @Autowired
    private DealerProfitDao dealerProfitDao;
    /**
     * 插入
     *
     * @param dealerProfit
     */
    @Override
    public void insert(DealerProfit dealerProfit) {
        dealerProfitDao.insert(dealerProfit);
    }

    /**
     * 修改
     *
     * @param dealerProfit
     */
    @Override
    public void update(DealerProfit dealerProfit) {
        dealerProfitDao.update(dealerProfit);
    }

    /**
     * 根据代理商编码和产品编码查询代理桑合伙人推荐默认设置
     *
     * @param dealerId
     * @param productId
     * @return
     */
    @Override
    public List<DealerProfitSettingResponse> selectDealerByDealerIdAndProductId(long dealerId, long productId) {
        List<DealerProfitSettingResponse> dealerProfitSettingResponses = dealerProfitDao.selectDealerByDealerIdAndProductId(dealerId,productId);
        if(dealerProfitSettingResponses.size()>0){
            for(int i=0;i<dealerProfitSettingResponses.size();i++){
                if(dealerProfitSettingResponses.get(i).getProfitSpace()!=null){
                    dealerProfitSettingResponses.get(i).setProfitSpace(dealerProfitSettingResponses.get(i).getProfitSpace().multiply(new BigDecimal("100")));
                }
            }
        }
        return dealerProfitSettingResponses;
    }

    /**
     * 根据代理商编码和产品编码查询
     *
     * @param productId
     * @return
     */
    @Override
    public List<DealerProfitSettingResponse> selectByDealerIdAndProductId(long productId) {
        List<DealerProfitSettingResponse> dealerProfitSettingResponses = dealerProfitDao.selectByDealerIdAndProductId(productId);
        if(dealerProfitSettingResponses.size()>0){
            for(int i=0;i<dealerProfitSettingResponses.size();i++){
                if(dealerProfitSettingResponses.get(i).getProfitSpace()!=null){
                    dealerProfitSettingResponses.get(i).setProfitSpace(dealerProfitSettingResponses.get(i).getProfitSpace().multiply(new BigDecimal("100")));
                }
            }
        }
        return dealerProfitSettingResponses;
    }

    /**
     * 查询某通道是否已经设置过分润空间
     *
     * @param dealerId
     * @param productId
     * @param channelTypeSign
     * @return
     */
    @Override
    public Optional<DealerProfit> selectByDealerIdAndProductIdAndChannelTypeSign(long dealerId, long productId, Integer channelTypeSign) {
        return Optional.fromNullable(dealerProfitDao.selectByDealerIdAndProductIdAndChannelTypeSign(dealerId,productId,channelTypeSign));
    }
}
