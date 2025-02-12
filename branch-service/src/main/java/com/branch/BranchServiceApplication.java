package com.branch;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.branch")
public class BranchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BranchServiceApplication.class, args);
	}

	@Bean
	public OpenAPI swaggerHeader() {
		return new OpenAPI()
				.info((new Info())
						.description("API documentation for Branch Service")
						.title(StringUtils.substringBefore(getClass().getSimpleName(), "$"))
						.version("v1"));
	}

}
