package com.lxqq.tools.common.handler.file.vo;

import lombok.Data;

/**
 * @Description:
 * @Author Administrator
 * @Date 2022/5/7
 **/
@Data
public class FileVO {
    /**
     * 对象名称
     */
    private String objectName;

    /**
     * 上传时间
     */
    private String uploadTime;

    /**
     * 地址
     */
    private String url;
}
