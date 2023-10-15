package com.silmarfnascimento.CEPSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CepSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CepSystemApplication.class, args);
	}

}
