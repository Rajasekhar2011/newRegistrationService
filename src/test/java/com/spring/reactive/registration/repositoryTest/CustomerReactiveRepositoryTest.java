package com.spring.reactive.registration.repositoryTest;

import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class CustomerReactiveRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    List<Customer> customerList = Arrays.asList(
            new Customer("1","Nov20@1994","Raja","Sekhar","Rajasekhar","Nandyal","AP","India","rajasekhar@gmail.com","BKOPRJ978K",8499909196L,"1994-11-20","Savings"),
            new Customer("2","Apr20@1995","Sushmita","Reddy","Sush","Hyderabad","Telangana","India","sushmita@gmail.com","BKOPRJ428K",9785462183l,"1995-06-20","Current"),
            new Customer("3","Jun21@1994","Sneha","Verma","SnehaVerma","Bhimavaram","AP","India","sneha@gmail.com","APOPRJ092K",7605322440L,"1995-01-20","Savings")
    );

    @BeforeEach
    public void setUpData(){
        registrationRepository.deleteAll()
                .thenMany(Flux.fromIterable(customerList))
                .flatMap(registrationRepository::save)
                .doOnNext((item->{
                    System.out.println("inserted item is "+item);
                }))
                .blockLast();
    }

    @Test
    public void saveitem(){
        Customer newCustomer = new Customer("4","Aug3@1992","Chandra","Sekhar","Chandrasekhar","Kanala","AP","India","chandrasekhar@gmail.com","CJOPRJ105K",9505957693L,"1996-10-25","Loan");
        Mono<Customer> insertedCustomer=registrationRepository.save(newCustomer);

        StepVerifier.create(insertedCustomer)
                .expectSubscription()
                .expectNextMatches(customer->customer.getFirstName().equalsIgnoreCase("Chandra") && customer.getUserId()!=null)
                .verifyComplete();
    }

    @Test
    public void findCustomerByUserIdAndPassword(){
        StepVerifier.create(registrationRepository.findByUserIdAndPassword("1","Nov20@1994"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void updateCustomer(){
        Mono<Customer> updatedCustomer =  registrationRepository.findByUserIdAndPassword("1","Nov20@1994")
                .map(customer->{
                    customer.setContactNumber(7675020618L);
                    return customer;
                })
                .flatMap(customer1->{
                    return registrationRepository.save(customer1);
                });
        StepVerifier.create(updatedCustomer)
                .expectSubscription()
                .expectNextMatches(customer -> customer.getContactNumber()==7675020618L)
                .verifyComplete();
    }

}
