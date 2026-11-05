package org.skypro.skyshop;

import org.junit.jupiter.api.Test;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.service.StorageService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StorageServiceTest {

    private final StorageService storageService = new StorageService();

    @Test
    void getAllProducts_returnPrefilledProducts() {
        assertThat(storageService.getAllProducts()).isNotEmpty();
    }

    @Test
    void getAllArticles_returnsPrefilledArticles() {
        assertThat(storageService.getAllArticles()).isNotEmpty();
    }

    @Test
    void getProductById_whenProductExists_returnsIt() {
        Product existing = storageService.getAllProducts().iterator().next();

        assertThat(storageService.getProductById(existing.getId())).contains(existing);
    }

    @Test
    void getProductById_whenProductDoesNotExist_returnsEmptyOptional() {
        assertThat(storageService.getProductById(UUID.randomUUID())).isEmpty();
    }
}
