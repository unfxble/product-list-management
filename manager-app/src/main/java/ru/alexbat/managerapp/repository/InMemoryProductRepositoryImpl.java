package ru.alexbat.managerapp.repository;

import org.springframework.stereotype.Repository;
import ru.alexbat.managerapp.entity.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Repository
public class InMemoryProductRepositoryImpl implements ProductRepository {

    private final List<Product> products = Collections.synchronizedList(new LinkedList<>());

    public InMemoryProductRepositoryImpl() {
        IntStream.range(1, 4)
            .forEach(i -> products.add(new Product(i, "Товар №%d".formatted(i), "Описание №%d".formatted(i))));
    }

    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public Product save(Product product) {
        product.setId(products.stream()
            .max(Comparator.comparingInt(Product::getId))
            .map(Product::getId)
            .orElse(0) + 1);
        products.add(product);
        return product;
    }

    @Override
    public Optional<Product> findById(Integer productId) {
        return products.stream()
            .filter(product -> Objects.equals(product.getId(), productId))
            .findFirst();
    }

    @Override
    public void deleteById(Integer id) {
        products.removeIf(product -> Objects.equals(product.getId(), id));
    }
}
