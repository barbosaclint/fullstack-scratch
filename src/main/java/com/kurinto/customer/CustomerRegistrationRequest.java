package com.kurinto.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
