package com.jkm.hss.controller.migration;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.AccountDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.AccountInfo;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.AccountInfoService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/4.
 */
@Slf4j
@Controller
@RequestMapping(value = "/migration")
public class MigrationController extends BaseController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AccountDao accountDao;

    @RequestMapping(value = "account")
    public void accountMigration() {
        final List<Long> merchantAccountIds = new ArrayList<>();
        final List<Long> dealerAccountIds = new ArrayList<>();
        for (long i = 0; i < 120; i++) {
            final AccountInfo accountInfo = this.accountInfoService.selectByPrimaryKey(i);
            if (null != accountInfo) {
                final Optional<Dealer> dealerOptional = this.dealerService.getByAccountId(accountInfo.getId());
                if (dealerOptional.isPresent()) {
                    final Account account1 = new Account();
                    account1.setId(accountInfo.getId());
                    account1.setUserName(dealerOptional.get().getProxyName());
                    account1.setTotalAmount(accountInfo.getAmount());
                    account1.setAvailable(accountInfo.getAmount());
                    account1.setFrozenAmount(new BigDecimal("0.00"));
                    account1.setDueSettleAmount(new BigDecimal("0.00"));
                    this.accountDao.initPoundageAccount(account1);
                    merchantAccountIds.add(account1.getId());
                }
                final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.getByAccountId(accountInfo.getId());
                if (merchantInfoOptional.isPresent()) {
                    final Account account1 = new Account();
                    account1.setId(accountInfo.getId());
                    account1.setUserName(merchantInfoOptional.get().getMerchantName());
                    account1.setTotalAmount(new BigDecimal("0.00"));
                    account1.setAvailable(new BigDecimal("0.00"));
                    account1.setFrozenAmount(new BigDecimal("0.00"));
                    account1.setDueSettleAmount(new BigDecimal("0.00"));
                    this.accountDao.initPoundageAccount(account1);
                    dealerAccountIds.add(account1.getId());
                }
                final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(101).get();
                if (basicChannel.getAccountId() == accountInfo.getId()) {
                    final Account account1 = new Account();
                    account1.setId(accountInfo.getId());
                    account1.setUserName("通道账户");
                    account1.setTotalAmount(accountInfo.getAmount());
                    account1.setAvailable(accountInfo.getAmount());
                    account1.setFrozenAmount(new BigDecimal("0.00"));
                    account1.setDueSettleAmount(new BigDecimal("0.00"));
                    this.accountDao.initPoundageAccount(account1);
                }

                final Product product = this.productService.selectAll().get(0);
                if (product.getAccountId() == accountInfo.getId()) {
                    final Account account1 = new Account();
                    account1.setId(accountInfo.getId());
                    account1.setUserName("产品账户");
                    account1.setTotalAmount(accountInfo.getAmount());
                    account1.setAvailable(accountInfo.getAmount());
                    account1.setFrozenAmount(new BigDecimal("0.00"));
                    account1.setDueSettleAmount(new BigDecimal("0.00"));
                    this.accountDao.initPoundageAccount(account1);
                }
            }
        }
        log.info("merchant [{}]", merchantAccountIds);
        log.info("dealer [{}]", dealerAccountIds);
    }
}
