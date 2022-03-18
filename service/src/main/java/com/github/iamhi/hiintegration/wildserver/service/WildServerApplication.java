package com.github.iamhi.hiintegration.wildserver.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.github.iamhi.hiintegration.wildserver"})
@ConfigurationPropertiesScan("com.github.iamhi.hiintegration.wildserver.service.config")
public class WildServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WildServerApplication.class, args);
	}

}
