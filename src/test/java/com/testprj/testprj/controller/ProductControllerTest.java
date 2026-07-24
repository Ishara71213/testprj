package com.testprj.testprj.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.assertj.MockMvcTester.from;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.testprj.testprj.entity.Product;
import com.testprj.testprj.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private ProductService productService;

    @Test
    void getAllProducts_returnsOkWithList() {
        Product product = new Product("Laptop", 1500.0, 5);
        product.setId(1L);
        when(productService.getAllProducts()).thenReturn(List.of(product));

        assertThat(mockMvcTester.get().uri("/api/products"))
                .hasStatusOk()
                .bodyJson().extractingPath("$[0].name").isEqualTo("Laptop");
    }

    @Test
    void getProductById_whenFound_returnsOk() {
        Product product = new Product("Laptop", 1500.0, 5);
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        assertThat(mockMvcTester.get().uri("/api/products/1"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.id").isEqualTo(1);
    }

    @Test
    void getProductById_whenNotFound_returns404() {
        when(productService.getProductById(99L)).thenReturn(Optional.empty());

        assertThat(mockMvcTester.get().uri("/api/products/99")).hasStatus4xxClientError();
    }

    @Test
    void createProduct_returns201WithCreatedProduct() {
        Product saved = new Product("Laptop", 1500.0, 5);
        saved.setId(1L);
        when(productService.createProduct(any(Product.class))).thenReturn(saved);

        assertThat(mockMvcTester.post().uri("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":1500.0,\"quantity\":5}"))
                .hasStatus(201)
                .bodyJson().extractingPath("$.id").isEqualTo(1);
    }

    @Test
    void updateProduct_whenFound_returnsOk() {
        Product updated = new Product("Laptop Pro", 2000.0, 3);
        updated.setId(1L);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(updated));

        assertThat(mockMvcTester.put().uri("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop Pro\",\"price\":2000.0,\"quantity\":3}"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.name").isEqualTo("Laptop Pro");
    }

    @Test
    void updateProduct_whenNotFound_returns404() {
        when(productService.updateProduct(eq(99L), any(Product.class))).thenReturn(Optional.empty());

        assertThat(mockMvcTester.put().uri("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X\",\"price\":1.0,\"quantity\":1}"))
                .hasStatus4xxClientError();
    }

    @Test
    void deleteProduct_whenFound_returns204() {
        when(productService.deleteProduct(1L)).thenReturn(true);

        assertThat(mockMvcTester.delete().uri("/api/products/1")).hasStatus(204);
    }

    @Test
    void deleteProduct_whenNotFound_returns404() {
        when(productService.deleteProduct(99L)).thenReturn(false);

        assertThat(mockMvcTester.delete().uri("/api/products/99")).hasStatus4xxClientError();
    }
}
