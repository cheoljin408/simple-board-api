package com.example.simpleboardapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class SimpleBoardApiApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SimpleBoardApiApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

}
