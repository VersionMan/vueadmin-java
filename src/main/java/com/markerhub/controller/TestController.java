package com.markerhub.controller;
import com.markerhub.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    SysUserService sysUserService;
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public  Object Test(){
        System.out.println("进");
        return  sysUserService.list();
    }

}
