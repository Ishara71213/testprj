package com.testprj.testprj.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.testprj.testprj.entity.Product;
import com.testprj.testprj.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_returnsAllProducts() {
        Product product = new Product("Laptop", 1500.0, 5);
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(1).containsExactly(product);
    }

    @Test
    void getProductById_whenFound_returnsProduct() {
        Product product = new Product("Laptop", 1500.0, 5);
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertThat(result).contains(product);
    }

    @Test
    void getProductById_whenNotFound_returnsEmpty() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void createProduct_savesAndReturnsProduct() {
        Product product = new Product("Laptop", 1500.0, 5);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertThat(result).isEqualTo(product);
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_whenFound_updatesFieldsAndReturnsProduct() {
        Product existing = new Product("Laptop", 1500.0, 5);
        existing.setId(1L);
        Product details = new Product("Laptop Pro", 2000.0, 3);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Product> result = productService.updateProduct(1L, details);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Laptop Pro");
        assertThat(result.get().getPrice()).isEqualTo(2000.0);
        assertThat(result.get().getQuantity()).isEqualTo(3);
    }

    @Test
    void updateProduct_whenNotFound_returnsEmpty() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.updateProduct(1L, new Product("X", 1.0, 1));

        assertThat(result).isEmpty();
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_whenExists_deletesAndReturnsTrue() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean result = productService.deleteProduct(1L);

        assertThat(result).isTrue();
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_whenNotExists_returnsFalse() {
        when(productRepository.existsById(1L)).thenReturn(false);

        boolean result = productService.deleteProduct(1L);

        assertThat(result).isFalse();
        verify(productRepository, never()).deleteById(anyLong());
    }
}
