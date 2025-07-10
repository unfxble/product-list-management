package ru.alexbat.managerapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alexbat.managerapp.controller.payload.NewProductPayload;
import ru.alexbat.managerapp.entity.Product;
import ru.alexbat.managerapp.service.ProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {

    private final ProductService productService;

    @GetMapping("/list")
    public String getProductsList(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String getNewProductPage() {
        return "catalogue/products/new-product";
    }

    @PostMapping("/create")
    public String createProduct(@Validated NewProductPayload payload, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors",
                bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
            return "catalogue/products/new-product";
        } else {
            Product product = productService.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }
}
