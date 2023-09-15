package com.lxqq.tools.system.pojo;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @Description: 系统角色表(SysRole)实体类
 * @Author QinQiang
 * @Date 2023-09-15 10:18:41
 */
@Data
@Table("sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = -47164742085699056L;
    /**
     * 角色ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 删除标识：0-正常，1-删除
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
     * 修改人
     */
    private String updateBy;

    /**
     * 创建人
     */
    private String createBy;

}

