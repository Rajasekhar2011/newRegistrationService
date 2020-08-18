package com.spring.reactive.registration.controllerTest;

import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.dto.LoginDetails;
import com.spring.reactive.registration.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class RegistrationControllerTest {

    @Autowired
    WebTestClient webTestClient;


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
    public void createCustomer(){
        Customer newCustomer = new Customer("4","Aug3@1992","Chandra","Sekhar","Chandrasekhar","Kanala","AP","India","chandrasekhar@gmail.com","CJOPRJ105K",9505957693L,"1996-10-25","Loan");
        webTestClient.post().uri("/v1/register").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newCustomer), Customer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("4")
                .jsonPath("$.firstName").isEqualTo("Chandra")
                .jsonPath("$.userName").isEqualTo("ChandraSekhar");
    }

    @Test
    public void loginTest(){
        LoginDetails ld= new LoginDetails("3", "Jun21@1994");
        webTestClient.post().uri("/v1/login").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(ld), LoginDetails.class)
                .exchange()
                .expectStatus().isFound();

    }

    @Test
    public void updateCustomer(){
        Long newContact = 7675020618l;
        String mail = "rajasekharreddy.kvr@gmail.com";
        Customer cust = new Customer("1","Nov20@1994","Raja","Sekhar","Rajasekhar","Nandyal","AP","India",mail,"BKOPRJ978K",newContact,"1994-11-20","Savings");
        webTestClient.put().uri("/v1/login/{userId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(cust), Customer.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo("rajasekharreddy.kvr@gmail.com");

    }


}
