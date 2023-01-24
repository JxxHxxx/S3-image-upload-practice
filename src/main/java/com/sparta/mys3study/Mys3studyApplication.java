package com.sparta.mys3study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Mys3studyApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mys3studyApplication.class, args);
    }

}
