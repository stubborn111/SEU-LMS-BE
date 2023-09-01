package edu.seu.lms.backend.seulmsbe.config;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AliyunOSSConfig {
    private String endPoint="oss-cn-nanjing.aliyuncs.com";// 地域节点
    private String accessKeyId="LTAI5tJRk2xwCfEvQ4dEoEuE";
    private String accessKeySecret="28NCmZ8hE1GvgyQ28pYzk0TAjNJ0eY";
    private String bucketName="seu-lms";// OSS的Bucket名称
    private String urlPrefix="seu-lms.oss-cn-nanjing.aliyuncs.com";// Bucket 域名
    private String fileHost="images";// 目标文件夹
    @Bean
    public OSS OSSClient(){
        return new OSSClient(endPoint,accessKeyId,accessKeySecret);
    }
}

