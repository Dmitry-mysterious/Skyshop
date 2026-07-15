package org.skypro.skyshop.model.product;

import java.util.UUID;

public class SimpleProduct extends Product {
    private final int price;

    public SimpleProduct(UUID id, String productName, int price) {
        super(id, productName);

        if (price <= 0) {
            throw new IllegalArgumentException("Цена не может быть меньше или равняться нулю.");
        }
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public boolean isSpecial() { return false; }

    @Override
    public String toString() {
        return getName() + ": " + getPrice();
    }
}
