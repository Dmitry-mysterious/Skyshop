package org.skypro.skyshop.model.product;

import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;


public abstract class Product implements Searchable {
    private final UUID id;
    private final String productName;

    public Product(UUID id, String productName) {
        this.id = id;

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Название продукта не должно быть пустым.");
        }

        this.productName = productName;
    }

    public UUID getId() {
        return id;
    }

    public abstract int getPrice();

    public String getName() {
        return productName;
    }

    public abstract boolean isSpecial();

    @Override
    public String getSearchTerm() {
        return productName;
    }

    @Override
    public String getContentType() {
        return "PRODUCT";
    }

    @Override
    public String getSearchableName() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return Objects.equals(productName, ((Product) o).productName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productName);
    }
}
