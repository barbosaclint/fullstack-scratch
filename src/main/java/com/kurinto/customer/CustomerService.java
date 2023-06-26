package com.kurinto.customer;

import com.kurinto.exception.DuplicateResourceException;
import com.kurinto.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  CustomerService {

    private final CustomerDao customerDao;
    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer customerId){
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "No customer id with id [%s]".formatted(customerId)
            ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){

        String email = customerRegistrationRequest.email();

        if (customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException(
                    "email already exist"
            );
        }

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );

        customerDao.insertCustomer(customer);
    }

    public void deleteCustomer(Integer customerId){
        if (!customerDao.existsCustomersById(customerId)){
            throw new ResourceNotFoundException(
                    "No customer found with id [%n] ".formatted(customerId)
            );
        }

        customerDao.deleteCustomer(customerId);
    }
}
