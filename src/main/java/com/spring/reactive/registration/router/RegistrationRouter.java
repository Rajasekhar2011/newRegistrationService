package com.spring.reactive.registration.router;

import com.spring.reactive.registration.handler.RegistrationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RegistrationRouter {

    @Bean
    public RouterFunction<ServerResponse> registrationsRouter(RegistrationHandler regHandler){

        return RouterFunctions
                .route(POST("/v1/functional/register").and(accept(MediaType.APPLICATION_JSON)),regHandler::createCustomer)
                .andRoute(POST("/v1/functional/login").and(accept(MediaType.APPLICATION_JSON)),regHandler::customerLogin)
                .andRoute(PUT("/v1/functional/register/{userId}").and(accept(MediaType.APPLICATION_JSON)),regHandler::updateCustomer);
    }
}
