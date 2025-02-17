package me.vladislav.homework02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Homework02Application {

  public static void main(String[] args) {
    SpringApplication.run(Homework02Application.class, args);
  }

}

