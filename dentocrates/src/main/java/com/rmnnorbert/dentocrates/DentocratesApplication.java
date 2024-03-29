package com.rmnnorbert.dentocrates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DentocratesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DentocratesApplication.class, args);
	}

}
