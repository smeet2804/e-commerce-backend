package com.example.paymentservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PaymentServiceApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("STRIPE_API_KEY", dotenv.get("STRIPE_API_KEY"));
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}
