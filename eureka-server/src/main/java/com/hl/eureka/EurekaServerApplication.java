package com.hl.eureka;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication{

    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaServerApplication.class)
                    .web(WebApplicationType.REACTIVE).run(args);
    }
}