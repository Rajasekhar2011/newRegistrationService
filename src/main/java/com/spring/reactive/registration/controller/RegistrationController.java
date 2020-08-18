package com.spring.reactive.registration.controller;

import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.dto.LoginDetails;
import com.spring.reactive.registration.repository.RegistrationRepository;
import com.spring.reactive.registration.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;


@RestController
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RegistrationRepository registrationRepository;

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @PostMapping("/v1/register")
    public Mono<ResponseEntity<Customer>> createRegister(@RequestBody Customer customer){
        logger.info("Taking customer object and assigning to pojo"+customer);
        customer.setUserName(customer.getFirstName().concat(customer.getLastName()));

        logger.info("username is extracted from firstname and lastname and username is "+customer.getUserName());
         return registrationService.registerCustomer(customer)
                 .map( customer1-> new ResponseEntity<>(customer1,HttpStatus.CREATED));
    }

   @PostMapping("/v1/login")
    public Mono<ResponseEntity<String>> loginCustomer(@RequestBody LoginDetails loginDetails){
        logger.info("Request to login to application made by user with id"+loginDetails.getUserId());
        return registrationService.login(loginDetails.getUserId(),loginDetails.getPassword())
                .map( customer2->  new ResponseEntity<>("Logged in successfully",HttpStatus.FOUND))
                .defaultIfEmpty(new ResponseEntity<>("No user Found",HttpStatus.NOT_FOUND));
    }



    @PutMapping("/v1/login/{userId}")
    public Mono<ResponseEntity<Customer>> updateItem(@PathVariable String userId,@RequestBody Customer customer){
        logger.info("Request to update customer details by customer with id "+userId);
        return registrationService.getCustomerById(userId)
                .flatMap(existingCustomer ->{
                    existingCustomer.setAddress(customer.getAddress());
                    existingCustomer.setContactNumber(customer.getContactNumber());
                    existingCustomer.setEmail(customer.getEmail());
                    return registrationRepository.save(existingCustomer);
                })
                .map(updatedCustomer ->  new ResponseEntity<>(updatedCustomer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
