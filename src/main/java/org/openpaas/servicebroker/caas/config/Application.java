package org.openpaas.servicebroker.caas.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Kubernetes broker entry point.
 * @author Hyerin
 * @since 2018.07.24
 * @version 20180724
 */
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
        
}