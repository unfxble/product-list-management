package ru.alexbat.managerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import ru.alexbat.managerapp.client.RestClientProductsRestClientImpl;
import ru.alexbat.managerapp.security.OAuthClientHttpInterceptor;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClientImpl productsRestClient(
        @Value("${product-list.services.catalog-service.uri:http://localhost:8081}") String catalogueBaseUri,
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository authorizedClientRepository,
        @Value("${product-list.services.catalog-service.registration-id:keycloak}") String registrationId) {
        return new RestClientProductsRestClientImpl(RestClient.builder()
            .baseUrl(catalogueBaseUri)
            .requestInterceptor(new OAuthClientHttpInterceptor(
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository),
                registrationId))
            .build());
    }
}
