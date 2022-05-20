package top.cattycat.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author 王金义
 */
@SpringBootApplication(scanBasePackages = "top.cattycat")
@MapperScan("top.cattycat.mapper.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@EntityScan(basePackages = "top.cattycat.common")
@ConfigurationPropertiesScan("top.cattycat.common.config")
public class ControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }

}
