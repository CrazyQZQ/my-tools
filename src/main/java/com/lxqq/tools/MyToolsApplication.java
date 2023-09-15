package com.lxqq.tools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author QinQiang
 * @date 2023/9/13
 **/
@SpringBootApplication
@MapperScan("com.lxqq.tools.system.mapper")
public class MyToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyToolsApplication.class, args);

    }

}
