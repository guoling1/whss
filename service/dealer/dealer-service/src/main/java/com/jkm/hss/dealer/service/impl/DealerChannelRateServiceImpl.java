package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.dao.DealerChannelRateDao;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
@Service
public class DealerChannelRateServiceImpl implements DealerChannelRateService{

    @Autowired
    private DealerChannelRateDao dealerChannelRateDao;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private DealerService dealerService;
    /**
     * {@inheritDoc}
     * @param dealerChannelRate
     */
    @Override
    public void init(DealerChannelRate dealerChannelRate) {
        this.dealerChannelRateDao.init(dealerChannelRate);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param productId
     * @return
     */
    @Override
    public List<DealerChannelRate> selectByDealerIdAndProductId(long id, long productId) {
        return this.dealerChannelRateDao.selectByDealerIdAndProductId(id, productId);
    }

    @Override
    public List<DealerChannelRate> selectByDealerId(long dealerId) {
        return this.dealerChannelRateDao.selectByDealerId(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param payChannel
     * @return
     */
    @Override
    public List<DealerChannelRate> selectByDealerIdAndPayChannelSign(long dealerId, int payChannel) {
        return this.dealerChannelRateDao.selectByDealerIdAndPayChannelSign(dealerId, payChannel);
    }

    /**
     * 根据代理商id、产品id与通道标识查询代理商费率
     *
     * @param dealerId
     * @param productId
     * @param channelType
     * @return
     */
    @Override
    public Optional<DealerChannelRate> selectByDealerIdAndProductIdAndChannelType(long dealerId, long productId, int channelType) {
        return Optional.fromNullable(this.dealerChannelRateDao.selectByDealerIdAndProductIdAndChannelType(dealerId,productId,channelType));
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @return
     */
    @Override
    public Triple<BigDecimal, BigDecimal, BigDecimal> getMerchantRateByDealerId(long dealerId, long productId) {

        BigDecimal wxRate = null;
        BigDecimal zfbRate = null;
        BigDecimal ylRate = null;
        if ( dealerId == 0){
            // 没有代理商，公司直属商户
            final List<ProductChannelDetail> productChannelDetails = this.productChannelDetailService.selectByProductId(productId);
            for (ProductChannelDetail detail : productChannelDetails){

                if (detail.getChannelTypeSign() == EnumPayChannelSign.YG_WEIXIN.getId()){
                    wxRate = detail.getProductMerchantPayRate();
                }else if (detail.getChannelTypeSign() == EnumPayChannelSign.YG_ZHIFUBAO.getId()){
                    zfbRate = detail.getProductMerchantPayRate();
                }else {
                    ylRate = detail.getProductMerchantPayRate();
                }
            }
        }else {
            final Dealer dealer = this.dealerService.getById(dealerId).get();
            if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()) {
                //一级代理直属商户
                final List<DealerChannelRate> dealerChannelRates = this.dealerChannelRateDao.selectByDealerIdAndProductId(dealerId, productId);
                for (DealerChannelRate detail : dealerChannelRates) {

                    if (detail.getChannelTypeSign() == EnumPayChannelSign.YG_WEIXIN.getId()) {
                        wxRate = detail.getDealerMerchantPayRate();
                    } else if (detail.getChannelTypeSign() == EnumPayChannelSign.YG_ZHIFUBAO.getId()) {
                        zfbRate = detail.getDealerMerchantPayRate();
                    } else {
                        ylRate = detail.getDealerMerchantPayRate();
                    }
                }
            } else {
                //二级代理直属商户
                final List<DealerChannelRate> dealerChannelRates = this.dealerChannelRateDao.selectByDealerIdAndProductId(dealer.getFirstLevelDealerId(), productId);
                for (DealerChannelRate detail : dealerChannelRates) {

                    if (detail.getChannelTypeSign() == EnumPayChannelSign.YG_WEIXIN.getId()) {
                        wxRate = detail.getDealerMerchantPayRate();
                    } else if (detail.getChannelTypeSign() == EnumPayChannelSign.YG_ZHIFUBAO.getId()) {
                        zfbRate = detail.getDealerMerchantPayRate();
                    } else {
                        ylRate = detail.getDealerMerchantPayRate();
                    }

                }
            }
        }
        return Triple.of(wxRate, zfbRate, ylRate);
    }

    /**
     * 查询代理商绑定的产品编码
     *
     * @param dealerId
     * @param sysType
     * @return
     */
    @Override
    public long getDealerBindProductId(long dealerId, String sysType) {
        return dealerChannelRateDao.getDealerBindProductId(dealerId,sysType);
    }
}
