package com.lxqq.tools.system.pojo;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @Description: 文件管理表(SysFile)实体类
 * @Author QinQiang
 * @Date 2023-09-15 10:18:44
 */
@Data
@Table("sys_file")
public class SysFile implements Serializable {
    private static final long serialVersionUID = -15485224236356890L;
    /**
     * 文件ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件存储桶名称
     */
    private String bucketName;

    /**
     * 原始文件名
     */
    private String original;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 删除标志：0-正常，1-删除
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

}

