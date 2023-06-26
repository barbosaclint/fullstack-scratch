package com.kurinto.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
        List<Customer> selectAllCustomers();
        Optional<Customer> selectCustomerById(Integer customerId);
        void insertCustomer(Customer customer);
        boolean existsPersonWithEmail(String email);
        boolean existsCustomersById(Integer customerId);
        void deleteCustomer(Integer customerId);
        void updateCustomer(Customer customer);
}
