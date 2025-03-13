package com.green.acamatch;

import com.green.acamatch.config.GlobalOauth2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableWebSocketMessageBroker
//@EnableConfigurationProperties(GlobalOauth2.class)
public class AcaMatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcaMatchApplication.class, args);
    }
}
