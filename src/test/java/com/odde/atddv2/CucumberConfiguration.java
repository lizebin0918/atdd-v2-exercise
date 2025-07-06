package com.odde.atddv2;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@SpringBootApplication
public class CucumberConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

