package com.mycompany.SkySong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SkySongApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkySongApplication.class, args);
	}
}
