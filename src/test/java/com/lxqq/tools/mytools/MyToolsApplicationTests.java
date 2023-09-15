package com.lxqq.tools.mytools;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.lxqq.tools.pdf.service.PdfTransferService;
import com.lxqq.tools.system.mapper.SysUserMapper;
import com.lxqq.tools.system.pojo.SysUser;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.lxqq.tools.system.pojo.table.SysRoleTableDef.SYS_ROLE;
import static com.lxqq.tools.system.pojo.table.SysUserRoleTableDef.SYS_USER_ROLE;
import static com.lxqq.tools.system.pojo.table.SysUserTableDef.SYS_USER;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MyToolsApplicationTests {

    @Autowired
    private PdfTransferService pdfTransferService;

    @Test
    void contextLoads() throws IOException {
        String s = pdfTransferService.imgToPdf(Files.newInputStream(new File("E:\\qqWorkspace\\upload images\\banner5.jpg").toPath()));
        System.out.println(s);
    }

    @Test
    void test1() throws IOException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
//            {"url":"http://127.0.0.1:9000","accessKey":"utSiUL7UEobU5pHBbdNf","secretKey":"U5GoQYizVhWpU0WLmQWEQClFNCpuuRzurateXpDS","api":"s3v4","path":"auto"}
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://117.50.187.26:9000")
                    .credentials("utSiUL7UEobU5pHBbdNf", "U5GoQYizVhWpU0WLmQWEQClFNCpuuRzurateXpDS")
                    .build();

            // Make 'asiatrip' bucket if not exist.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("qq-qqq").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("qq-qqq").build());
            } else {
                System.out.println("Bucket 'qq-qqq' already exists.");
            }

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("qq-qqq")
                            .object("cf.png")
                            .filename("E:/qqWorkspace/upload images/cf.png")
                            .build());
            System.out.println("successfully");
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e);
        }
    }
    @Autowired
    private SysUserMapper userMapper;

    @Test
    void testDb() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(SYS_USER.ID, SYS_USER.USERNAME, SYS_ROLE.ALL_COLUMNS)
                .from(SYS_USER)
                .leftJoin(SYS_USER_ROLE).on(SYS_USER_ROLE.USER_ID.eq(SYS_USER.ID))
                .leftJoin(SYS_ROLE).on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ID));
        List<SysUser> users = userMapper.selectListByQueryAs(queryWrapper, SysUser.class);
        System.out.println(JSON.toJSONString(users));

    }

    @Test
    void testDb1() {
        QueryCondition queryCondition = QueryCondition.create(SYS_USER.USERNAME, "qq");
        SysUser sysUser = userMapper.selectOneByCondition(queryCondition);
        System.out.println(JSON.toJSONString(sysUser));
        AES aes = SecureUtil.aes();
        String encryptHex = aes.encryptHex(sysUser.getPassword());
//        ee7ebd1eb0c9169d9cabdad0cdefa444
        System.out.println(encryptHex);
        System.out.println(aes.decryptStr(encryptHex));
    }

    public static void main(String[] args) {
        AES aes = SecureUtil.aes();
        System.out.println(SecureUtil.md5("qq123"));
        System.out.println(SecureUtil.md5("admin123"));
//        System.out.println(aes.encryptHex("qq123"));
//        System.out.println(aes.encryptHex("admin123"));
    }

}
