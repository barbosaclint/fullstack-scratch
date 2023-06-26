package com.kurinto.customer;

import com.kurinto.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer customerId){
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFound(
                    "No customer id with id [%s]".formatted(customerId)
            ));
    }
}
