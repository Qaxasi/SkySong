package com.mycompany.skysong.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.mycompany.skysong.app.config")
public class SkySongApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkySongApplication.class, args);
	}
}
