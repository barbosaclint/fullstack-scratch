package com.kurinto.customer;

import com.kurinto.exception.DuplicateResourceException;
import com.kurinto.exception.RequestValidationException;
import com.kurinto.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  CustomerService {

    private final CustomerDao customerDao;
    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
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

    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest){

        Customer customer = getCustomerById(customerId);
        boolean gotChanges = false;

        if (customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
            customer.setName(customerUpdateRequest.name());
            gotChanges = true;
        }

        if (customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
            customer.setAge(customerUpdateRequest.age());
            gotChanges = true;
        }

        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException(
                        "email already exist"
                );
            }
            customer.setEmail(customerUpdateRequest.email());
            gotChanges = true;
        }

        if (!gotChanges) {
            throw new RequestValidationException(
                    "No changes found"
            );
        }

        customerDao.updateCustomer(customer);
    }
}
