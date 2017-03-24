package com.jkm.hss.admin.service.impl;

import com.jkm.hss.admin.dao.AdminRoleDao;
import com.jkm.hss.admin.entity.AdminMenu;
import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.helper.requestparam.AdminRoleListRequest;
import com.jkm.hss.admin.helper.responseparam.AdminMenuOptRelListResponse;
import com.jkm.hss.admin.helper.responseparam.AdminOptResponse;
import com.jkm.hss.admin.helper.responseparam.AdminRoleListResponse;
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
    public List<AdminMenuOptRelListResponse> getPrivilege(int type) {
        List<AdminMenuOptRelListResponse> list = new ArrayList<AdminMenuOptRelListResponse>();
        List<AdminMenu> adminMenuList =  adminRoleDao.getMenuByParentIdAndType(0,type);
        if(adminMenuList.size()>0){
            for(int i=0;i<adminMenuList.size();i++){
                AdminMenuOptRelListResponse adminMenuOptRelListResponse = new AdminMenuOptRelListResponse();
                adminMenuOptRelListResponse.setId(adminMenuList.get(i).getId());
                adminMenuOptRelListResponse.setMenuName(adminMenuList.get(i).getMenuName());
                adminMenuOptRelListResponse.setMenuUrl(adminMenuList.get(i).getMenuUrl());
                List<AdminMenu> adminMenuChildrenList = adminRoleDao.getMenuByParentIdAndType(adminMenuList.get(i).getId(),type);
                List<AdminMenuOptRelListResponse.Menu> menus = new ArrayList<AdminMenuOptRelListResponse.Menu>();
                if(adminMenuChildrenList.size()>0){
                    for(int j=0;j<adminMenuChildrenList.size();j++){
                        AdminMenuOptRelListResponse.Menu menu = new AdminMenuOptRelListResponse.Menu();
                        menu.setId(adminMenuChildrenList.get(j).getId());
                        menu.setMenuName(adminMenuChildrenList.get(j).getMenuName());
                        menus.add(menu);
                    }
                }
                adminMenuOptRelListResponse.setChildren(menus);

                List<AdminOptResponse> adminOptResponses = adminRoleDao.getOptByMenuIdAndType(adminMenuList.get(i).getId(),type);
                List<AdminMenuOptRelListResponse.Opt> opts= new ArrayList<AdminMenuOptRelListResponse.Opt>();
                if(adminOptResponses.size()>0){
                    for(int k=0;k<opts.size();k++){
                        AdminMenuOptRelListResponse.Opt opt= new AdminMenuOptRelListResponse.Opt();
                        opt.setId(opts.get(k).getId());
                        opt.setOptName(opts.get(k).getOptName());
                        opts.add(opt);
                    }
                }
                adminMenuOptRelListResponse.setOpts(opts);
                list.add(adminMenuOptRelListResponse);
            }
        }
        return list;
    }


}
