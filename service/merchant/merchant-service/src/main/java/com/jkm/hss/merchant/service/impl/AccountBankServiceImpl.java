package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse2;
import com.jkm.hss.admin.service.AppBizDistrictService;
import com.jkm.hss.merchant.dao.AccountBankDao;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumAccountBank;
import com.jkm.hss.merchant.enums.EnumBankDefault;
import com.jkm.hss.merchant.enums.EnumCleanType;
import com.jkm.hss.merchant.enums.EnumCommonStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.ContinueBankInfoRequest;
import com.jkm.hss.merchant.helper.response.BankListResponse;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.VerifyIdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
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
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private VerifyIdService verifyIdService;
    @Autowired
    private AppBizDistrictService appBizDistrictService;

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
     * 是否有信用卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public int isHasCreditBank(long accountId) {
        return accountBankDao.isHasCreditBank(accountId);
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
        AppBizDistrictResponse2 adrCounty = appBizDistrictService.getByCode(merchantInfo.getDistrictCode());
        if (null != adrCounty) {
            accountBank.setBranchCountyCode(adrCounty.getCode());
            accountBank.setBranchCountyName(adrCounty.getAname());
        }
        AppBizDistrictResponse2 adrCity = appBizDistrictService.getByCode(adrCounty.getParentCode());
        if (null != adrCity) {
            accountBank.setBranchCityCode(adrCity.getCode());
            accountBank.setBranchCityName(adrCity.getAname());
            if("110000,120000,310000,500000".contains(adrCity.getCode())){
                accountBank.setBranchProvinceCode(adrCity.getCode());
                accountBank.setBranchProvinceName(adrCity.getAname());
            }else{
                AppBizDistrictResponse2 adrProvince = appBizDistrictService.getByCode(adrCity.getParentCode());
                accountBank.setBranchProvinceCode(adrProvince.getCode());
                accountBank.setBranchProvinceName(adrProvince.getAname());
            }
        }
        accountBank.setCardType(EnumAccountBank.DEBITCARD.getId());
        accountBank.setIsAuthen(merchantInfo.getIsAuthen());
        accountBank.setIsDefault(EnumBankDefault.DEFAULT.getId());
        accountBank.setBankBin(merchantInfo.getBankBin());
        return this.insert(accountBank);
    }

    /**
     * 初始化信用卡账户
     *
     * @return
     */
    @Override
    public long initCreditBankCard(long accountId,String bankNo,String bankName,String reserveMobile,String bankBin,String expiryTime,String cvv) {
        Optional<AccountBank> backAccountBank = this.selectCreditCardByBankNoAndStateless(accountId,bankNo);
        if(!backAccountBank.isPresent()){
            AccountBank accountBank = new AccountBank();
            accountBank.setAccountId(accountId);
            accountBank.setBankNo(MerchantSupport.encryptBankCard(bankNo));
            accountBank.setBankName(bankName);
            accountBank.setReserveMobile(MerchantSupport.encryptMobile(reserveMobile));
            accountBank.setCardType(EnumAccountBank.CREDIT.getId());
            accountBank.setIsDefault(EnumBankDefault.UNDEFALUT.getId());
            accountBank.setBankBin(bankBin);
            accountBank.setExpiryTime(expiryTime);
            if(cvv!=null&&!"".equals(cvv)){
                accountBank.setCvv(MerchantSupport.encryptCvv(cvv));
            }
            accountBank.setStatus(EnumCommonStatus.DISABLE.getId());
            this.insert(accountBank);
            return accountBank.getId();
        }else{
            AccountBank accountBank = backAccountBank.get();
            accountBank.setReserveMobile(MerchantSupport.encryptMobile(reserveMobile));
            accountBank.setBankName(bankName);
            accountBank.setBankBin(bankBin);
            accountBank.setExpiryTime(expiryTime);
            if(cvv!=null&&!"".equals(cvv)){
                accountBank.setCvv(MerchantSupport.encryptCvv(cvv));
            }
            accountBankDao.updateBankInfo(accountBank);
            return backAccountBank.get().getId();
        }

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
     * 更新token
     *
     * @param id
     * @param token
     * @return
     */
    @Override
    public int updateToken(final long id, final String token) {
        return accountBankDao.updateToken(id, token);
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
     * 设置为默认信用卡
     *
     * @param id
     * @return
     */
    @Override
    public int setDefaultCreditCard(long id) {
        AccountBank accountBank = accountBankDao.selectStatelessById(id);
        if(accountBank!=null){
            this.reset(accountBank.getAccountId(),EnumAccountBank.CREDIT.getId());
        }
        return accountBankDao.setDefaultCreditCard(id);
    }
    /**
     * 设置为默认信用卡
     *
     * @param id
     * @return
     */
    @Override
    public int setDefaultCreditCardById(long id) {
        return accountBankDao.setDefaultCreditCardById(id);
    }
    /**
     * 全部设置为不是默认银行卡
     *
     * @param accountId
     * @return
     */
    @Override
    public int reset(long accountId,int cardType) {
        return accountBankDao.reset(accountId,cardType);
    }

    /**
     * 获取默认银行卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public AccountBank getDefault(long accountId) {
        AccountBank accountBank = accountBankDao.getDefault(accountId);
        if(accountBank!=null){
            accountBank.setBankNo(MerchantSupport.decryptBankCard(accountId,accountBank.getBankNo()));
            accountBank.setReserveMobile(MerchantSupport.decryptMobile(accountId,accountBank.getReserveMobile()));
        }
        return accountBank;
    }

    /**
     * 获取默认信用卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public AccountBank getDefaultCreditCard(long accountId) {
        AccountBank accountBank = accountBankDao.getDefaultCreditCard(accountId);
        if(accountBank!=null){
            accountBank.setBankNo(MerchantSupport.decryptBankCard(accountId,accountBank.getBankNo()));
            accountBank.setReserveMobile(MerchantSupport.decryptMobile(accountId,accountBank.getReserveMobile()));
        }
        return accountBank;
    }

    /**
     * 查询解密过的信用卡列表
     *
     * @param accountId
     * @return
     */
    @Override
    public List<AccountBank> selectCreditList(long accountId) {
        List<AccountBank> accountBankList = this.selectCreditCardList(accountId);
        if(accountBankList.size()>0){
            for(int i=0;i<accountBankList.size();i++){
                accountBankList.get(i).setBankNo(MerchantSupport.decryptBankCard(accountId,accountBankList.get(i).getBankNo()));
                accountBankList.get(i).setReserveMobile(MerchantSupport.decryptMobile(accountId,accountBankList.get(i).getReserveMobile()));
            }
        }
        return accountBankList;
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
        AccountBank accountBank = accountBankDao.getDefault(accountId);
        if(accountBank!=null){
            accountBankList.add(accountBank);
        }
        List<AccountBank> creditCardList = this.selectCreditCardList(accountId);
        if(creditCardList.size()>0){
            accountBankList.addAll(creditCardList);
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
                String tempBranchName = accountBank1.getBranchName();
                if(!"".equals(tempBranchName)&&tempBranchName!=null&&tempBranchName.length()>12){
                    tempBranchName = "***"+tempBranchName.substring(tempBranchName.length()-12,tempBranchName.length());
                }
                bankListResponse.setBranchName(tempBranchName);
                bankListResponse.setCardType(accountBank1.getCardType());
                if((accountBank1.getBranchCode()!=null&&!"".equals(accountBank1.getBranchCode()))||(accountBank1.getBranchName()!=null&&!"".equals(accountBank1.getBranchName()))){
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
     * @param merchantInfo
     * @param bankNo
     * @param reserveMobile
     * @return
     */
    @Override
    public int changeBankCard(MerchantInfo merchantInfo, String bankNo, String reserveMobile,
                              String branchCityCode, String branchCityName,
                              String branchCountyCode, String branchCountyName,
                              String branchProvinceCode, String branchProvinceName, String branchName) {

        this.reset(merchantInfo.getAccountId(),EnumAccountBank.DEBITCARD.getId());
        AccountBank accountBank = new AccountBank();
        //校验身份4要素
        final String mobile = MerchantSupport.decryptMobile(merchantInfo.getMobile());
        final String idCard = MerchantSupport.decryptIdentity(merchantInfo.getIdentity());
        final String realName = merchantInfo.getName();
        final Pair<Integer, String> pair = this.verifyIdService.verifyID(mobile, bankNo, idCard, reserveMobile, realName);
        if (0 == pair.getLeft()) {
            accountBank.setIsAuthen("1");
        }
        log.info("AccountId={}",merchantInfo.getAccountId());
        accountBank.setAccountId(merchantInfo.getAccountId());
        accountBank.setBankNo(MerchantSupport.encryptBankCard(bankNo));
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(bankNo);
        accountBank.setBankName(bankCardBinOptional.get().getBankName());
        accountBank.setReserveMobile(MerchantSupport.encryptMobile(reserveMobile));
        accountBank.setCardType(EnumAccountBank.DEBITCARD.getId());
        accountBank.setIsDefault(EnumBankDefault.DEFAULT.getId());
        accountBank.setBankBin(bankCardBinOptional.get().getShorthand());
        accountBank.setBranchCityCode(branchCityCode);
        accountBank.setBranchCityName(branchCityName);
        accountBank.setBranchCountyCode(branchCountyCode);
        accountBank.setBranchCountyName(branchCountyName);
        accountBank.setBranchProvinceCode(branchProvinceCode);
        accountBank.setBranchProvinceName(branchProvinceName);
        accountBank.setBranchName(branchName);
        return this.insert(accountBank);
    }
    /**
     * 更改默认银行卡（已存在银行卡）
     * @param bankId
     * @param merchantInfo
     * @param bankNo
     * @param reserveMobile
     * @return
     */
    @Override
    public int updateDefaultBankCard(long bankId,MerchantInfo merchantInfo, String bankNo, String reserveMobile) {
        this.reset(merchantInfo.getAccountId(),EnumAccountBank.DEBITCARD.getId());
        return setDefault(bankId);
    }


    /**
     * 是否有银行卡
     *
     * @param accountId
     * @param bankNo
     * @param cardType
     * @return
     */
    @Override
    public Optional<AccountBank> isExistBankNo(long accountId, String bankNo, int cardType) {
        return Optional.fromNullable(accountBankDao.selectByBankNo(accountId,bankNo,cardType));
    }

    /**
     * 无状态查询信用卡
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AccountBank> selectStatelessById(long id) {
        return Optional.fromNullable(accountBankDao.selectStatelessById(id));
    }

    /**
     * 是否有信用卡
     *
     * @param accountId
     * @param bankNo
     * @return
     */
    @Override
    public Optional<AccountBank> selectCreditCardByBankNo(long accountId, String bankNo) {
        Optional<AccountBank> accountBankOptional = this.isExistBankNo(accountId,MerchantSupport.encryptBankCard(bankNo),EnumAccountBank.CREDIT.getId());
        return accountBankOptional;
    }

    /**
     * 无状态查询信用卡
     *
     * @param accountId
     * @param bankNo
     * @return
     */
    @Override
    public Optional<AccountBank> selectCreditCardByBankNoAndStateless(long accountId, String bankNo) {
        return Optional.fromNullable(accountBankDao.selectByBankNoAndStateless(accountId,MerchantSupport.encryptBankCard(bankNo),EnumAccountBank.CREDIT.getId()));
    }

    /**
     * 获取最新信用卡
     *
     * @param accountId
     * @return
     */
    @Override
    public Optional<AccountBank> getTopCreditCard(long accountId) {
        return Optional.fromNullable(accountBankDao.getTopCreditCard(accountId));
    }

    /**
     * 根据id更改cvv
     *
     * @param cvv
     * @param id
     */
    @Override
    public void updateCvvById(String cvv, long id) {
        AccountBank accountBank = new AccountBank();
        accountBank.setId(id);
        accountBank.setCvv(MerchantSupport.encryptCvv(cvv));
        accountBank.setUpdateType(EnumCleanType.CVV.getId());
        accountBankDao.updateBankInfo(accountBank);
    }

    /**
     * 根据id更改有效期
     *
     * @param expiryTime
     * @param id
     */
    @Override
    public void updateExpiryTimeById(String expiryTime, long id) {
        AccountBank accountBank = new AccountBank();
        accountBank.setId(id);
        accountBank.setExpiryTime(expiryTime);
        accountBank.setUpdateType(EnumCleanType.EXPIRYTIME.getId());
        accountBankDao.updateBankInfo(accountBank);
    }
    /**
     * 根据id更改cvv和有效期
     *
     * @param cvv
     * @param expiryTime
     * @param id
     */
    @Override
    public void updateCvvAndExpiryTimeById(String cvv, String expiryTime, long id) {
        AccountBank accountBank = new AccountBank();
        accountBank.setId(id);
        accountBank.setCvv(MerchantSupport.encryptCvv(cvv));
        accountBank.setExpiryTime(expiryTime);
        accountBank.setUpdateType(EnumCleanType.CVVANDEXPIRYTIME.getId());
        accountBankDao.updateBankInfo(accountBank);
    }

    /**
     * 是否有cvv
     *
     * @param id
     * @return
     */
    @Override
    public boolean isHasCvv(long id) {
        AccountBank accountBank = accountBankDao.selectById(id);
        if(accountBank!=null&&accountBank.getCvv()!=null&&!"".equals(accountBank.getCvv())){
            return true;
        }
        return false;
    }

    /**
     * 是否有有效期
     *
     * @param id
     * @return
     */
    @Override
    public boolean isHasExpiryTime(long id) {
        AccountBank accountBank = accountBankDao.selectById(id);
        if(accountBank!=null&&accountBank.getExpiryTime()!=null&&!"".equals(accountBank.getExpiryTime())){
            return true;
        }
        return false;
    }

    /**
     * 清理cvv或有有效期
     *
     * @param type
     */
    @Override
    public void cleanCvvAndExpiryTime(long id,int type) {
        if(EnumCleanType.CVV.getId()==type){
            accountBankDao.cleanCvv(id);
        }
        if(EnumCleanType.EXPIRYTIME.getId()==type){
            accountBankDao.cleanExpiryTime(id);
        }
        if(EnumCleanType.CVVANDEXPIRYTIME.getId()==type){
            accountBankDao.cleanCvvAndExpiryTime(id);
        }
    }


    /**
     * 初始化信用卡账户
     *
     * @return
     */
    @Override
    public void bindCard(long accountId,String bankNo,
                                   String bankName,String reserveMobile,
                                   String bankBin,String token) {
        Optional<AccountBank> accountBankOptional = this.selectCreditCardByBankNo(accountId,bankNo);
        final AccountBank accountBank = new AccountBank();
        if(!accountBankOptional.isPresent()){
            accountBank.setAccountId(accountId);
            accountBank.setToken(token);
            accountBank.setBankNo(MerchantSupport.encryptBankCard(bankNo));
            accountBank.setBankName(bankName);
            accountBank.setReserveMobile(reserveMobile);
            accountBank.setCardType(EnumAccountBank.CREDIT.getId());
            AccountBank ab= this.getDefaultCreditCard(accountId);
            if(ab==null){
                accountBank.setIsDefault(EnumBankDefault.DEFAULT.getId());
            }else{
                accountBank.setIsDefault(EnumBankDefault.UNDEFALUT.getId());
            }
            accountBank.setBankBin(bankBin);
            accountBank.setStatus(EnumCommonStatus.NORMAL.getId());
            this.insert(accountBank);
        } else {
            log.info("账户id-[{}]，银行卡号-[{}]，记录已存在，保存token-[{}]", accountId, bankNo, token);
            this.updateToken(accountBankOptional.get().getId(), token);
        }
    }

    /**
     * 根据bankNo查询
     *
     * @param accountId
     * @param bankNo
     * @return
     */
    @Override
    public AccountBank selectCreditListByBankNo(long accountId, String bankNo) {
        AccountBank accountBank = accountBankDao.selectCreditListByBankNo(accountId,MerchantSupport.encryptBankCard(bankNo));
        return accountBank;
    }

    @Override
    public int isHasCreditBankToken(long accountId) {
        return accountBankDao.isHasCreditBankToken(accountId);
    }

    @Override
    public List<AccountBank> selectCreditList2Token(long accountId) {
        List<AccountBank> accountBankList = this.selectCreditCardList2Token(accountId);
        if(accountBankList.size()>0){
            for(int i=0;i<accountBankList.size();i++){
                accountBankList.get(i).setBankNo(MerchantSupport.decryptBankCard(accountId,accountBankList.get(i).getBankNo()));
                accountBankList.get(i).setReserveMobile(MerchantSupport.decryptMobile(accountId,accountBankList.get(i).getReserveMobile()));
            }
        }
        return accountBankList;
    }

    private List<AccountBank> selectCreditCardList2Token(long accountId) {
        return accountBankDao.selectCreditCardList2Token(accountId);
    }
}
