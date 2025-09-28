package com.greenkawsay.users.infrastructure.configuration;

import com.greenkawsay.users.application.ports.in.UserProfileServicePort;
import com.greenkawsay.users.application.services.UserProfileApplicationService;
import com.greenkawsay.users.domain.repositories.UserProfileRepository;
import com.greenkawsay.users.infrastructure.adapters.out.persistence.UserProfilePersistenceAdapter;
import com.greenkawsay.users.infrastructure.adapters.out.persistence.mappers.UserProfileMapper;
import com.greenkawsay.users.infrastructure.adapters.out.persistence.repositories.UserProfileJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for User Context services
 */
@Configuration
public class UserServiceConfig {

    @Bean
    public UserProfileServicePort userProfileService(
            UserProfileRepository userProfileRepository) {
        return new UserProfileApplicationService(userProfileRepository);
    }

    @Bean
    public UserProfileRepository userProfileRepository(
            UserProfileJpaRepository userProfileJpaRepository,
            UserProfileMapper userProfileMapper) {
        return new UserProfilePersistenceAdapter(userProfileJpaRepository, userProfileMapper);
    }
}