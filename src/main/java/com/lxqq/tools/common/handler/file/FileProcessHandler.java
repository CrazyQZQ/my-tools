package com.lxqq.tools.common.handler.file;

import com.lxqq.tools.common.handler.file.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @Description:
 * @Author QinQiang
 * @Date 2023/9/13
 **/
public interface FileProcessHandler {
    /**
     * 上传文件到存储系统。
     *
     * @param file 要上传的文件
     * @return 生成的文件名
     */
    String upload(MultipartFile file, String subPath);

    String upload(File file, String subPath, String fileName);

    /**
     * 从存储系统下载文件。
     *
     * @param fileName   要下载的文件名
     * @param objectName 存储系统中的对象名
     * @param response   HttpServletResponse对象，用于将文件内容写入其中
     */
    void download(String fileName, String objectName, HttpServletResponse response);

    /**
     * 从存储系统中删除多个文件。
     *
     * @param objectNames 要删除的对象名列表
     */
    void deleteFile(List<String> objectNames);

    /**
     * 根据完整路径删除存储系统中的多个文件。
     *
     * @param urls 要删除的文件的完整路径列表
     */
    void deleteFileByFullPath(List<String> urls);

    /**
     * 从存储系统中获取文件信息列表。
     *
     * @return 表示文件的FileVO对象列表
     */
    List<FileVO> listFiles();
}
