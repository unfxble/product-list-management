package ru.alexbat.catalogueservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexbat.catalogueservice.entity.Product;
import ru.alexbat.catalogueservice.repository.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String details) {
        return productRepository.save(new Product(null, title, details));
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void updateProduct(Integer id, String title, String details) {
        productRepository.findById(id).ifPresentOrElse(product -> {
                product.setTitle(title);
                product.setDetails(details);
            }, () -> {
                throw new NoSuchElementException("Product not found");
            }
        );
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
