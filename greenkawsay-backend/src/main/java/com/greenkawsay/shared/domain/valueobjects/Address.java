package com.greenkawsay.shared.domain.valueobjects;

import java.util.Objects;

/**
 * Value object representing a physical address
 * Immutable and thread-safe
 */
public final class Address {
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;

    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = Objects.requireNonNull(street, "Street cannot be null").trim();
        this.city = Objects.requireNonNull(city, "City cannot be null").trim();
        this.state = state != null ? state.trim() : null;
        this.zipCode = zipCode != null ? zipCode.trim() : null;
        this.country = Objects.requireNonNull(country, "Country cannot be null").trim();
        validate();
    }

    public Address(String street, String city, String country) {
        this(street, city, null, null, country);
    }

    private void validate() {
        if (street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
        if (city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        if (country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
        if (street.length() > 255) {
            throw new IllegalArgumentException("Street cannot exceed 255 characters");
        }
        if (city.length() > 100) {
            throw new IllegalArgumentException("City cannot exceed 100 characters");
        }
        if (state != null && state.length() > 100) {
            throw new IllegalArgumentException("State cannot exceed 100 characters");
        }
        if (zipCode != null && zipCode.length() > 20) {
            throw new IllegalArgumentException("Zip code cannot exceed 20 characters");
        }
        if (country.length() > 100) {
            throw new IllegalArgumentException("Country cannot exceed 100 characters");
        }
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public boolean isInCountry(String countryCode) {
        return this.country.equalsIgnoreCase(countryCode);
    }

    public boolean hasState() {
        return state != null && !state.isBlank();
    }

    public boolean hasZipCode() {
        return zipCode != null && !zipCode.isBlank();
    }

    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append(street);
        
        if (hasState() || hasZipCode() || !city.isBlank()) {
            sb.append(", ").append(city);
        }
        
        if (hasState()) {
            sb.append(", ").append(state);
        }
        
        if (hasZipCode()) {
            sb.append(" ").append(zipCode);
        }
        
        if (!country.isBlank()) {
            sb.append(", ").append(country);
        }
        
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(city, address.city) &&
                Objects.equals(state, address.state) &&
                Objects.equals(zipCode, address.zipCode) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zipCode, country);
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}