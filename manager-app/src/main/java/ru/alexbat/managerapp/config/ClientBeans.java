package ru.alexbat.managerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.alexbat.managerapp.client.RestClientProductsRestClientImpl;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClientImpl productsRestClient(@Value("${product-list.services.catalog-service.uri:http://localhost:8081}") String catalogueBaseUri) {
        return new RestClientProductsRestClientImpl(RestClient.builder()
            .baseUrl(catalogueBaseUri)
            .build());
    }
}
