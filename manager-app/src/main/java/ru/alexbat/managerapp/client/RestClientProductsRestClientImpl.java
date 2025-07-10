package ru.alexbat.managerapp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.alexbat.managerapp.controller.payload.NewProductPayload;
import ru.alexbat.managerapp.controller.payload.UpdateProductPayload;
import ru.alexbat.managerapp.entity.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
public class RestClientProductsRestClientImpl implements ProductsRestClient {

    private static final ParameterizedTypeReference<List<Product>> TYPE_REFERENCE = new ParameterizedTypeReference<>() {
    };

    private final RestClient restClient;

    @Override
    public List<Product> findAllProducts() {
        return restClient
            .get()
            .uri("/catalogue-api/products")
            .retrieve()
            .body(TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(String title, String detail) {
        try {
            return restClient
                .post()
                .uri("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new NewProductPayload(title, detail))
                .retrieve()
                .body(Product.class);
        } catch (HttpClientErrorException.BadRequest e) {
            ProblemDetail problemDetail = e.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        try {
            return Optional.ofNullable(restClient
                .get()
                .uri("catalogue-api/products/{productId}", productId)
                .retrieve()
                .body(Product.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(int productId, String title, String detail) {
        try {
            restClient
                .patch()
                .uri("/catalogue-api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateProductPayload(title, detail))
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest e) {
            ProblemDetail problemDetail = e.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            restClient
                .delete()
                .uri("catalogue-api/products/{productId}", productId)
                .retrieve()
                .body(Product.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException(e);
        }
    }
}
