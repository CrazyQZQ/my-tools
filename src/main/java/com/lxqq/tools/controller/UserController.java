package com.lxqq.tools.controller;

import com.lxqq.tools.common.domain.AjaxResult;
import com.lxqq.tools.common.domain.LoginVo;
import com.lxqq.tools.common.domain.Route;
import com.lxqq.tools.common.domain.User;
import com.lxqq.tools.common.handler.file.FileSuffixEnum;
import com.lxqq.tools.common.handler.file.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author QinQiang
 * @date 2023/9/14
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {


    @PostMapping("login")
    public AjaxResult login(@RequestBody LoginVo loginVo) {
        User user;
        if ("admin".equals(loginVo.getUsername())) {
            user = new User()
                    .setUsername("admin")
                    .setRoles(Collections.singletonList("admin"))
                    .setAccessToken("eyJhbGciOiJIUzUxMiJ9.admin")
                    .setRefreshToken("eyJhbGciOiJIUzUxMiJ9.adminRefresh")
                    .setExpires("2023/10/30 00:00:00");
        } else {
            user = new User()
                    .setUsername("common")
                    .setRoles(Collections.singletonList("common"))
                    .setAccessToken("eyJhbGciOiJIUzUxMiJ9.common")
                    .setRefreshToken("eyJhbGciOiJIUzUxMiJ9.commonRefresh")
                    .setExpires("2023/10/30 00:00:00");
        }
        return AjaxResult.success(user);
    }

    @PostMapping("refreshToken")
    public AjaxResult refreshToken(@RequestBody LoginVo loginVo) {
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", "eyJhbGciOiJIUzUxMiJ9.newAdmin");
        result.put("refreshToken", "eyJhbGciOiJIUzUxMiJ9.newAdminRefresh");
        result.put("expires", "2023/10/30 23:59:59");
        return AjaxResult.success(result);
    }

    @GetMapping("getAsyncRoutes")
    public AjaxResult getAsyncRoutes() {
        List<Route> children = new ArrayList<>();
        Route child1 = new Route()
                .setPath("/permission/page/index")
                .setName("PermissionPage")
                .setMeta(new Route.MetaInfo().setTitle("页面权限").setRoles(Arrays.asList("admin", "common")));
        children.add(child1);
        Route child2 = new Route()
                .setPath("/permission/button/index")
                .setName("PermissionButton")
                .setMeta(new Route.MetaInfo()
                        .setTitle("按钮权限")
                        .setRoles(Arrays.asList("admin", "common"))
                        .setAuths(Arrays.asList("btn_add", "btn_edit", "btn_delete")));
        children.add(child2);
        Route route = new Route()
                .setPath("/permission")
                .setChildren(children)
                .setMeta(new Route.MetaInfo()
                        .setTitle("权限管理")
                        .setIcon("lollipop")
                        .setRank(10));
        return AjaxResult.success(Collections.singletonList(route));
    }

}
