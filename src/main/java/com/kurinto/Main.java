package com.kurinto;

import com.github.javafaker.Faker;
import com.kurinto.customer.Customer;
import com.kurinto.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner (CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            Customer customer = new Customer(
                    faker.name().name(),
                    faker.internet().safeEmailAddress(),
                    random.nextInt(10,90)
            );
            customerRepository.save(customer);
        };

    }
}
