package com.lxqq.tools.system.pojo;

import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 用户角色表(SysUserRole)实体类
 * @Author QinQiang
 * @Date 2023-09-15 11:23:50
 */
@Data
@Table("sys_user_role")
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 234879025505928969L;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}

