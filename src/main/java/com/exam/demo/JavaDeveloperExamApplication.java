package com.exam.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.demo.controller.AppController;

@SpringBootApplication
@RestController
@EnableScheduling
public class JavaDeveloperExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaDeveloperExamApplication.class, args);
	}

	@GetMapping("/player")
	public String start() throws Exception {
		AppController.run();
		return String.format("CSV file was successfully created!");
	}

}
