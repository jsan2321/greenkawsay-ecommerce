package com.greenkawsay.users.domain.valueobjects;

import java.util.Arrays;
import java.util.Objects;

/**
 * Value object representing user roles with validation
 * Immutable and thread-safe
 */
public final class Role {
    public enum RoleType {
        CUSTOMER("customer"),
        VENDOR("vendor"),
        ADMIN("admin");
        
        private final String value;
        
        RoleType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static RoleType fromString(String value) {
            return Arrays.stream(values())
                    .filter(role -> role.value.equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + value));
        }
    }
    
    private final RoleType value;
    
    public Role(RoleType value) {
        this.value = Objects.requireNonNull(value, "Role cannot be null");
    }
    
    public Role(String value) {
        this(RoleType.fromString(value));
    }
    
    public RoleType getValue() {
        return value;
    }
    
    public String getValueAsString() {
        return value.getValue();
    }
    
    public boolean isCustomer() {
        return value == RoleType.CUSTOMER;
    }
    
    public boolean isVendor() {
        return value == RoleType.VENDOR;
    }
    
    public boolean isAdmin() {
        return value == RoleType.ADMIN;
    }
    
    public boolean hasPermission(Role requiredRole) {
        if (isAdmin()) return true;
        if (isVendor()) return requiredRole.isVendor() || requiredRole.isCustomer();
        return requiredRole.isCustomer();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return value == role.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value.getValue();
    }
}