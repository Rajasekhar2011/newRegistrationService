package com.spring.reactive.registration.handler;

import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.exception.UserNotFoundException;
import com.spring.reactive.registration.repository.RegistrationRepository;
import com.spring.reactive.registration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class RegistrationHandler {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RegistrationRepository registrationRepository;

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {
        Mono<Customer> newCustomer =serverRequest.bodyToMono(Customer.class);

        return newCustomer.flatMap( cust -> ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .body(registrationService.registerCustomer(cust), Customer.class));
    }


/*    public Mono<ServerResponse> customerLogin(ServerRequest serverRequest) {
        Mono<Customer> existingCustomer =serverRequest.bodyToMono(Customer.class);

        return existingCustomer.flatMap( existCust -> ServerResponse.ok()
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .body(registrationService.login(existCust.getUserId(),existCust.getPassword()), Customer.class)
                                                    .switchIfEmpty(notFound));
    }*/

    public Mono<ServerResponse> customerLogin(ServerRequest serverRequest) {
        Mono<Customer> existingCustomer =serverRequest.bodyToMono(Customer.class);
        Mono<Customer> checkCustomer = existingCustomer.flatMap(ec-> registrationService.login(ec.getUserId(),ec.getPassword()));
        return checkCustomer.flatMap(cc-> {
            if(cc!=null) {
                System.out.println(cc);
                return ServerResponse.ok().body(cc, Customer.class);
            }else{
                System.out.println("I am hitted########################################");
                throw  new UserNotFoundException(HttpStatus.NOT_FOUND,"No user found")  ;
            }
        });
    }


    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("userId");
        Mono<Customer> updatedCustomer = serverRequest.bodyToMono(Customer.class)
                                        .flatMap( updateCustomer -> {
                                            Mono<Customer> existingUser = registrationService.getCustomerById(id)
                                             .flatMap(currentCustomer -> {
                                                 currentCustomer.setAddress(updateCustomer.getAddress());
                                                 currentCustomer.setEmail(updateCustomer.getEmail());
                                                 currentCustomer.setContactNumber(updateCustomer.getContactNumber());
                                                 return registrationRepository.save(currentCustomer);
                                             });
                                return existingUser;
                                });

        return updatedCustomer.flatMap( updated-> ServerResponse.ok()
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .body(fromObject(updated))
                                                    .switchIfEmpty(notFound));
    }
}
