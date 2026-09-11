package com.lgsoftworks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FitoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitoraApplication.class, args);
	}

}
