package ru.alexbat.catalogueservice.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.alexbat.catalogueservice.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/products.sql")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIT {

    @Autowired
    ProductRepository productRepository;

    @Test
    void findAllByTitleLikeIgnoreCase_ReturnsFilteredList() {
        // given
        var filter = "%sport%";

        // when
        var products = productRepository.findAllByTitleLikeIgnoreCase(filter);

        // then
        assertEquals(List.of(new Product(2, "RitterSport", "Соленый миндаль")), products);
    }
}