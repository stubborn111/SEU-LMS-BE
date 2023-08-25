package edu.seu.lms.backend.seulmsbe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.seu.lms.backend.seulmsbe.*.mapper")
public class SeuLmsBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeuLmsBeApplication.class, args);
    }

}
