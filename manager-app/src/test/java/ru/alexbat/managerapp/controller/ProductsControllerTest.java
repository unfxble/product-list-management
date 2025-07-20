package ru.alexbat.managerapp.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.alexbat.managerapp.client.BadRequestException;
import ru.alexbat.managerapp.client.ProductsRestClient;
import ru.alexbat.managerapp.controller.payload.NewProductPayload;
import ru.alexbat.managerapp.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты ProductsController")
class ProductsControllerTest {

    @Mock
    ProductsRestClient productsRestClient = Mockito.mock(ProductsRestClient.class);

    @InjectMocks
    ProductsController controller;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("createProduct создаст новый товар и перенаправит на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        //given
        String title = "Новый товар";
        String description = "Описание нового товара";
        var payload = new NewProductPayload(title, description);
        var model = new ConcurrentModel();

        doReturn(new Product(1,title, description))
            .when(productsRestClient)
            .createProduct(title, description);

        //when
        var result = controller.createProduct(payload, model);

        //then
        assertEquals("redirect:/catalogue/products/1", result);
        // проверяем что метод вызывался
        verify(productsRestClient).createProduct(title, description);
        // проверяем что к данному объекту больше никто не обращался
        verifyNoMoreInteractions(productsRestClient);
    }

    @Test
    @DisplayName("createProduct вернет страницу с ошибками, если запрос невалиден")
    void createProduct_RequestIsInvalid_ReturnsProductFormWithErrors() {
        //given
        String title = "   ";
        String description = null;
        var payload = new NewProductPayload(title, description);
        var model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
            .when(productsRestClient)
            .createProduct(title, description);

        //when
        var result = controller.createProduct(payload, model);

        //then
        assertEquals("catalogue/products/new-product", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));


        // проверяем что метод вызывался
        verify(productsRestClient).createProduct(title, description);
        // проверяем что к данному объекту больше никто не обращался
        verifyNoMoreInteractions(productsRestClient);
    }
}