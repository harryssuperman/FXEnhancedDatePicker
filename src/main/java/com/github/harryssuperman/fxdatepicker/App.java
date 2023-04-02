package com.github.harryssuperman.fxdatepicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {
    @Autowired
    static Environment env;

    public static void main(String[] args) {

        System.out.println("f" + env.getActiveProfiles().toString());
    }

}
