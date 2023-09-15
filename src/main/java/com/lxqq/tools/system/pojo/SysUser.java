package com.lxqq.tools.system.pojo;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @Description: 用户表(SysUser)实体类
 * @Author QinQiang
 * @Date 2023-09-15 10:18:44
 */
@Data
@Table("sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = -58371796898349129L;

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别，1男2女
     */
    private String gender;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 随机盐
     */
    private String salt;

    /**
     * 简介
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 0-正常，9-锁定
     */
    private String lockFlag;

    /**
     * 0-正常，1-删除
     */
    private String delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 角色
     */
    @RelationManyToMany(
            joinTable = "sys_user_role",
            joinSelfColumn = "user_id",
            joinTargetColumn = "role_id"
    )
    private List<SysRole> roles;

}

