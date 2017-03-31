package com.jkm.hss.admin.service.impl;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumGlobalAdminUserLevel;
import com.jkm.base.common.util.GlobalID;
import com.jkm.hss.admin.dao.AdminRoleDao;
import com.jkm.hss.admin.dao.AdminUserDao;
import com.jkm.hss.admin.entity.*;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.enums.EnumDataDictionaryType;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType2;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.admin.helper.requestparam.AdminUserListRequest;
import com.jkm.hss.admin.helper.responseparam.AdminUserListResponse;
import com.jkm.hss.admin.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by huangwei on 5/27/16.
 */
@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private AdminUserPassportService adminUserPassportService;

    @Autowired
    private DistributeQRCodeRecordService distributeQRCodeRecordService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private AdminRoleDao adminRoleDao;

    /**
     * {@inheritDoc}
     *
     * @param adminUser
     */
    @Override
    public long createUser(final AdminUser adminUser) {
        final String salt = AdminUserSupporter.generateSalt();
        final String password = AdminUserSupporter.encryptPassword(salt, adminUser.getPassword());
        adminUser.setSalt(salt);
        adminUser.setPassword(password);
        adminUser.setStatus(EnumAdminUserStatus.NORMAL.getCode());
        adminUser.setMobile(AdminUserSupporter.encryptMobile(adminUser.getMobile()));
        adminUser.setIdCard(AdminUserSupporter.encryptIdenrity(adminUser.getIdCard()));
        adminUser.setType(EnumAdminType.BOSS.getCode());
        this.adminUserDao.insert(adminUser);
        this.adminUserDao.updateMarkCode(GlobalID.GetAdminUserID(EnumGlobalAdminUserLevel.BOSS,adminUser.getId()+""),adminUser.getId());
        return adminUser.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param adminUser
     * @return
     */
    @Override
    public int update(final AdminUser adminUser) {
        adminUser.setMobile(AdminUserSupporter.encryptMobile(adminUser.getMobile()));
        adminUser.setIdCard(AdminUserSupporter.encryptIdenrity(adminUser.getIdCard()));
        adminUser.setStatus(EnumAdminUserStatus.NORMAL.getCode());
        return this.adminUserDao.update(adminUser);
    }

    /**
     * {@inheritDoc}
     *
     * @param auid
     * @return
     */
    @Override
    public Optional<AdminUser> getAdminUserById(final long auid) {
        return Optional.fromNullable(adminUserDao.selectById(auid));
    }

    /**
     * {@inheritDoc}
     *
     * @param username
     * @return
     */
    @Override
    public Optional<AdminUser> getAdminUserByNameAndType(final String username,final int type) {
        if(type==EnumAdminType.BOSS.getCode()){
            return Optional.fromNullable(adminUserDao.selectByUsernameAndType(username,type));
        }else{
            return Optional.fromNullable(adminUserDao.selectDealerLoginNameByUsername(username));
        }
    }

    /**
     * 根据用户名和类型获取
     *
     * @param username
     * @param type
     * @param dealerId @return
     */
    @Override
    public Optional<AdminUser> getAdminUserByNameAndTypeUnIncludeNow(String username, int type, long dealerId) {
        if(type==EnumAdminType.BOSS.getCode()){
            return Optional.fromNullable(adminUserDao.getAdminUserByNameAndTypeUnIncludeNow(username,type,dealerId));
        }else{
            return Optional.fromNullable(adminUserDao.getAdminDealerUserByNameUnIncludeNow(username,dealerId));
        }

    }

    @Override
    public void disableUser(long auid) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(auid);
        adminUser.setStatus(EnumAdminUserStatus.DISABLE.getCode());
        adminUserDao.enableOrDisable(adminUser);
    }

    @Override
    public void activeUser(long auid) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(auid);
        adminUser.setStatus(EnumAdminUserStatus.NORMAL.getCode());
        adminUserDao.enableOrDisable(adminUser);
    }

    /**
     * {@inheritDoc}
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Optional<AdminUserPassport> login(final String username, final String password) {
        final Optional<AdminUser> adminUserOptional = getAdminUserByNameAndType(username,EnumAdminType.BOSS.getCode());
        if (adminUserOptional.isPresent()) {
            final AdminUser adminUser = adminUserOptional.get();
            if (AdminUserSupporter.isPasswordCorrect(adminUser, password)) {
                final AdminUserPassport adminUserToken = adminUserPassportService.generateToken(adminUser.getId());
                return Optional.of(adminUserToken);
            }
        }

        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     *
     * @param auid
     */
    @Override
    public void logout(final long auid) {
        adminUserPassportService.invalidatePassport(auid);
    }

    /**
     * {@inheritDoc}
     *
     * @param adminId
     * @param firstLevelDealerId
     * @param count
     * @return
     */
    @Override
    @Transactional
    synchronized public Triple<Integer, String, List<Pair<QRCode, QRCode>>> distributeQRCode(final long adminId,
                                                                                             final long firstLevelDealerId, final int count,final String sysType) {
        final List<Long> unDistributeList = this.qrCodeService.getUnDistributeCode(sysType);
        log.info("未分配的二维码数量[{}]", unDistributeList.size());
        if (unDistributeList.size() < count) {
            return Triple.of(0, "未分配的二维码数量[" + unDistributeList.size() + "]小于要分配的个数[" + count+ "]", null);
        }
        final List<Long> ids = unDistributeList.subList(0, count);
        final int updateCount = this.qrCodeService.markCodeToDealer(firstLevelDealerId, ids);
        log.info("分配二维码数量[{}], 实际要分配数量[{}]", updateCount, count);
        Preconditions.checkState(updateCount == count, "mark code to dealer error!!!");
        final List<QRCode> codes = this.qrCodeService.getByIds(ids);
        final List<Pair<QRCode, QRCode>> pairs = this.qrCodeService.getPairQRCodeList(codes);
        return Triple.of(1, "分配成功", pairs);
    }

    /**
     * {@inheritDoc}
     *
     *
     * @param firstLevelDealerId
     * @param startCode
     * @param endCode
     */
    @Override
    @Transactional
    synchronized public List<Pair<QRCode, QRCode>> distributeRangeQRCode(final long firstLevelDealerId,
                                                                         final String startCode, final String endCode, final String sysType) {
        final List<Long> codeIds = this.qrCodeService.getUnDistributeCodeByRangeCode(startCode, endCode, sysType);
        if (CollectionUtils.isEmpty(codeIds)) {
            return Collections.emptyList();
        }
        final int updateCount = this.qrCodeService.markCodeToDealer(firstLevelDealerId, codeIds);
        Preconditions.checkState(updateCount == codeIds.size(), "mark code to dealer error!!!");
        final List<QRCode> codes = this.qrCodeService.getByIds(codeIds);
        final List<Pair<QRCode, QRCode>> pairs = this.qrCodeService.getPairQRCodeList(codes);
        return pairs;
    }

    @Override
    public CodeQueryResponse getCode(final String code) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getCode(code);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getProxyName(long firstLevelDealerId) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getProxyName(firstLevelDealerId);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getProxyName1(long secondLevelDealerId) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getProxyName1(secondLevelDealerId);
        return codeQueryResponse;
    }

    @Override
    public CodeQueryResponse getMerchantName(long merchantId) {
        final CodeQueryResponse codeQueryResponse = this.qrCodeService.getMerchantName(merchantId);
        return codeQueryResponse;
    }


    /**
     * {@inheritDoc}
     *
     * @param dealerId  一级代理商id
     * @param startCode  开始二维码
     * @param endCode  结束二维码
     * @return
     */
    @Override
    @Transactional
    public List<DistributeQRCodeRecord> distributeQRCodeByCode(final int type, final String sysType, final long dealerId,
                                                               final String startCode, final String endCode) {
        final List<DistributeQRCodeRecord> records = new ArrayList<>();
        final List<QRCode> qrCodeList = this.qrCodeService.getUnDistributeCodeByCodeAndSysType(startCode, endCode,sysType);
        if (CollectionUtils.isEmpty(qrCodeList)) {
            return records;
        }
        final List<Long> qrCodeIds = Lists.transform(qrCodeList, new Function<QRCode, Long>() {
            @Override
            public Long apply(QRCode input) {
                return input.getId();
            }
        });
        this.qrCodeService.markCodeToDealer(dealerId, qrCodeIds);
        final List<Pair<QRCode, QRCode>> pairQRCodeList = this.qrCodeService.getPairQRCodeList(qrCodeList);
        for (Pair<QRCode, QRCode> pair : pairQRCodeList) {
            final QRCode left = pair.getLeft();
            final QRCode right = pair.getRight();
            final DistributeQRCodeRecord distributeQRCodeRecord = new DistributeQRCodeRecord();
            distributeQRCodeRecord.setFirstLevelDealerId(0);
            distributeQRCodeRecord.setSecondLevelDealerId(dealerId);
            distributeQRCodeRecord.setCount((int) (Long.valueOf(right.getCode()) - Long.valueOf(left.getCode()) + 1));
            distributeQRCodeRecord.setStartCode(left.getCode());
            distributeQRCodeRecord.setEndCode(right.getCode());
            distributeQRCodeRecord.setCreateTime(new Date());
            distributeQRCodeRecord.setDistributeType(EnumQRCodeDistributeType2.ADMIN.getCode());
            distributeQRCodeRecord.setType(type);
            records.add(distributeQRCodeRecord);
            this.distributeQRCodeRecordService.add(distributeQRCodeRecord);
        }
        return records;
    }

    /**
     * 按个数分配
     *
     * @param type
     * @param dealerId
     * @param count
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> distributeQRCodeByCount(int type, String sysType, long dealerId, int count) {
        final List<DistributeQRCodeRecord> records = new ArrayList<>();
        final List<QRCode> qrCodeList = this.qrCodeService.getUnDistributeCodeBySysType(sysType);
        if (CollectionUtils.isEmpty(qrCodeList)) {
            return records;
        }
        final List<Long> qrCodeIds = Lists.transform(qrCodeList, new Function<QRCode, Long>() {
            @Override
            public Long apply(QRCode input) {
                return input.getId();
            }
        });
        final List<Long> ids = qrCodeIds.subList(0, count);
        final List<QRCode> qrCodeList1 = qrCodeList.subList(0, count);
        this.qrCodeService.markCodeToDealer(dealerId, ids);
        final List<Pair<QRCode, QRCode>> pairQRCodeList = this.qrCodeService.getPairQRCodeList(qrCodeList1);
        for (Pair<QRCode, QRCode> pair : pairQRCodeList) {
            final QRCode left = pair.getLeft();
            final QRCode right = pair.getRight();
            final DistributeQRCodeRecord distributeQRCodeRecord = new DistributeQRCodeRecord();
            distributeQRCodeRecord.setCreateTime(new Date());
            distributeQRCodeRecord.setFirstLevelDealerId(0);
            distributeQRCodeRecord.setSecondLevelDealerId(dealerId);
            distributeQRCodeRecord.setCount((int) (Long.valueOf(right.getCode()) - Long.valueOf(left.getCode()) + 1));
            distributeQRCodeRecord.setStartCode(left.getCode());
            distributeQRCodeRecord.setEndCode(right.getCode());
            distributeQRCodeRecord.setType(type);
            distributeQRCodeRecord.setDistributeType(EnumQRCodeDistributeType2.ADMIN.getCode());
            records.add(distributeQRCodeRecord);
            this.distributeQRCodeRecordService.add(distributeQRCodeRecord);
        }
        return records;
    }

    /**
     * 剩余二维码个数
     *
     * @param sysType
     * @return
     */
    @Override
    public int unDistributeCount(String sysType) {
        return this.qrCodeService.getUnDistributeCountBySysType(sysType);
    }

    /**
     * 修改密码
     * @param password
     * @param id
     * @return
     */
    @Override
    public int updatePwd(String password,long id) {
        final String salt = AdminUserSupporter.generateSalt();
        final String passwordTemp = AdminUserSupporter.encryptPassword(salt, password);
        return this.adminUserDao.updatePwd(salt,passwordTemp,id);
    }

    /**
     * 员工列表
     *
     * @param adminUserListRequest
     * @return
     */
    @Override
    public PageModel<AdminUserListResponse> userList(AdminUserListRequest adminUserListRequest) {
        final PageModel<AdminUserListResponse> pageModel = new PageModel<>(adminUserListRequest.getPageNo(), adminUserListRequest.getPageSize());
        adminUserListRequest.setOffset(pageModel.getFirstIndex());
        adminUserListRequest.setCount(pageModel.getPageSize());
        if(!StringUtil.isNullOrEmpty(adminUserListRequest.getMobile())){
            adminUserListRequest.setMobile(AdminUserSupporter.encryptMobile(adminUserListRequest.getMobile()));
        }
        final long count = this.adminUserDao.selectAdminUserCountByPageParams(adminUserListRequest);
        final List<AdminUser> adminUsers = this.adminUserDao.selectAdminUserListByPageParams(adminUserListRequest);
        List<AdminUserListResponse> list = new ArrayList<AdminUserListResponse>();
        if(adminUsers.size()>0){
            for(int i=0;i<adminUsers.size();i++){
                AdminUserListResponse adminUserListResponse = new AdminUserListResponse();
                adminUserListResponse.setId(adminUsers.get(i).getId());
                adminUserListResponse.setMarkCode(adminUsers.get(i).getMarkCode());
                adminUserListResponse.setUsername(adminUsers.get(i).getUsername());
                adminUserListResponse.setRealname(adminUsers.get(i).getRealname());
                adminUserListResponse.setCompanyName(dataDictionaryService.selectDictNameByDictTypeAndDictValue(EnumDataDictionaryType.COMPANY.getId(),adminUsers.get(i).getCompanyId()));
                adminUserListResponse.setDeptName(dataDictionaryService.selectDictNameByDictTypeAndDictValue(EnumDataDictionaryType.DEPT.getId(),adminUsers.get(i).getDeptId()));
                if(adminUsers.get(i).getMobile()!=null&&!"".equals(adminUsers.get(i).getMobile())){
                    adminUserListResponse.setMobile(AdminUserSupporter.decryptMobile(adminUsers.get(i).getId(),adminUsers.get(i).getMobile()));
                }
                adminUserListResponse.setEmail(adminUsers.get(i).getEmail());
                AdminRole adminRole = adminRoleDao.selectById(adminUsers.get(i).getRoleId());
                if(adminRole!=null){
                    adminUserListResponse.setRoleName(adminRole.getRoleName());
                }
                adminUserListResponse.setCreateTime(adminUsers.get(i).getCreateTime());
                adminUserListResponse.setStatus(adminUsers.get(i).getStatus());
                list.add(adminUserListResponse);
            }
        }
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return pageModel;
    }

    /**
     * @param username
     * @param id
     * @return
     */
    @Override
    public Long selectByUsernameAndTypeUnIncludeNow(String username, int type, long id) {
        return adminUserDao.selectByUsernameAndTypeUnIncludeNow(username,type,id);
    }

    /**
     * 最后一次登陆时间
     *
     * @param id
     * @return
     */
    @Override
    public void updateLastLoginDate(long id) {
        adminUserDao.updateLastLoginDate(id);
    }

    /**
     * 创建一级代理登录用户
     * @param adminUser
     */
    @Override
    public long createFirstDealerUser(AdminUser adminUser) {
        this.adminUserDao.insert(adminUser);
        this.adminUserDao.updateMarkCode(GlobalID.GetAdminUserID(EnumGlobalAdminUserLevel.FIRSTDEALER,adminUser.getId()+""),adminUser.getId());
        return adminUser.getId();
    }
    /**
     * 创建二级代理登录用户
     * @param adminUser
     */
    @Override
    public long createSecondDealerUser(AdminUser adminUser) {
        this.adminUserDao.insert(adminUser);
        this.adminUserDao.updateMarkCode(GlobalID.GetAdminUserID(EnumGlobalAdminUserLevel.SECONDDEALER,adminUser.getId()+""),adminUser.getId());
        return adminUser.getId();
    }

    /**
     * 修改一级代理商管理账户
     *
     * @param adminUser
     * @return
     */
    @Override
    public void updateDealerUser(AdminUser adminUser) {
        this.adminUserDao.updateDealerUser(adminUser);
    }

    /**
     * 修改代理商登录密码
     *
     * @param pwd
     * @param dealerId
     */
    @Override
    public void updateDealerUserPwd(String pwd, long dealerId) {
        this.adminUserDao.updateDealerUserPwd(pwd,dealerId);
    }

    /**
     * 修改代理商登录密码
     *
     * @param pwd
     * @param id
     */
    @Override
    public void updateDealerUserPwdById(String pwd, long id) {
        this.adminUserDao.updateDealerUserPwdById(pwd,id);
    }

    /**
     * 根据代理商编码和是否有所有权限查询代理商
     * @param dealerId
     * @param isMaster
     * @return
     */
    @Override
    public Optional<AdminUser> getAdminUserByDealerIdAndIsMaster(long dealerId,int isMaster) {
        return Optional.fromNullable(this.adminUserDao.getAdminUserByDealerIdAndIsMaster(dealerId,isMaster));
    }

    /**
     * 判断是否有接口访问权限
     *
     * @param roleId
     * @param type
     * @param url
     * @param method
     * @return
     */
    @Override
    public int getPrivilegeByContions(long roleId, int type, String url, String method) {
        return adminRoleDao.getPrivilegeByContions(roleId,type,url,method);
    }

    /**
     * 判断是否有接口访问权限(js公共调用)
     *
     * @param roleId
     * @param type
     * @param descr
     * @return
     */
    @Override
    public int getPrivilegeByContionsOfJs(long roleId, int type, String descr) {
        return adminRoleDao.getPrivilegeByContionsOfJs(roleId,type,descr);
    }


    /**
     * 是否有访问的操作
     *
     * @param type
     * @param url
     * @param method
     * @return
     */
    @Override
    public int hasUrl(int type, String url, String method) {
        return adminRoleDao.hasUrl(type,url,method);
    }

    /**
     * 是否有关键字
     *
     * @param type
     * @param descr
     * @return
     */
    @Override
    public int hasDescr(int type, String descr) {
        return adminRoleDao.hasDescr(type,descr);
    }
}
