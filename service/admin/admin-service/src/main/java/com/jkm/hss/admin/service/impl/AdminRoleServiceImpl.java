package com.jkm.hss.admin.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.admin.dao.AdminRoleDao;
import com.jkm.hss.admin.entity.AdminMenu;
import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.enums.EnumIsSelected;
import com.jkm.hss.admin.helper.requestparam.AdminRoleListRequest;
import com.jkm.hss.admin.helper.requestparam.RoleDetailRequest;
import com.jkm.hss.admin.helper.responseparam.*;
import com.jkm.hss.admin.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<AdminRole> selectAdminRoleListByPageParams(AdminRoleListRequest adminRoleListRequest) {
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
    public List<AdminMenuOptRelListResponse> getPrivilege(int type,long roleId,long userId) {
        List<AdminMenuOptRelListResponse> list = null;
        if(roleId<=0){
            list = getAddPrivilege(type,roleId);
        }else{
            list = getEditPrivilege(type,roleId,userId);
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
                List<AdminMenu> adminMenuChildrenList = adminRoleDao.getMenuByParentIdAndType(adminMenuList.get(i).getParentId(),type);
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
    private List<AdminMenuOptRelListResponse> getEditPrivilege(int type,long roleId,long userId){
        List<AdminMenuOptRelListResponse> list = new ArrayList<AdminMenuOptRelListResponse>();
        List<AdminMenuResponse> adminMenuList =  adminRoleDao.getMenuByParentIdAndTypeAndRoleId(0,type,roleId,userId);
        if(adminMenuList.size()>0){
            for(int i=0;i<adminMenuList.size();i++){
                AdminMenuOptRelListResponse adminMenuOptRelListResponse = new AdminMenuOptRelListResponse();
                adminMenuOptRelListResponse.setId(adminMenuList.get(i).getId());
                adminMenuOptRelListResponse.setIsSelected(adminMenuList.get(i).getIsSelected());
                adminMenuOptRelListResponse.setMenuName(adminMenuList.get(i).getMenuName());
                List<AdminMenuResponse> adminMenuChildrenList = adminRoleDao.getMenuByParentIdAndTypeAndRoleId(adminMenuList.get(i).getParentId(),type,roleId,userId);
                List<AdminMenuOptRelListResponse.Menu> menus = new ArrayList<AdminMenuOptRelListResponse.Menu>();
                if(adminMenuChildrenList.size()>0){
                    for(int j=0;j<adminMenuChildrenList.size();j++){
                        AdminMenuOptRelListResponse.Menu menu = new AdminMenuOptRelListResponse.Menu();
                        menu.setId(adminMenuChildrenList.get(j).getId());
                        menu.setIsSelected(adminMenuChildrenList.get(i).getIsSelected());
                        menu.setMenuName(adminMenuChildrenList.get(j).getMenuName());
                        List<AdminOptRelResponse> adminOptResponses = adminRoleDao.getOptByMenuIdAndTypeAndUserId(adminMenuChildrenList.get(j).getId(),type,userId);
                        List<AdminMenuOptRelListResponse.Opt> opts= new ArrayList<AdminMenuOptRelListResponse.Opt>();
                        if(adminOptResponses.size()>0){
                            for(int l=0;l<adminOptResponses.size();l++){
                                AdminMenuOptRelListResponse.Opt opt= new AdminMenuOptRelListResponse.Opt();
                                opt.setId(adminOptResponses.get(l).getId());
                                opt.setIsSelected(adminOptResponses.get(i).getIsSelected());
                                opt.setOptName(adminOptResponses.get(l).getShowName());
                                opts.add(opt);
                            }
                        }
                        menu.setOpts(opts);
                        menus.add(menu);
                    }
                }
                adminMenuOptRelListResponse.setChildren(menus);

                List<AdminOptRelResponse> adminOptResponses = adminRoleDao.getOptByMenuIdAndTypeAndUserId(adminMenuList.get(i).getId(),type,userId);
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

    }


}
