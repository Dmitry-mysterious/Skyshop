package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Article> storageArticle;
    private final Map<UUID, Product> storageProduct;

    public StorageService() {
        storageArticle = new HashMap<>();
        storageProduct = new HashMap<>();
        addItemsToMap();
    }

    // Добавление элементов в мапы
    private void addItemsToMap() {

        Article milkArticle = new Article(UUID.randomUUID(), "Молоко", "Деревенское коровье молоко");
        Article bradArticle = new Article(UUID.randomUUID(), "Хлеб", "Домашний хлеб из печи");
        Article lemonArticle = new Article(UUID.randomUUID(), "Лимон", "Большой желтый импортный лимон");
        Article juiceArticle = new Article(UUID.randomUUID(), "Сок", "Сок из натуральных яблок");

        storageArticle.put(milkArticle.getId(), milkArticle);
        storageArticle.put(bradArticle.getId(), bradArticle);
        storageArticle.put(lemonArticle.getId(), lemonArticle);
        storageArticle.put(juiceArticle.getId(), juiceArticle);

        SimpleProduct milk = new SimpleProduct(UUID.randomUUID(), "Молоко", 120);
        DiscountedProduct bread = new DiscountedProduct(UUID.randomUUID(), "Хлеб", 100, 12);
        SimpleProduct lemon = new SimpleProduct(UUID.randomUUID(), "Лимон", 40);
        FixPriceProduct juice = new FixPriceProduct(UUID.randomUUID(), "Сок");

        storageProduct.put(milk.getId(), milk);
        storageProduct.put(bread.getId(), bread);
        storageProduct.put(lemon.getId(), lemon);
        storageProduct.put(juice.getId(), juice);
    }

    // Получение продукта по id
    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(storageProduct.get(id));
    }

    // Вывод всех статей
    public Collection<Article> getAllArticles() {
        return storageArticle.values().stream().toList();
    }

    // Вывод всех продуктов
    public Collection<Product> getAllProducts() {
        return storageProduct.values().stream().toList();
    }
}
