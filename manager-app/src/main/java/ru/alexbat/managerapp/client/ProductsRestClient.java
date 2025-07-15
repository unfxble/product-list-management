package ru.alexbat.managerapp.client;


import ru.alexbat.managerapp.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {

    List<Product> findAllProducts(String filter);

    Product createProduct(String title, String detail);

    Optional<Product> findProduct(int productId);

    void updateProduct(int productId, String title, String detail);

    void deleteProduct(int productId);
}
