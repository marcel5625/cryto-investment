package com.marcel.crypto.investment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

import static java.util.TimeZone.getTimeZone;

@SpringBootApplication
public class InvestmentApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(getTimeZone("GMT"));
		SpringApplication.run(InvestmentApplication.class, args);
	}
}