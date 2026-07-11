package org.skypro.skyshop.model.product;

import java.util.UUID;

public class FixPriceProduct extends Product {
    private static final int FIX_PRICE = 150;

    public FixPriceProduct(UUID id, String productName) {
        super(id, productName);
    }

    @Override
    public int getPrice() {
        return FIX_PRICE;
    }

    @Override
    public boolean isSpecial() { return true; }

    @Override
    public String toString() {
        return getName() + ": Фиксированная цена " + getPrice();
    }
}
