package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    public void addInBasket(UUID id) {
        storageService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар с id " + id + " не найден"));
        productBasket.addProduct(id);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketMap = productBasket.getAllProducts();

        List<BasketItem> items = basketMap.entrySet().stream()
                .map(entry -> {
                    UUID id = entry.getKey();
                    int quantity = entry.getValue();

                    Product product = storageService.getProductById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + id));

                    return new BasketItem(product, quantity);
                }).collect(Collectors.toList());

        return new UserBasket(items);
    }
}
