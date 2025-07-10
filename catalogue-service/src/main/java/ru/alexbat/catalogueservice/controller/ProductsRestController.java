package ru.alexbat.catalogueservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexbat.catalogueservice.controller.payload.NewProductPayload;
import ru.alexbat.catalogueservice.entity.Product;
import ru.alexbat.catalogueservice.service.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-api/products")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping
    public List<Product> findProducts() {
        return productService.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = productService.createProduct(payload.title(), payload.details());
            return ResponseEntity
                .created(uriBuilder
                    .replacePath("/catalogue-api/products/{productId}")
                    .build(Map.of("productId", product.getId())))
                .body(product);
        }
    }
}
