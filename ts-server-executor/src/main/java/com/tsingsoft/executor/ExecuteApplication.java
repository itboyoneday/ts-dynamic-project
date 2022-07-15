package com.tsingsoft.executor;

import com.tsingsoft.executor.config.PluginImportBeanDefinitionRegistrar;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author bask
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.tsingsoft"})
@MapperScan(basePackages = {"com.tsingsoft.*.mapper","com.baomidou.mybatisplus.core.mapper"})
@Import(value = {PluginImportBeanDefinitionRegistrar.class})
public class ExecuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExecuteApplication.class, args);
    }
}
