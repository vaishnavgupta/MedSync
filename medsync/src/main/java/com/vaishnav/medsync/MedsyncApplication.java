package com.vaishnav.medsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedsyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedsyncApplication.class, args);

		System.out.println("USER=" + System.getenv("MAIL_USERNAME"));
		System.out.println("PASS=" + System.getenv("MAIL_PASSWORD"));

	}

}
