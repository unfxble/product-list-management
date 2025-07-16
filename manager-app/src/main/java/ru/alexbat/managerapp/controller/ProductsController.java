package ru.alexbat.managerapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alexbat.managerapp.client.BadRequestException;
import ru.alexbat.managerapp.client.ProductsRestClient;
import ru.alexbat.managerapp.controller.payload.NewProductPayload;
import ru.alexbat.managerapp.entity.Product;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
@Slf4j
public class ProductsController {

    private final ProductsRestClient productsRestClient;

    @GetMapping("/list")
    public String getProductsList(Model model, @RequestParam(name = "filter", required = false) String filter, Principal principal) {
        log.info("User: {}", principal);
        model.addAttribute("products", productsRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String getNewProductPage() {
        return "catalogue/products/new-product";
    }

    @PostMapping("/create")
    public String createProduct(NewProductPayload payload, Model model) {
        try {
            Product product = productsRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", e.getErrors());
            return "catalogue/products/new-product";
        }
    }
}
