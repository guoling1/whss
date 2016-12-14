package com.jkm.hss.admin.service.impl;


import com.google.common.base.Optional;
import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.service.AdminUserInitService;
import com.jkm.hss.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/11/24.
 */
@Service
public class AdminUserInitServiceImpl implements AdminUserInitService {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initAdminUser() {
        final AdminUser adminUser = new AdminUser();
        adminUser.setUsername("root");
        adminUser.setRealname("root");
        adminUser.setPassword("123456");
        adminUser.setEmail("");
        adminUser.setMobile("18611429994");
        this.addAdminUser(adminUser);

        final AdminUser adminUser1 = new AdminUser();
        adminUser1.setUsername("admin");
        adminUser1.setRealname("admin");
        adminUser1.setPassword("123456");
        adminUser1.setEmail("");
        adminUser1.setMobile("18640426296");
        this.addAdminUser(adminUser1);

    }

    private void addAdminUser(final AdminUser adminUser) {
        final Optional<AdminUser> adminUserOptional = this.adminUserService.getAdminUserByName(adminUser.getUsername());
        if (!adminUserOptional.isPresent()) {
            this.adminUserService.createUser(adminUser);
        }
    }
}
