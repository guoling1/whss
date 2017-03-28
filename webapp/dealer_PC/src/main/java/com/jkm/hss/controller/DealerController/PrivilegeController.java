package com.jkm.hss.controller.DealerController;

import com.aliyun.oss.OSSClient;
import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.CookieUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.enums.EnumIsMaster;
import com.jkm.hss.admin.helper.AdminUserSupporter;
import com.jkm.hss.admin.helper.requestparam.*;
import com.jkm.hss.admin.helper.responseparam.*;
import com.jkm.hss.admin.service.AdminRoleService;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.service.DealerPassportService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/3/23.
 */
@Slf4j
@RestController
@RequestMapping(value = "/daili/privilege")
public class PrivilegeController extends BaseController {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private DealerPassportService dealerPassportService;
    @Autowired
    private OSSClient ossClient;
    @Autowired
    private DealerService dealerService;
    /**
     * 新增员工
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public CommonResponse addUser (@RequestBody AdminUserRequest adminUserRequest) {
        Optional<Dealer> dealerOptional = super.getDealer();
        int level = dealerOptional.get().getLevel();
        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(level==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(level==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        final Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByNameAndType(adminUserRequest.getUsername(), type);
        if(adminUserOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "登录名已存在");
        }
        if (!ValidateUtils.isMobile(adminUserRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (!ValidateUtils.isIDCard(adminUserRequest.getIdCard())) {
            return CommonResponse.simpleResponse(-1, "身份证号格式错误");
        }
        if (!ValidateUtils.isEmail(adminUserRequest.getEmail())) {
            return CommonResponse.simpleResponse(-1, "邮箱格式错误");
        }
        if (!(adminUserRequest.getUsername().length() >= 4 && adminUserRequest.getUsername().length() <= 16)) {
            return CommonResponse.simpleResponse(-1, "用户名长度4-16位");
        }
        if (!(adminUserRequest.getPassword().length() >= 6 && adminUserRequest.getPassword().length() <= 32)) {
            return CommonResponse.simpleResponse(-1, "密码长度6-32位");
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(adminUserRequest.getUsername());
        adminUser.setSalt(level+"00000");
        adminUser.setPassword(DealerSupport.passwordDigest(adminUserRequest.getPassword(),"JKM"));
        adminUser.setRealname(adminUserRequest.getRealname());
        adminUser.setEmail(adminUserRequest.getEmail());
        adminUser.setMobile(AdminUserSupporter.encryptMobile(adminUserRequest.getMobile()));
        adminUser.setCompanyId(adminUserRequest.getCompanyId());
        adminUser.setDeptId(adminUserRequest.getDeptId());
        adminUser.setIdCard(AdminUserSupporter.encryptIdenrity(adminUserRequest.getIdCard()));
        adminUser.setRoleId(adminUserRequest.getRoleId());
        adminUser.setIdentityFacePic(adminUserRequest.getIdentityFacePic());
        adminUser.setIdentityOppositePic(adminUserRequest.getIdentityOppositePic());
        adminUser.setType(type);
        adminUser.setDealerId(super.getDealerId());
        adminUser.setIsMaster(EnumIsMaster.NOTMASTER.getCode());
        adminUser.setStatus(EnumAdminUserStatus.NORMAL.getCode());
        long backId = 0;
        if(level==1){
            backId = this.adminUserService.createFirstDealerUser(adminUser);
        }
        if(level==2){
            backId = this.adminUserService.createSecondDealerUser(adminUser);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "新增成功",backId);
    }

    /**
     * 禁用用户
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableUser", method = RequestMethod.POST)
    public CommonResponse disableUser (final HttpServletResponse response, @RequestBody AdminUserRequest adminUserRequest) {
        adminUserService.disableUser(adminUserRequest.getId());
        Optional<AdminUser> adminUserOptional = adminUserService.getAdminUserById(adminUserRequest.getId());
        if(adminUserOptional.isPresent()){
            if(adminUserOptional.get().getDealerId()==super.getDealerId()){
                this.dealerPassportService.markAsLogout(getDealerId());
                CookieUtil.deleteCookie(response, ApplicationConsts.DEALER_COOKIE_KEY, ApplicationConsts.getApplicationConfig().domain());
            }
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "禁用成功");
    }

    /**
     * 启用用户
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activeUser", method = RequestMethod.POST)
    public CommonResponse activeUser (@RequestBody AdminUserRequest adminUserRequest) {
        adminUserService.activeUser(adminUserRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "启用成功");
    }

    /**
     * 修改密码
     * @param adminUserRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public CommonResponse updatePwd (@RequestBody AdminUserRequest adminUserRequest) {
        if(adminUserRequest.getId()<=0){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        if(StringUtils.isEmpty(adminUserRequest.getPassword())){
            return CommonResponse.simpleResponse(-1, "请输入密码");
        }
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserById(adminUserRequest.getId());
        if(!adminUserOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        adminUserService.updateDealerUserPwdById(DealerSupport.passwordDigest(adminUserRequest.getPassword(),"JKM"),adminUserRequest.getId());
        adminUserService.updatePwd(adminUserRequest.getPassword(),adminUserRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
    }

    /**
     * 编辑用户
     * @param adminUser
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public CommonResponse updateUser (@RequestBody AdminUser adminUser) {
        if(adminUser.getId()<=0){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserById(adminUser.getId());
        if(!adminUserOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }
        Optional<Dealer> dealerOptional = dealerService.getById(adminUserOptional.get().getDealerId());
        if(!dealerOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "登录名不存在");
        }

        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(dealerOptional.get().getLevel()==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(dealerOptional.get().getLevel()==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        final long proxyNameCount = this.adminUserService.selectByUsernameAndTypeUnIncludeNow(adminUser.getUsername(),type, adminUser.getId());
        if (proxyNameCount > 0) {
            return CommonResponse.simpleResponse(-1, "登录名已经存在");
        }
        adminUserService.update(adminUser);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "修改成功");
    }
    /**
     * 用户列表
     * @param adminUserListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public CommonResponse userList (@RequestBody AdminUserListRequest adminUserListRequest) {
        Optional<Dealer> dealerOptional = super.getDealer();
        int level = dealerOptional.get().getLevel();
        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(level==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(level==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        adminUserListRequest.setType(type);
        adminUserListRequest.setDealerId(super.getDealerId());
        PageModel<AdminUserListResponse> adminUserPageModel = adminUserService.userList(adminUserListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserPageModel);
    }

    /**
     * 员工详情
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userDetail/{userId}", method = RequestMethod.GET)
    public CommonResponse userDetail (@PathVariable final long userId) {
        Optional<AdminUser> adminUserOptional = adminUserService.getAdminUserById(userId);
        if(!adminUserOptional.isPresent()){
            return  CommonResponse.simpleResponse(-1,"该员工不存在");
        }
        Date expiration = new Date(new Date().getTime() + 30*60*1000);
        AdminUserResponse adminUserResponse = new AdminUserResponse();
        if(adminUserOptional.get().getMobile()!=null&&!"".equals(adminUserOptional.get().getMobile())){
            adminUserResponse.setMobile(AdminUserSupporter.decryptMobile(userId,adminUserOptional.get().getMobile()));
        }
        if(adminUserOptional.get().getIdCard()!=null&&!"".equals(adminUserOptional.get().getIdCard())){
            adminUserResponse.setIdCard(AdminUserSupporter.decryptIdentity(adminUserOptional.get().getIdCard()));
        }
        if(adminUserOptional.get().getIdentityFacePic()!=null&&!"".equals(adminUserOptional.get().getIdentityFacePic())){
            adminUserResponse.setIdentityFacePic(adminUserOptional.get().getIdentityFacePic());
            URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), adminUserResponse.getIdentityFacePic(),expiration);
            adminUserResponse.setRealIdentityFacePic(url.toString());
        }
        if(adminUserOptional.get().getIdentityOppositePic()!=null&&!"".equals(adminUserOptional.get().getIdentityOppositePic())){
            adminUserResponse.setIdentityOppositePic(adminUserOptional.get().getIdentityOppositePic());
            URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), adminUserResponse.getIdentityOppositePic(),expiration);
            adminUserResponse.setRealIdentityOppositePic(url.toString());
        }
        adminUserResponse.setId(adminUserOptional.get().getId());
        adminUserResponse.setStatus(adminUserOptional.get().getStatus());
        adminUserResponse.setUsername(adminUserOptional.get().getUsername());
        adminUserResponse.setRealname(adminUserOptional.get().getRealname());
        adminUserResponse.setEmail(adminUserOptional.get().getEmail());
        adminUserResponse.setCompanyId(adminUserOptional.get().getCompanyId());
        adminUserResponse.setDeptId(adminUserOptional.get().getDeptId());
        adminUserResponse.setRoleId(adminUserOptional.get().getRoleId());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserResponse);
    }


    /**
     * 角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userRoleList", method = RequestMethod.GET)
    public CommonResponse userRoleList () {
        Optional<Dealer> dealerOptional = super.getDealer();
        int level = dealerOptional.get().getLevel();
        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(level==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(level==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        List<AdminRoleListResponse> adminRoleListResponses = adminRoleService.selectAdminRoleList(type);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminRoleListResponses);
    }


    /**
     * 角色详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRoleDetail", method = RequestMethod.POST)
    public CommonResponse getRoleDetail (@RequestBody AdminRoleDetailRequest adminRoleDetailRequest) {
        Optional<Dealer> dealerOptional = super.getDealer();
        int level = dealerOptional.get().getLevel();
        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(level==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(level==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        RoleDetailResponse roleDetailResponse = new RoleDetailResponse();
        if(adminRoleDetailRequest.getId()>0){
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectById(adminRoleDetailRequest.getId());
            if(!adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色不存在");
            }
            roleDetailResponse.setRoleId(adminRoleDetailRequest.getId());
            roleDetailResponse.setRoleName(adminRoleOptional.get().getRoleName());
        }
        List<AdminMenuOptRelListResponse> adminMenuOptRelListResponses = adminRoleService.getPrivilege(type,adminRoleDetailRequest.getId());
        roleDetailResponse.setList(adminMenuOptRelListResponses);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",roleDetailResponse);
    }

    /**
     * 添加或修改角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveRole", method = RequestMethod.POST)
    public CommonResponse saveRole (@RequestBody RoleDetailRequest roleDetailRequest) {
        Optional<Dealer> dealerOptional = super.getDealer();
        int level = dealerOptional.get().getLevel();
        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(level==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(level==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        if(roleDetailRequest.getRoleName()==null||"".equals(roleDetailRequest.getRoleName())){
            return CommonResponse.simpleResponse(-1, "请输入角色名");
        }
        if(roleDetailRequest.getList()==null||roleDetailRequest.getList().size()<=0){
            return CommonResponse.simpleResponse(-1, "请选择菜单");
        }
        if(roleDetailRequest.getRoleId()<=0){
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectByRoleNameAndType(roleDetailRequest.getRoleName(),type);
            if(adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色名已存在");
            }
        }else{
            Optional<AdminRole> adminRoleOptional = adminRoleService.selectByRoleNameAndTypeUnIncludeNow(roleDetailRequest.getRoleName(),type,roleDetailRequest.getRoleId());
            if(adminRoleOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "角色名已存在");
            }
        }
        roleDetailRequest.setType(type);
        adminRoleService.save(roleDetailRequest);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "操作成功");
    }

    /**
     * 分页查询角色列表
     * @param adminRoleListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/roleListByPage", method = RequestMethod.POST)
    public CommonResponse roleListByPage (@RequestBody AdminRoleListRequest adminRoleListRequest) {
        Optional<Dealer> dealerOptional = super.getDealer();
        int level = dealerOptional.get().getLevel();
        int type = EnumAdminType.FIRSTDEALER.getCode();
        if(level==1){
            type=EnumAdminType.FIRSTDEALER.getCode();
        }
        if(level==2){
            type=EnumAdminType.SECONDDEALER.getCode();
        }
        adminRoleListRequest.setType(type);
        PageModel<AdminRoleListPageResponse> adminUserPageModel = adminRoleService.roleListByPage(adminRoleListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",adminUserPageModel);
    }

    /**
     * 禁用角色
     * @param adminRoleDetailRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableRole", method = RequestMethod.POST)
    public CommonResponse disableRole (final HttpServletResponse response,@RequestBody AdminRoleDetailRequest adminRoleDetailRequest) {
        adminRoleService.disableRole(adminRoleDetailRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "禁用成功");
    }
    /**
     * 启用角色
     * @param adminRoleDetailRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activeRole", method = RequestMethod.POST)
    public CommonResponse activeRole (@RequestBody AdminRoleDetailRequest adminRoleDetailRequest) {
        adminRoleService.enableRole(adminRoleDetailRequest.getId());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "启用成功");
    }
}
