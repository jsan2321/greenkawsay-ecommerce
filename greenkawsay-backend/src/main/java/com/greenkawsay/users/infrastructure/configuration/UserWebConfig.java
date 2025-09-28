package com.greenkawsay.users.infrastructure.configuration;

import com.greenkawsay.users.application.ports.in.UserProfileServicePort;
import com.greenkawsay.users.infrastructure.adapters.in.web.controllers.UserProfileController;
import com.greenkawsay.users.infrastructure.adapters.in.web.mappers.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for User Context web layer
 */
@Configuration
public class UserWebConfig {

    @Bean
    public UserProfileController userProfileController(
            UserProfileServicePort userProfileService,
            UserMapper userMapper) {
        return new UserProfileController(userProfileService, userMapper);
    }
}