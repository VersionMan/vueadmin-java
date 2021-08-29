package com.markerhub;


import com.markerhub.entity.SysUser;
import com.markerhub.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class Testt {

    @Autowired
    SysUserService sysUserService;
    @Test
    public  void  get(){
        List<SysUser> a   = sysUserService.list();
        for (SysUser sysUser : a) {
            System.out.printf("sysUser"+sysUser);
        }
    }
}
