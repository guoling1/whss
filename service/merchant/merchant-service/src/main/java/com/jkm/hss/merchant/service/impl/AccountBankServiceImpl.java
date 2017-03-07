package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.AccountBankDao;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumAccountBank;
import com.jkm.hss.merchant.enums.EnumBankDefault;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/6.
 */
@Slf4j
@Service
public class AccountBankServiceImpl implements AccountBankService{
    @Autowired
    private AccountBankDao accountBankDao;
    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 是否有银行卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public int isHasAccountBank(long accountId) {
        return accountBankDao.isHasAccountBank(accountId);
    }

    /**
     * 初始化银行卡账户
     *
     * @return
     */
    @Override
    public int initAccountBank(long merchantId,long accountId) {
        int count = this.isHasAccountBank(accountId);
        if(count>0){
            log.info("已有账户无需初始化，账户编码{},商户编码{}",accountId,merchantId);
            return 0;
        }
        Optional<MerchantInfo> merchantInfoOptional =  merchantInfoService.selectById(merchantId);
        if(!merchantInfoOptional.isPresent()){
            log.info("初始化银行账户时找不到商户{}",merchantId);
        }
        MerchantInfo merchantInfo = merchantInfoOptional.get();
        AccountBank accountBank = new AccountBank();
        accountBank.setAccountId(accountId);
        accountBank.setBankNo(merchantInfo.getBankNo());
        accountBank.setBankName(merchantInfo.getBankName());
        accountBank.setReserveMobile(merchantInfo.getReserveMobile());
        accountBank.setBranchCode(merchantInfo.getBranchCode());
        accountBank.setBranchName(merchantInfo.getBranchName());
        accountBank.setBranchProvinceCode(merchantInfo.getProvinceCode());
        accountBank.setBranchProvinceName(merchantInfo.getProvinceName());
        accountBank.setBranchCityCode(merchantInfo.getCityCode());
        accountBank.setBranchCityName(merchantInfo.getCityName());
        accountBank.setBranchCountyCode(merchantInfo.getCountyCode());
        accountBank.setBranchCountyName(merchantInfo.getCountyName());
        accountBank.setCardType(EnumAccountBank.DEBITCARD.getId());
        accountBank.setIsAuthen(merchantInfo.getIsAuthen());
        accountBank.setIsDefault(EnumBankDefault.DEFAULT.getId());
        accountBank.setBankBin(merchantInfo.getBankBin());
        return this.insert(accountBank);
    }

    /**
     * 新增
     *
     * @param accountBank
     * @return
     */
    @Override
    public int insert(AccountBank accountBank) {
        return accountBankDao.insert(accountBank);
    }

    /**
     * 修改
     *
     * @param accountBank
     * @return
     */
    @Override
    public int update(AccountBank accountBank) {
        return accountBankDao.update(accountBank);
    }

    /**
     * 设置为默认银行卡
     *
     * @param id
     * @return
     */
    @Override
    public int setDefault(long id) {
        return accountBankDao.setDefault(id);
    }

    /**
     * 全部设置为不是默认银行卡
     *
     * @param accountId
     * @return
     */
    @Override
    public int reset(long accountId) {
        return accountBankDao.reset(accountId);
    }

    /**
     * 获取默认银行卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public AccountBank getDefault(long accountId) {
        return accountBankDao.getDefault(accountId);
    }

    /**
     * 获取最新信用卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public AccountBank getCreditCard(long accountId) {
        return accountBankDao.getCreditCard(accountId);
    }


    /**
     * 查询信用卡列表
     *
     * @param accountId
     * @return
     */
    @Override
    public List<AccountBank> selectCreditCardList(long accountId) {
        return accountBankDao.selectCreditCardList(accountId);
    }


    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AccountBank> selectById(long id) {
        return Optional.fromNullable(accountBankDao.selectById(id));
    }

    /**
     * 我的银行卡列表
     *
     * @param accountId
     * @return
     */
    @Override
    public List<AccountBank> selectAll(long accountId) {
        List<AccountBank> accountBankList = this.selectCreditCardList(accountId);
        AccountBank accountBank = this.getDefault(accountId);
        if(accountBank!=null){
            accountBankList.add(accountBank);
        }
        return accountBankList;
    }

    /**
     * 修改支行信息
     *
     * @param continueBankInfoRequest
     * @return
     */
    @Override
    public int updateBranchInfo(ContinueBankInfoRequest continueBankInfoRequest) {
        return accountBankDao.updateBranchInfo(continueBankInfoRequest);
    }
}
