package ru.alexbat.managerapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alexbat.managerapp.controller.payload.UpdateProductPayload;
import ru.alexbat.managerapp.entity.Product;
import ru.alexbat.managerapp.service.ProductService;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductService productService;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return productService.findProduct(productId)
            .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("/edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("/edit")
    public String updateProduct(@ModelAttribute(value = "product", binding = false) Product product,
                                @Valid UpdateProductPayload payload,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors",
                bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
            return"catalogue/products/edit";
        } else {
            productService.updateProduct(product.getId(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute(value = "product", binding = false) Product product) {
        productService.deleteProduct(product.getId());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e,
                                               Model model,
                                               HttpServletResponse response,
                                               Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
            messageSource.getMessage(e.getMessage(), new Object[0], e.getMessage(), locale));
        return "errors/404";
    }
}