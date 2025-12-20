package com.fujian.heritage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fujian.heritage.mapper") // 【关键】扫描 Mapper 包
public class HeritageApplication {
    public static void main(String[] args) {
        SpringApplication.run(HeritageApplication.class, args);
    }
}
