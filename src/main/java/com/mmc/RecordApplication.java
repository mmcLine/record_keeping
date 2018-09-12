package com.mmc;

import com.mmc.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class RecordApplication {
    public static void main(String[] args) {
        ApplicationContext context=SpringApplication.run(RecordApplication.class, args);
        SpringContextUtil.setApplicationContext(context);
    }
}
