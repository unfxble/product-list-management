package ru.alexbat.managerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import ru.alexbat.managerapp.client.ProductsRestClient;
import ru.alexbat.managerapp.client.RestClientProductsRestClientImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class TestingBeans {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
        return mock(OAuth2AuthorizedClientRepository.class);
    }

    @Bean
    @Primary
    public RestClientProductsRestClientImpl testRestClientProductRestClient(@Value("${product-list.services.catalog-service.uri:http://localhost:8081}") String catalogueBaseUri) {
        return new RestClientProductsRestClientImpl(RestClient.builder()
            .baseUrl(catalogueBaseUri)
            .build());
    }
}
