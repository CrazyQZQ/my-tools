package com.lxqq.tools.common.handler.file.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.lxqq.tools.common.handler.file.FileProcessHandler;
import com.lxqq.tools.common.handler.file.vo.FileVO;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QinQiang
 * @date 2023/9/13
 **/
@Slf4j
@Component
public class MinIoFileProcessHandler implements FileProcessHandler {
    private MinioClient minioClient;


    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    public MinioClient getMinioClient() {
        if (minioClient == null) {
//            minioClient = MinioClient.builder()
//                    .endpoint(endpoint)
//                    .credentials(accessKey, secretKey)
//                    .build();
            minioClient = MinioClient.builder()
                    .endpoint("http://117.50.187.26:9000")
                    .credentials("utSiUL7UEobU5pHBbdNf", "U5GoQYizVhWpU0WLmQWEQClFNCpuuRzurateXpDS")
                    .build();
        }
        return minioClient;
    }

    @Override
    public String upload(MultipartFile file, String subPath) {
        String uploadPath = null;
        try {
            uploadPath = upload(file.getInputStream(), subPath, file.getOriginalFilename());
        } catch (IOException e) {
            log.error("上传文件出错: ", e);
        }
        return uploadPath;
    }

    @Override
    public String upload(File file, String subPath, String fileName) {
        String uploadPath = null;
        try {
            fileName = StrUtil.isEmpty(fileName) ? file.getName() : fileName;
            uploadPath = upload(Files.newInputStream(file.toPath()), subPath, fileName);
        } catch (IOException e) {
            log.error("上传文件出错: ", e);
        }
        return uploadPath;
    }

    private String upload(InputStream inputStream, String subPath, String fileName) {
        try {
            //创建一个MinIO的Java客户端
            MinioClient minioClient = getMinioClient();
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketExistsArgs);
            if (isExist) {
                log.info("存储桶已经存在！");
            } else {
                //创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            // 设置存储对象名称
            String objectName = subPath+ "/" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd") + "/" + fileName;
            // 使用putObject上传一个文件到存储桶中
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            log.info("文件上传成功!");
            return endpoint + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            log.info("上传发生错误: ", e);
        }
        return null;
    }

    @Override
    public void download(String fileName, String objectName, HttpServletResponse response) {
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            // 设置response的Header
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            OutputStream os = response.getOutputStream();
            MinioClient minioClient = getMinioClient();
            // 获取文件输入流
            inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[2048];
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        } catch (Exception e) {
            log.error("下载文件发生错误: ", e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    log.error("下载文件发生错误: ", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("下载文件发生错误: ", e);
                }
            }
        }
    }

    @Override
    public void deleteFile(List<String> objectNames) {
        try {
            MinioClient minioClient = getMinioClient();
            String prefix = endpoint + "/" + bucketName + "/";
            List<DeleteObject> handledObjectNames = new ArrayList<>(objectNames.size());
            for (String objectName : objectNames) {
                if (objectName.startsWith(prefix)) {
                    objectName = objectName.substring(prefix.length());
                }
                handledObjectNames.add(new DeleteObject(objectName));
            }
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(handledObjectNames).build());
            for (Result<DeleteError> result : results) {
                DeleteError deleteError = result.get();
                log.debug(deleteError.message());
            }
        } catch (Exception e) {
            log.error("删除文件发生错误: ", e);
        }
    }

    @Override
    public void deleteFileByFullPath(List<String> urls) {
        if (CollUtil.isEmpty(urls)) {
            return;
        }
        String prefix = endpoint + "/" + bucketName + "/";
        List<String> objectNames = urls.stream().map(url -> url.substring(prefix.length())).collect(Collectors.toList());
        deleteFile(objectNames);
    }

    @Override
    public List<FileVO> listFiles() {
        List<FileVO> fileVOS = new ArrayList<>();
        try {
            MinioClient minioClient = getMinioClient();
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                ZonedDateTime zonedDateTime = item.lastModified().withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
                FileVO fileVO = new FileVO();
                fileVO.setObjectName(item.objectName());
                fileVO.setUploadTime(zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                fileVO.setUrl(endpoint + "/" + bucketName + "/" + item.objectName());
                fileVOS.add(fileVO);
            }
        } catch (Exception e) {
            log.error("查询文件列表发生错误: ", e);
        }
        return fileVOS;
    }
}
