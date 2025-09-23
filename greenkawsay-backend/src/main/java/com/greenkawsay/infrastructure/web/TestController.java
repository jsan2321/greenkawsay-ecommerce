package com.greenkawsay.infrastructure.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint!";
    }

    @GetMapping("/authenticated")
    public String authenticatedEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return "Hello " + jwt.getClaimAsString("email") + "! You are authenticated.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Only admins can see this!";
    }

    @GetMapping("/vendor")
    @PreAuthorize("hasRole('VENDOR')")
    public String vendorEndpoint() {
        return "Hello seller!";
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerEndpoint() {
        return "Hello customer!";
    }

    @GetMapping("/admin-or-vendor")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    public String adminOrVendorEndpoint() {
        return "Hello admin or seller!";
    }

    @GetMapping("/user-info")
    public String userInfoEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return String.format(
                "User ID: %s, Email: %s, Roles: %s",
                jwt.getClaimAsString("user_id"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsStringList("roles")
        );
    }
}