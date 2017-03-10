package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.AccountBankDao;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumAccountBank;
import com.jkm.hss.merchant.enums.EnumBankDefault;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;
import com.jkm.hss.merchant.helper.response.BankListResponse;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<BankListResponse> selectAll(long accountId) {
        List<AccountBank> accountBankList = new ArrayList<AccountBank>();
        accountBankList = this.selectCreditCardList(accountId);
        AccountBank accountBank = this.getDefault(accountId);
        if(accountBank!=null){
            accountBankList.add(accountBank);
        }
        List<BankListResponse> bankListResponseList = new ArrayList<BankListResponse>();
        if(accountBankList.size()>0){
            for(int i=0;i<accountBankList.size();i++){
                AccountBank accountBank1 = accountBankList.get(i);
                BankListResponse bankListResponse = new BankListResponse();
                bankListResponse.setBankId(accountBank1.getId());
                if(accountBank1.getBankNo()!=null&&!"".equals(accountBank1.getBankNo())){
                    String bankNo = MerchantSupport.decryptBankCard(accountBank1.getBankNo());
                    bankListResponse.setBankNo(bankNo.substring(bankNo.length()-4,bankNo.length()));
                }
                bankListResponse.setBankName(accountBank1.getBankName());
                if(accountBank1.getReserveMobile()!=null&&!"".equals(accountBank1.getReserveMobile())){
                    String mobile = MerchantSupport.decryptBankCard(accountBank1.getReserveMobile());
                    bankListResponse.setReserveMobile(mobile.substring(0,3)+"******"+mobile.substring(mobile.length()-2,mobile.length()));
                }
                bankListResponse.setBankBin(accountBank1.getBankBin());
                bankListResponse.setBranchName(accountBank1.getBranchName());
                bankListResponse.setCardType(accountBank1.getCardType());
                if(accountBank1.getBranchCode()!=null&&!"".equals(accountBank1.getBranchCode())){
                    bankListResponse.setHasBranch(1);
                }else{
                    bankListResponse.setHasBranch(0);
                }
                bankListResponseList.add(bankListResponse);
            }
        }
        return bankListResponseList;
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

    /**
     * 删除信用卡
     *
     * @param id
     * @return
     */
    @Override
    public int deleteCreditCard(long id) {
        return accountBankDao.deleteCreditCard(id);
    }

    /**
     * 更改默认银行卡
     *
     * @param accountId
     * @param bankNo
     * @param reserveMobile
     * @return
     */
    @Override
    public int changeBankCard(long accountId, String bankNo, String reserveMobile) {
        this.reset(accountId);
        AccountBank accountBank = new AccountBank();
        accountBank.setAccountId(accountId);
//        accountBank.setBankNo(merchantInfo.getBankNo());
//        accountBank.setBankName(merchantInfo.getBankName());
//        accountBank.setReserveMobile(merchantInfo.getReserveMobile());
//        accountBank.setBranchCode(merchantInfo.getBranchCode());
//        accountBank.setBranchName(merchantInfo.getBranchName());
//        accountBank.setBranchProvinceCode(merchantInfo.getProvinceCode());
//        accountBank.setBranchProvinceName(merchantInfo.getProvinceName());
//        accountBank.setBranchCityCode(merchantInfo.getCityCode());
//        accountBank.setBranchCityName(merchantInfo.getCityName());
//        accountBank.setBranchCountyCode(merchantInfo.getCountyCode());
//        accountBank.setBranchCountyName(merchantInfo.getCountyName());
//        accountBank.setCardType(EnumAccountBank.DEBITCARD.getId());
//        accountBank.setIsAuthen(merchantInfo.getIsAuthen());
//        accountBank.setIsDefault(EnumBankDefault.DEFAULT.getId());
//        accountBank.setBankBin(merchantInfo.getBankBin());
        return this.insert(accountBank);
    }
}
