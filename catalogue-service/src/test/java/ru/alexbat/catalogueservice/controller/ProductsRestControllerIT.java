package ru.alexbat.catalogueservice.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional // для отката бд после теста в исходное состояние
@SpringBootTest
@AutoConfigureMockMvc
class ProductsRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/products.sql")
    void findProducts_ReturnProductsList() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue-api/products")
            .param("filter", "товар")
            .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        // when
        mockMvc.perform(requestBuilder)
            //then
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                    [
                      {"id":  1, "title":  "Товар №1", "details":  "Описание товара №1"},
                      {"id":  3, "title":  "Товар №3", "details":  "Описание товара №3"}
                    ]
                    """)
            );
    }

    @Test
    void createProduct_RequestIsValid_ReturnNewProduct() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title":  "Абсолютно новый товар","details":  "Такого вообще раньше не было"}
                """
            )
            .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        // when
        mockMvc.perform(requestBuilder)
            // then
            .andDo(print()).andExpectAll(
                status().isCreated(),
                header().string(HttpHeaders.LOCATION, "http://localhost/catalogue-api/products/1"),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                                     {"id": 1,
                                     "title":  "Абсолютно новый товар",
                                      "details":  "Такого вообще раньше не было"}
                    """)
            );
    }

    @Test
    void createProduct_RequestIsInvalid_ReturnProblemsDetail() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title":  "   ","details":  null}
                """
            )
            .locale(Locale.of("ru", "RU"))
            .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        // when
        mockMvc.perform(requestBuilder)
            // then
            .andDo(print()).andExpectAll(
                status().isBadRequest(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                content().json("""
                     {"errors": ["Название товара должно быть указано"]}
                    """)
            );
    }

    @Test
    void createProduct_RequestIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title":  "   ","details":  null}
                """
            )
            .locale(Locale.of("ru", "RU"))
            .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        // when
        mockMvc.perform(requestBuilder)
            // then
            .andDo(print()).andExpectAll(
                status().isForbidden()
            );
    }
}