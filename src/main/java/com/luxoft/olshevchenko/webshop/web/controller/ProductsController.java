package com.luxoft.olshevchenko.webshop.web.controller;

import com.luxoft.olshevchenko.webshop.dto.ProductForCart;
import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.entity.Session;
import com.luxoft.olshevchenko.webshop.service.ProductService;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;
    private final SecurityService securityService;

    @GetMapping()
    protected String showAllProducts(HttpServletRequest request, ModelMap model) {
        dataForProductsList(request, model);
        return "products_list";
    }


    @GetMapping("/add")
    protected String getAddProductPage() {
        return "add_product";
    }

    @PostMapping("/add")
    protected String addProduct(@RequestParam String name, @RequestParam double price, ModelMap model) {
        try {
            if(name != null && name.length() > 0 && price > 0) {
                Optional<Product> optionalProduct = Optional.of(Product.builder()
                        .name(name)
                        .price(price)
                        .creationDate(LocalDateTime.now().withNano(0).withSecond(0))
                        .build());

                optionalProduct.ifPresent(product -> addProduct(product, model));

            } else {
                writeErrorResponse(model);
            }
        } catch (Exception e) {
            writeErrorResponse(model);
            throw new RuntimeException(e);
        }
        return "add_product";
    }

    @GetMapping("/delete")
    protected String deleteProduct(@RequestParam int id, HttpServletRequest request, ModelMap model) {
        productService.remove(id);
        String msgSuccess = "Product " + id + " was successfully deleted!";
        model.addAttribute("msgSuccess", msgSuccess);
        dataForProductsList(request, model);
        return "products_list";
    }

    @GetMapping("/edit")
    protected String getEditProductPage(@RequestParam int id, ModelMap model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/edit")
    protected String editProduct(@RequestParam int id, @RequestParam String name, @RequestParam double price, ModelMap model) {
        try {
            if(name != null && name.length() > 0 && price > 0) {
                Optional<Product> optionalProduct = Optional.of(Product.builder()
                        .id(id)
                        .name(name)
                        .price(price)
                        .build());
                model.addAttribute("product", optionalProduct.get());
                optionalProduct.ifPresent(product -> editProduct(product, model));
            } else {
                writeErrorResponse(model);
            }
        } catch (Exception e) {
            writeErrorResponse(model);
            throw new RuntimeException(e);
        }
        return "edit_product";
    }

    @GetMapping("/cart")
    protected String getCart(HttpServletRequest request, ModelMap model) {
        dataForProductsListCart(request, model);
        return "cart";
    }

    @PostMapping()
    protected String addProductToCart(@RequestParam int id, HttpServletRequest request, ModelMap model) {
        Session session = securityService.getSessionByToken(getUserToken(request));
        List<Product> products = session.getCart();
        dataForProductsList(request, model);
        Product product = productService.findById(id);
        products.add(product);
        return "products_list";
    }



    private void editProduct(Product product, ModelMap model) {
        productService.edit(product);
        String msgSuccess = "Product <i>" + product.getName() + "</i> was successfully changed!";
        model.addAttribute("msgSuccess", msgSuccess);
    }

    private void addProduct(Product product, ModelMap model) {
        productService.add(product);
        String msgSuccess = "Product <i>" + product.getName() + "</i> was successfully added!";
        model.addAttribute("msgSuccess", msgSuccess);
    }

    private void writeErrorResponse(ModelMap model) {
        String errorMsg = "Please fill up all fields!";
        model.addAttribute("errorMsg", errorMsg);
    }

    private void dataForProductsList(HttpServletRequest request, ModelMap model) {
        Session session = securityService.getSessionByToken(getUserToken(request));
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        addAttributes(model, session);
    }

    private void dataForProductsListCart(HttpServletRequest request, ModelMap model) {
        Session session = securityService.getSessionByToken(getUserToken(request));
        List<Product> products = session.getCart();
        products.sort(Comparator.comparingInt(Product::getId));

        Set<ProductForCart> productsForCart = new HashSet<>();
        for (int i = 0; i < products.size(); i++) {
            int quantity = Collections.frequency(products, products.get(i));
            productsForCart.add(dtoConvertToProductForCart(products.get(i), quantity));
        }
        model.addAttribute("products", productsForCart);
        addAttributes(model, session);
    }

    private ProductForCart dtoConvertToProductForCart(Product product, int quantity) {
        ProductForCart productForCart = new ProductForCart();
        productForCart.setId(product.getId());
        productForCart.setName(product.getName());
        productForCart.setPrice(product.getPrice());
        productForCart.setQuantity(quantity);
        return productForCart;
    }


    public String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> "user-token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .collect(Collectors.joining());
    }


    public void addAttributes(ModelMap model, Session session) {
        if (session == null) {
            model.addAttribute("login", "false");
        } else {
            model.addAttribute("user", session.getUser());
            model.addAttribute("login", "true");
        }
    }


}
