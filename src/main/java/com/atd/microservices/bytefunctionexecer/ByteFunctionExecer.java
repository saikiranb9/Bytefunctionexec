package com.atd.microservices.bytefunctionexecer;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;


@SpringBootApplication
@EnableSwagger2
public class ByteFunctionExecer {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${env.host.url:#{null}}")
    private String envHostURL;

    public static void main(String[] args) {

        System.out.println(new File("generated").mkdirs());

        SpringApplication.run(ByteFunctionExecer.class, args);
        
    }
    
	@Bean
	public WebClient getWebClientBuilder(){
		return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer
								.defaultCodecs()
								.maxInMemorySize(20 * 1024 * 1024))
						.build())
				.build();
	}
}
