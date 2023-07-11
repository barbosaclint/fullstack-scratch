package com.kurinto.customer;

import com.kurinto.BaseTestContainersUnitTest;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.bouncycastle.asn1.x500.style.RFC4519Style.c;

class CustomerJDBCDataAccessServiceTest extends BaseTestContainersUnitTest {

    private CustomerJDBCDataAccessService underTest;
    private CustomerRowMapper customerRowMapper= new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        // When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying( c -> {
           assertThat(c.getId()).isEqualTo(id);
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAge()).isEqualTo(customer.getAge());

        });
    }

    @Test
    void selectCustomerIDEmptyTest() {
        // Given
        int id = 3123;

        // When
        var actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                20
        );
        underTest.insertCustomer(customer);

        // When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);
        int id  = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsPersonWithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithEmailFalse() {
        // Given
        String email = "nlablabl@gmail.com";

        // When
        var actual = underTest.existsPersonWithEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomersById() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        var id = underTest.selectAllCustomers().stream()
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();

        // When
        var actual = underTest.existsCustomersById(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existCustomerByIdFalse() {
        // Given
        int id = 1331231;

        // When
        var actual = underTest.existsCustomersById(id);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void notExistCustomerId() {
        // Given
        int id = 31231;

        // When
        var actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void deleteCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                22
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();

        // When
        underTest.deleteCustomer(id);

        //Then
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                25
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();

        Customer cu = underTest.selectAllCustomers().stream()
                .findFirst()
                .orElseThrow();
        cu.setName("sssss");
        cu.setEmail(FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID());
        cu.setAge(11);
        cu.setId(id);
        underTest.updateCustomer(cu);

        // When
        var actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isNotEmpty().hasValueSatisfying(c -> {
            assertThat(c.getName()).isNotEqualTo(customer.getName());
            assertThat(c.getEmail()).isNotEqualTo(customer.getEmail());
            assertThat(c.getAge()).isNotEqualTo(customer.getAge());
        });
    }
}