package com.greenkawsay.users.infrastructure.adapters.in.web.mappers;

import com.greenkawsay.users.application.commands.CreateUserProfileCommand;
import com.greenkawsay.users.application.commands.UpdateUserProfileCommand;
import com.greenkawsay.users.domain.models.UserProfile;
import com.greenkawsay.shared.domain.valueobjects.Email;
import com.greenkawsay.users.domain.valueobjects.ImpactScore;
import com.greenkawsay.users.domain.valueobjects.Role;
import com.greenkawsay.users.domain.valueobjects.UserId;
import com.greenkawsay.users.infrastructure.adapters.in.web.dto.request.CreateUserProfileRequest;
import com.greenkawsay.users.infrastructure.adapters.in.web.dto.request.UpdateUserProfileRequest;
import com.greenkawsay.users.infrastructure.adapters.in.web.dto.response.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

/**
 * MapStruct mapper for User operations in web layer
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", source = "keycloakId")
    @Mapping(target = "email", source = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    @Mapping(target = "impactScoreTotal", source = "impactScoreTotal", qualifiedByName = "intToImpactScore")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "wishlists", ignore = true)
    UserProfile toDomain(CreateUserProfileRequest request);

    @Mapping(target = "id", source = "userId", qualifiedByName = "stringToUserId")
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "avatarUrl", source = "request.avatarUrl")
    @Mapping(target = "role", source = "request.role", qualifiedByName = "stringToRole")
    @Mapping(target = "impactScoreTotal", source = "request.impactScoreTotal", qualifiedByName = "intToImpactScore")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "wishlists", ignore = true)
    UserProfile toDomain(String userId, UpdateUserProfileRequest request);

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "keycloakId", source = "keycloakId")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "role", source = "role.value")
    @Mapping(target = "impactScoreTotal", source = "impactScoreTotal.value")
    @Mapping(target = "active", expression = "java(userProfile.isActive())")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "wishlists", ignore = true)
    UserProfileResponse toResponse(UserProfile userProfile);

    @Named("stringToRole")
    default Role stringToRole(String role) {
        if (role == null) {
            return null;
        }
        return new Role(role);
    }

    @Named("stringToEmail")
    default Email stringToEmail(String email) {
        if (email == null) {
            return null;
        }
        return new Email(email);
    }

    @Named("intToImpactScore")
    default ImpactScore intToImpactScore(Integer impactScore) {
        if (impactScore == null) {
            return null;
        }
        return new ImpactScore(impactScore);
    }

    @Named("stringToUserId")
    default UserId stringToUserId(String userId) {
        if (userId == null) {
            return null;
        }
        return new UserId(UUID.fromString(userId));
    }
}