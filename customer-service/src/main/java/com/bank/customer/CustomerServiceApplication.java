package com.bank.customer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
	@Bean
	public OpenAPI swaggerHeader() {
		return new OpenAPI()
				.info((new Info())
						.description("API documentation for Customer Service")
						.title(StringUtils.substringBefore(getClass().getSimpleName(), "$"))
						.version("v1"));
	}

}
