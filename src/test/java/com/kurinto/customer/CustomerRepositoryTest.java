package com.kurinto.customer;

import com.kurinto.BaseTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends BaseTestContainersUnitTest {

    @Autowired
    private CustomerRepository  underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomersByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.save(customer);
        String newEmail = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getEmail)
                .findFirst()
                .orElseThrow();

        // When
        boolean actual = underTest.existsCustomersByEmail(newEmail);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void notExistsCustomersByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsCustomersByEmail(email);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomersById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.save(customer);
        int id = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        boolean actual = underTest.existsCustomersById(id);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void notExistsCustomersById() {
        // Given
        int id = -1;

        // When
        boolean actual = underTest.existsCustomersById(id);
        //Then
        assertThat(actual).isFalse();
    }
}