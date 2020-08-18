package com.spring.reactive.registration.service;

import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    public Mono<Customer> registerCustomer(Customer customer){
        return registrationRepository.save(customer);
    }

    public Mono<Customer> login(String userId, String password){
        return registrationRepository.findByUserIdAndPassword(userId,password);
    }

    public Mono<Customer> getCustomerById(String userId){
        return registrationRepository.findById(userId);
    }

}
