package com.jkm.hss.admin.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.dao.AdminRoleDao;
import com.jkm.hss.admin.entity.AdminMenu;
import com.jkm.hss.admin.entity.AdminMenuOptRel;
import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.entity.AdminRoleMenuRel;
import com.jkm.hss.admin.enums.EnumAdminType;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import com.jkm.hss.admin.enums.EnumIsSelected;
import com.jkm.hss.admin.helper.requestparam.AdminRoleListRequest;
import com.jkm.hss.admin.helper.requestparam.RoleDetailRequest;
import com.jkm.hss.admin.helper.responseparam.*;
import com.jkm.hss.admin.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Slf4j
@Service
public class AdminRoleServiceImpl implements AdminRoleService{
    @Autowired
    private AdminRoleDao adminRoleDao;
    /**
     * 插入
     *
     * @param adminRole
     */
    @Override
    public void insert(AdminRole adminRole) {
        adminRoleDao.insert(adminRole);
    }

    /**
     * 启用
     *
     * @param id
     * @return
     */
    @Override
    public int enableRole(long id) {
        return adminRoleDao.enableRole(id);
    }

    /**
     * 禁用
     *
     * @param id
     * @return
     */
    @Override
    public int disableRole(long id) {
        return adminRoleDao.disableRole(id);
    }

    /**
     * 根据分页查询角色个数
     *
     * @param adminRoleListRequest
     * @return
     */
    @Override
    public long selectAdminRoleCountByPageParams(AdminRoleListRequest adminRoleListRequest) {
        return adminRoleDao.selectAdminRoleCountByPageParams(adminRoleListRequest);
    }

    /**
     * 分页查询角色列表
     *
     * @param adminRoleListRequest
     * @return
     */
    @Override
    public List<AdminRoleListPageResponse> selectAdminRoleListByPageParams(AdminRoleListRequest adminRoleListRequest) {
        return adminRoleDao.selectAdminRoleListByPageParams(adminRoleListRequest);
    }

    /**
     * 分类查询角色列表
     *
     * @param type
     * @return
     */
    @Override
    public List<AdminRoleListResponse> selectAdminRoleList(int type) {
        return adminRoleDao.selectAdminRoleList(type);
    }

    /**
     * 权限集合
     *
     * @param type
     * @return
     */
    @Override
    public List<AdminMenuOptRelListResponse> getPrivilege(int type,long roleId) {
        List<AdminMenuOptRelListResponse> list = null;
        if(roleId<=0){
            list = getAddPrivilege(type,roleId);
        }else{
            list = getEditPrivilege(type,roleId);
        }
        return list;
    }

    /**
     * 新增时获取权限
     * @param type
     * @return
     */
    private List<AdminMenuOptRelListResponse> getAddPrivilege(int type,long roleId){
        List<AdminMenuOptRelListResponse> list = new ArrayList<AdminMenuOptRelListResponse>();
        List<AdminMenu> adminMenuList =  adminRoleDao.getMenuByParentIdAndType(0,type);
        if(adminMenuList.size()>0){
            for(int i=0;i<adminMenuList.size();i++){
                AdminMenuOptRelListResponse adminMenuOptRelListResponse = new AdminMenuOptRelListResponse();
                adminMenuOptRelListResponse.setId(adminMenuList.get(i).getId());
                adminMenuOptRelListResponse.setIsSelected(EnumIsSelected.UNSELECTED.getCode());
                adminMenuOptRelListResponse.setMenuName(adminMenuList.get(i).getMenuName());
                List<AdminMenu> adminMenuChildrenList = adminRoleDao.getMenuByParentIdAndType(adminMenuList.get(i).getId(),type);
                List<AdminMenuOptRelListResponse.Menu> menus = new ArrayList<AdminMenuOptRelListResponse.Menu>();
                if(adminMenuChildrenList.size()>0){
                    for(int j=0;j<adminMenuChildrenList.size();j++){
                        AdminMenuOptRelListResponse.Menu menu = new AdminMenuOptRelListResponse.Menu();
                        menu.setId(adminMenuChildrenList.get(j).getId());
                        menu.setIsSelected(EnumIsSelected.UNSELECTED.getCode());
                        menu.setMenuName(adminMenuChildrenList.get(j).getMenuName());
                        List<AdminOptResponse> adminOptResponses = adminRoleDao.getOptByMenuIdAndType(adminMenuChildrenList.get(j).getId(),type);
                        List<AdminMenuOptRelListResponse.Opt> opts= new ArrayList<AdminMenuOptRelListResponse.Opt>();
                        if(adminOptResponses.size()>0){
                            for(int l=0;l<adminOptResponses.size();l++){
                                AdminMenuOptRelListResponse.Opt opt= new AdminMenuOptRelListResponse.Opt();
                                opt.setId(adminOptResponses.get(l).getId());
                                opt.setIsSelected(EnumIsSelected.UNSELECTED.getCode());
                                opt.setOptName(adminOptResponses.get(l).getShowName());
                                opts.add(opt);
                            }
                        }
                        menu.setOpts(opts);
                        menus.add(menu);
                    }
                }
                adminMenuOptRelListResponse.setChildren(menus);

                List<AdminOptResponse> adminOptResponses = adminRoleDao.getOptByMenuIdAndType(adminMenuList.get(i).getId(),type);
                List<AdminMenuOptRelListResponse.Opt> opts= new ArrayList<AdminMenuOptRelListResponse.Opt>();
                if(adminOptResponses.size()>0){
                    for(int k=0;k<adminOptResponses.size();k++){
                        AdminMenuOptRelListResponse.Opt opt= new AdminMenuOptRelListResponse.Opt();
                        opt.setId(adminOptResponses.get(k).getId());
                        opt.setIsSelected(EnumIsSelected.UNSELECTED.getCode());
                        opt.setOptName(adminOptResponses.get(k).getShowName());
                        opts.add(opt);
                    }
                }
                adminMenuOptRelListResponse.setOpts(opts);
                list.add(adminMenuOptRelListResponse);
            }
        }
        return list;
    }

    /**
     * 修改时获取权限
     * @param type
     * @return
     */
    private List<AdminMenuOptRelListResponse> getEditPrivilege(int type,long roleId){
        List<AdminMenuOptRelListResponse> list = new ArrayList<AdminMenuOptRelListResponse>();
        List<AdminMenuResponse> adminMenuList =  adminRoleDao.getMenuByParentIdAndTypeAndRoleId(0,type,roleId);
        if(adminMenuList.size()>0){
            for(int i=0;i<adminMenuList.size();i++){
                AdminMenuOptRelListResponse adminMenuOptRelListResponse = new AdminMenuOptRelListResponse();
                adminMenuOptRelListResponse.setId(adminMenuList.get(i).getId());
                adminMenuOptRelListResponse.setIsSelected(adminMenuList.get(i).getIsSelected());
                adminMenuOptRelListResponse.setMenuName(adminMenuList.get(i).getMenuName());
                List<AdminMenuResponse> adminMenuChildrenList = adminRoleDao.getMenuByParentIdAndTypeAndRoleId(adminMenuList.get(i).getId(),type,roleId);
                List<AdminMenuOptRelListResponse.Menu> menus = new ArrayList<AdminMenuOptRelListResponse.Menu>();
                if(adminMenuChildrenList.size()>0){
                    for(int j=0;j<adminMenuChildrenList.size();j++){
                        AdminMenuOptRelListResponse.Menu menu = new AdminMenuOptRelListResponse.Menu();
                        menu.setId(adminMenuChildrenList.get(j).getId());
                        menu.setIsSelected(adminMenuChildrenList.get(j).getIsSelected());
                        menu.setMenuName(adminMenuChildrenList.get(j).getMenuName());
                        List<AdminOptRelResponse> adminOptResponses = adminRoleDao.getOptByMenuIdAndTypeAndRoleId(adminMenuChildrenList.get(j).getId(),type,roleId);
                        List<AdminMenuOptRelListResponse.Opt> opts= new ArrayList<AdminMenuOptRelListResponse.Opt>();
                        if(adminOptResponses.size()>0){
                            for(int l=0;l<adminOptResponses.size();l++){
                                AdminMenuOptRelListResponse.Opt opt= new AdminMenuOptRelListResponse.Opt();
                                opt.setId(adminOptResponses.get(l).getId());
                                opt.setIsSelected(adminOptResponses.get(l).getIsSelected());
                                opt.setOptName(adminOptResponses.get(l).getShowName());
                                opts.add(opt);
                            }
                        }
                        menu.setOpts(opts);
                        menus.add(menu);
                    }
                }
                adminMenuOptRelListResponse.setChildren(menus);

                List<AdminOptRelResponse> adminOptResponses = adminRoleDao.getOptByMenuIdAndTypeAndRoleId(adminMenuList.get(i).getId(),type,roleId);
                List<AdminMenuOptRelListResponse.Opt> opts= new ArrayList<AdminMenuOptRelListResponse.Opt>();
                if(adminOptResponses.size()>0){
                    for(int k=0;k<adminOptResponses.size();k++){
                        AdminMenuOptRelListResponse.Opt opt= new AdminMenuOptRelListResponse.Opt();
                        opt.setId(adminOptResponses.get(k).getId());
                        opt.setIsSelected(adminOptResponses.get(k).getIsSelected());
                        opt.setOptName(adminOptResponses.get(k).getShowName());
                        opts.add(opt);
                    }
                }
                adminMenuOptRelListResponse.setOpts(opts);
                list.add(adminMenuOptRelListResponse);
            }
        }
        return list;
    }

    /**
     * 根据编码查询角色
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AdminRole> selectById(long id) {
        return Optional.fromNullable(adminRoleDao.selectById(id));
    }

    /**
     * 插入
     *
     * @param roleDetailRequest
     */

    @Override
    public void save(RoleDetailRequest roleDetailRequest) {
        if(roleDetailRequest.getRoleId()<=0){
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleName(roleDetailRequest.getRoleName());
            adminRole.setType(roleDetailRequest.getType());
            adminRole.setStatus(EnumAdminUserStatus.NORMAL.getCode());
            adminRoleDao.insert(adminRole);
            List<AdminMenuOptRelListResponse> list = roleDetailRequest.getList();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                    AdminRoleMenuRel adminRoleMenuRel = new AdminRoleMenuRel();
                    adminRoleMenuRel.setRoleId(adminRole.getId());
                    adminRoleMenuRel.setMenuId(list.get(i).getId());
                    adminRoleMenuRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                    adminRoleDao.insertRoleAndMenuRel(adminRoleMenuRel);
                }
                if(list.get(i).getOpts().size()>0){
                    List<AdminMenuOptRelListResponse.Opt> opts = list.get(i).getOpts();
                    for(int j=0;j<opts.size();j++){
                        if(opts.get(j).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                            AdminMenuOptRel adminMenuOptRel = new AdminMenuOptRel();
                            adminMenuOptRel.setMenuId(list.get(i).getId());
                            adminMenuOptRel.setRoleId(adminRole.getId());
                            adminMenuOptRel.setOptId(opts.get(j).getId());
                            adminMenuOptRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                            adminRoleDao.insertMenuAndOptRel(adminMenuOptRel);
                        }
                    }
                }
                if(list.get(i).getChildren().size()>0){
                    List<AdminMenuOptRelListResponse.Menu> menus = list.get(i).getChildren();
                    for(int k=0;k<menus.size();k++){
                        if(menus.get(k).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                            AdminRoleMenuRel adminRoleMenuRel = new AdminRoleMenuRel();
                            adminRoleMenuRel.setRoleId(adminRole.getId());
                            adminRoleMenuRel.setMenuId(menus.get(k).getId());
                            adminRoleMenuRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                            adminRoleDao.insertRoleAndMenuRel(adminRoleMenuRel);
                        }
                        if(menus.get(k).getOpts().size()>0){
                            List<AdminMenuOptRelListResponse.Opt> childOpts = menus.get(k).getOpts();
                            for(int n=0;n<childOpts.size();n++){
                                if(childOpts.get(n).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                                    AdminMenuOptRel adminMenuOptRel = new AdminMenuOptRel();
                                    adminMenuOptRel.setMenuId(menus.get(k).getId());
                                    adminMenuOptRel.setRoleId(adminRole.getId());
                                    adminMenuOptRel.setOptId(childOpts.get(n).getId());
                                    adminMenuOptRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                                    adminRoleDao.insertMenuAndOptRel(adminMenuOptRel);
                                }
                            }
                        }

                    }

                }
            }
        }else{
            adminRoleDao.updateRoleNameById(roleDetailRequest.getRoleName(),roleDetailRequest.getRoleId());
            adminRoleDao.deleteRoleAndMenuByRoleId(roleDetailRequest.getRoleId());
            adminRoleDao.deleteMenuAndOptByRoleId(roleDetailRequest.getRoleId());
            List<AdminMenuOptRelListResponse> list = roleDetailRequest.getList();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                    AdminRoleMenuRel adminRoleMenuRel = new AdminRoleMenuRel();
                    adminRoleMenuRel.setRoleId(roleDetailRequest.getRoleId());
                    adminRoleMenuRel.setMenuId(list.get(i).getId());
                    adminRoleMenuRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                    adminRoleDao.insertRoleAndMenuRel(adminRoleMenuRel);
                }
                if(list.get(i).getOpts().size()>0){
                    List<AdminMenuOptRelListResponse.Opt> opts = list.get(i).getOpts();
                    for(int j=0;j<opts.size();j++){
                        if(opts.get(j).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                            AdminMenuOptRel adminMenuOptRel = new AdminMenuOptRel();
                            adminMenuOptRel.setMenuId(list.get(i).getId());
                            adminMenuOptRel.setRoleId(roleDetailRequest.getRoleId());
                            adminMenuOptRel.setOptId(opts.get(j).getId());
                            adminMenuOptRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                            adminRoleDao.insertMenuAndOptRel(adminMenuOptRel);
                        }
                    }
                }
                if(list.get(i).getChildren().size()>0){
                    List<AdminMenuOptRelListResponse.Menu> menus = list.get(i).getChildren();
                    for(int k=0;k<menus.size();k++){
                        if(menus.get(k).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                            AdminRoleMenuRel adminRoleMenuRel = new AdminRoleMenuRel();
                            adminRoleMenuRel.setRoleId(roleDetailRequest.getRoleId());
                            adminRoleMenuRel.setMenuId(menus.get(k).getId());
                            adminRoleMenuRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                            adminRoleDao.insertRoleAndMenuRel(adminRoleMenuRel);
                        }
                        if(menus.get(k).getOpts().size()>0){
                            List<AdminMenuOptRelListResponse.Opt> childOpts = menus.get(k).getOpts();
                            for(int n=0;n<childOpts.size();n++){
                                if(childOpts.get(n).getIsSelected()==EnumIsSelected.SELECTED.getCode()){
                                    AdminMenuOptRel adminMenuOptRel = new AdminMenuOptRel();
                                    adminMenuOptRel.setMenuId(menus.get(k).getId());
                                    adminMenuOptRel.setRoleId(roleDetailRequest.getRoleId());
                                    adminMenuOptRel.setOptId(childOpts.get(n).getId());
                                    adminMenuOptRel.setStatus(EnumAdminUserStatus.NORMAL.getCode());
                                    adminRoleDao.insertMenuAndOptRel(adminMenuOptRel);
                                }
                            }
                        }

                    }

                }
            }
        }
    }

    /**
     * 角色列表
     *
     * @param adminRoleListRequest
     * @return
     */
    @Override
    public PageModel<AdminRoleListPageResponse> roleListByPage(AdminRoleListRequest adminRoleListRequest) {
        PageModel<AdminRoleListPageResponse> pageModel = new PageModel<>(adminRoleListRequest.getPageNo(),adminRoleListRequest.getPageSize());
        adminRoleListRequest.setOffset(pageModel.getFirstIndex());
        adminRoleListRequest.setCount(pageModel.getPageSize());
        long total = this.selectAdminRoleCountByPageParams(adminRoleListRequest);
        List<AdminRoleListPageResponse> adminRoleListPageResponses = this.selectAdminRoleListByPageParams(adminRoleListRequest);
        pageModel.setCount(total);
        pageModel.setRecords(adminRoleListPageResponses);
        return pageModel;
    }

    /**
     * 根据角色编码和类型查询登录菜单
     *
     * @param roleId
     * @param type
     * @return
     */
    @Override
    public List<AdminUserLoginResponse> getLoginMenu(long roleId, int type,int isMaster) {
        List<AdminUserLoginResponse> list = new ArrayList<AdminUserLoginResponse>();
        if(isMaster==1){
            List<MenuResponse> adminMenuList = adminRoleDao.getMasterLoginMenu(0,type);
            if(adminMenuList.size()>0){
                for(int i=0;i<adminMenuList.size();i++){
                    AdminUserLoginResponse adminUserLoginResponse = new AdminUserLoginResponse();
                    adminUserLoginResponse.setId(adminMenuList.get(i).getId());
                    adminUserLoginResponse.setMenuName(adminMenuList.get(i).getMenuName());
                    adminUserLoginResponse.setUrl(adminMenuList.get(i).getMenuUrl());
                    adminUserLoginResponse.setChildren(adminRoleDao.getMasterLoginMenu(adminMenuList.get(i).getId(),type));
                    list.add(adminUserLoginResponse);
                }
            }
        }else{
            List<MenuResponse> menuResponses = adminRoleDao.getLoginMenu(0,type,roleId);
            if(menuResponses.size()>0){
                for(int i=0;i<menuResponses.size();i++){
                    AdminUserLoginResponse adminUserLoginResponse = new AdminUserLoginResponse();
                    adminUserLoginResponse.setId(menuResponses.get(i).getId());
                    adminUserLoginResponse.setMenuName(menuResponses.get(i).getMenuName());
                    adminUserLoginResponse.setUrl(menuResponses.get(i).getMenuUrl());
                    adminUserLoginResponse.setChildren(adminRoleDao.getLoginMenu(menuResponses.get(i).getId(),type,roleId));
                    list.add(adminUserLoginResponse);
                }
            }
        }
        return list;
    }


    /**
     * 根据名称和类型查询角色
     *
     * @param roleName
     * @param type
     * @return
     */
    @Override
    public Optional<AdminRole> selectByRoleNameAndType(String roleName, int type) {
        return Optional.fromNullable(adminRoleDao.selectByRoleNameAndType(roleName,type));
    }

    /**
     * 根据名称和类型查询角色是否重复
     *
     * @param roleName
     * @param type
     * @param roleId
     * @return
     */
    @Override
    public Optional<AdminRole> selectByRoleNameAndTypeUnIncludeNow(String roleName, int type, long roleId) {
        return Optional.fromNullable(adminRoleDao.selectByRoleNameAndTypeUnIncludeNow(roleName,type,roleId));
    }

}
