package com.kurinto.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {

        String sql = """
                SELECT id, name, email, age 
                    FROM customer
                """;
        RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
            Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getInt("age")
            );
            return customer;
        };

        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);

        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return Optional.empty();
    }

    @Override
    public void insertCustomer(Customer customer) {
        String sql = """
                INSERT INTO customer (name, email, age)
                VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge());
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return false;
    }

    @Override
    public boolean existsCustomersById(Integer customerId) {
        return false;
    }

    @Override
    public void deleteCustomer(Integer customerId) {

    }

    @Override
    public void updateCustomer(Customer customer) {

    }
}
