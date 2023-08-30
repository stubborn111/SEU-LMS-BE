package edu.seu.lms.backend.seulmsbe;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("edu.seu.lms.backend.seulmsbe.*.mapper")
public class SeuLmsBeApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SeuLmsBeApplication.class, args);
    }

}
