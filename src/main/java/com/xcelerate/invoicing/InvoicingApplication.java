package com.xcelerate.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class InvoicingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicingApplication.class, args);
	}

}
