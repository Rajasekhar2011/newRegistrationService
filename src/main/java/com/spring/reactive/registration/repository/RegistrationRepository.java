package com.spring.reactive.registration.repository;

import com.spring.reactive.registration.document.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RegistrationRepository extends ReactiveMongoRepository<Customer,String> {
        Mono<Customer> findByUserIdAndPassword(String userId, String password);
}
