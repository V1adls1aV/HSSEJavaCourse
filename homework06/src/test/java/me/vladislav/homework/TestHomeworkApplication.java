package me.vladislav.homework;

import org.springframework.boot.SpringApplication;

public class TestHomeworkApplication {

  public static void main(String[] args) {
    SpringApplication.from(HomeworkApplication::main).run(args);
  }

}
