package com.jkm.hss.admin.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.entity.*;
import com.jkm.hss.admin.helper.requestparam.AdminDealerUserListRequest;
import com.jkm.hss.admin.helper.requestparam.AdminUserListRequest;
import com.jkm.hss.admin.helper.responseparam.AdminUserDealerListResponse;
import com.jkm.hss.admin.helper.responseparam.AdminUserListResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * Created by huangwei on 6/3/15.
 */
public interface AdminUserService {
    /**
     * 创建一个后台用户
     *
     * @param adminUser
     * @return
     */
    long createUser(AdminUser adminUser);

    /**
     *
     *
     * @param adminUser
     * @return
     */
    int update(AdminUser adminUser);

    /**
     * 根据auid获取
     *
     * @param auid
     * @return
     */
    Optional<AdminUser> getAdminUserById(long auid);

    /**
     * 根据用户名和类型获取
     *
     * @param username
     * @return
     */
    Optional<AdminUser> getAdminUserByNameAndType(final String username,final int type);
    /**
     * 根据用户名和类型获取
     *
     * @param username
     * @return
     */
    Optional<AdminUser> getAdminUserByNameAndTypeUnIncludeNow(final String username,final int type,final long dealerId);

    /**
     * 禁用用户
     *
     * @param auid
     */
    void disableUser(final long auid);

    /**
     * 激活用户
     *
     * @param auid
     */
    void activeUser(final long auid);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    Optional<AdminUserPassport> login(final String username, final String password);

    /**
     * 登出
     *
     * @param auid
     */
    void logout(final long auid);

    /**
     * 给一级代理商分配码段
     *
     * @param count
     * @param firstLevelDealerId
     * @param count
     * @return
     */
    Triple<Integer, String, List<Pair<QRCode, QRCode>>> distributeQRCode(long adminId, long firstLevelDealerId, int count,String sysType);

    /**
     * 给一级代理商分配指定范围的码段
     *
     * @param dealerId
     * @param startCode
     * @param endCode
     */
    List<Pair<QRCode, QRCode>> distributeRangeQRCode(long dealerId, String startCode, String endCode, String sysType);

    /**
     * 查询码段状态
     * @param code
     * @return
     */
    CodeQueryResponse getCode(String code);

    /**
     * 商户id查询其名称
     * @param firstLevelDealerId
     * @return
     */
    CodeQueryResponse getProxyName(long firstLevelDealerId);

    /**
     * 根据firstLevelDealerId
     * 查询一级代理商名称
     * @param secondLevelDealerId
     * @return
     */
    CodeQueryResponse getProxyName1(long secondLevelDealerId);

    /**
     * 根据merchantId
     * 查询商户名称
     * @param merchantId
     * @return
     */
    CodeQueryResponse getMerchantName(long merchantId);


    /**
     * 按码段分配二维码
     * @param type
     * @param dealerId
     * @param startCode
     * @param endCode
     * @return
     */
    List<DistributeQRCodeRecord> distributeQRCodeByCode(int type, String sysType, long dealerId,  String startCode, String endCode);

    /**
     * 按个数分配
     * @param type
     * @param dealerId
     * @param count
     * @return
     */
    List<DistributeQRCodeRecord> distributeQRCodeByCount(int type, String sysType, long dealerId, int count);

    /**
     * 剩余二维码个数
     * @param sysType
     * @return
     */
    int unDistributeCount(String sysType);

    /**
     * 修改密码
     * @param password
     * @param id
     * @return
     */
    int updatePwd(String password,long id);

    /**
     * 员工列表
     * @param adminUserListRequest
     * @return
     */
    PageModel<AdminUserListResponse> userList(AdminUserListRequest adminUserListRequest);
    /**
     * 员工列表
     * @param adminDealerUserListRequest
     * @return
     */
    PageModel<AdminUserDealerListResponse> userDealerList(AdminDealerUserListRequest adminDealerUserListRequest);

    /**
     *
     * @param username
     * @param id
     * @return
     */
    Long selectByUsernameAndTypeUnIncludeNow(String username,int type,long id);

    /**
     * 最后一次登陆时间
     * @param id
     * @return
     */
    void updateLastLoginDate(long id);

    /**
     * 创建一级代理商管理账户
     * @param adminUser
     * @return
     */
    long createFirstDealerUser(AdminUser adminUser);
    /**
     * 创建二级代理商管理账户
     * @param adminUser
     * @return
     */
    long createSecondDealerUser(AdminUser adminUser);
    /**
     * 修改一级代理商管理账户
     * @param adminUser
     * @return
     */
    void updateDealerUser(AdminUser adminUser);

    /**
     * 修改代理商登录密码
     * @param pwd
     * @param dealerId
     */
    void updateDealerUserPwd(String pwd,long dealerId);
    /**
     * 修改代理商登录密码
     * @param pwd
     * @param id
     */
    void updateDealerUserPwdById(String pwd,long id);

    /**
     * 根据代理商编码和是否有所有权限查询代理商
     * @param dealerId
     * @param isMaster
     * @return
     */
    Optional<AdminUser> getAdminUserByDealerIdAndIsMaster(final long dealerId,final int isMaster);

    /**
     * 判断是否有接口访问权限
     * @param roleId
     * @param type
     * @param url
     * @param method
     * @return
     */
    int getPrivilegeByContions(long roleId,int type,String url,String method);
    /**
     * 判断是否有接口访问权限(js公共调用)
     * @param roleId
     * @param type
     * @param descr
     * @return
     */
    int getPrivilegeByContionsOfJs(long roleId,int type,String descr);

    /**
     * 是否有访问的操作
     * @param type
     * @param url
     * @param method
     * @return
     */
    int hasUrl(int type,String url,String method);

    /**
     * 是否有关键字
     * @param type
     * @param descr
     * @return
     */
    int hasDescr(int type,String descr);
    /**
     * 查询用户密码
     * @param id
     * @return
     */
    String getPwd(long id);
}
