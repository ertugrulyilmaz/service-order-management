package com.eriks.service.order.management.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackages = {"com.eriks.service.order.management.test"})
@SpringBootApplication
public class ApplicationTest {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationTest.class, args);
  }

}
