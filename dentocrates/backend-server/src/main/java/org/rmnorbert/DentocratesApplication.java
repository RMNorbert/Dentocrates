package org.rmnorbert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DentocratesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DentocratesApplication.class, args);
	}

}
