package com.sms.crawler.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@SpringBootApplication
public class ParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParserApplication.class, args);
	}

}
