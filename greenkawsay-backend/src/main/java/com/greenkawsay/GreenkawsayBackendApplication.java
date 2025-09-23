package com.greenkawsay;

import com.greenkawsay.infrastructure.persistence.RedisTest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
public class GreenkawsayBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenkawsayBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner testRedis(RedisTest redisTest) {
        return args -> redisTest.testConnection();
    }

}
