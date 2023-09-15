package com.lxqq.tools.system.service;

import cn.hutool.core.date.DateUtil;
import com.lxqq.tools.common.ServiceException;
import com.lxqq.tools.common.domain.LoginUserVo;
import com.lxqq.tools.system.mapper.SysUserMapper;
import com.lxqq.tools.system.pojo.SysRole;
import com.lxqq.tools.system.pojo.SysUser;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.lxqq.tools.system.pojo.table.SysRoleTableDef.SYS_ROLE;
import static com.lxqq.tools.system.pojo.table.SysUserRoleTableDef.SYS_USER_ROLE;
import static com.lxqq.tools.system.pojo.table.SysUserTableDef.SYS_USER;

/**
 * @author QinQiang
 * @date 2023/9/15
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    @Autowired
    private SysUserMapper userMapper;

    public LoginUserVo loadByUsername(String username) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(SYS_USER.ALL_COLUMNS, SYS_ROLE.ALL_COLUMNS)
                .from(SYS_USER)
                .leftJoin(SYS_USER_ROLE).on(SYS_USER_ROLE.USER_ID.eq(SYS_USER.ID))
                .leftJoin(SYS_ROLE).on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ID))
                .where(SYS_USER.USERNAME.eq(username));
        SysUser sysUser = userMapper.selectOneByQuery(queryWrapper);
        if (sysUser == null) {
            throw new ServiceException("用户不存在!");
        }
        List<String> roleCodes = sysUser.getRoles().stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        String expireTime = DateUtil.format(LocalDateTime.now().plusDays(1), "yyyy/MM/dd HH:mm:ss");
        return new LoginUserVo()
                .setUsername(sysUser.getUsername())
                .setPassword(sysUser.getPassword())
                .setRoles(roleCodes)
                .setAccessToken("eyJhbGciOiJIUzUxMiJ9.admin")
                .setRefreshToken("eyJhbGciOiJIUzUxMiJ9.adminRefresh")
                .setExpires(expireTime);
    }

}
