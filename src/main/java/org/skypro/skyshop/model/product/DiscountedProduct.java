package org.skypro.skyshop.model.product;

import java.util.UUID;

public class DiscountedProduct extends Product{
    private final int basedPrice;
    private final int discount;

    public DiscountedProduct(UUID id, String productName, int basedPrice, int discount) {
        super(id, productName);

        if (basedPrice <= 0) {
            throw new IllegalArgumentException("Цена не может быть меньше или равняться нулю.");
        }

        if (discount > 100 || discount < 0) {
            throw new IllegalArgumentException("Скидка должна быть в диапазоне от 0 до 100.");
        }
        this.basedPrice = basedPrice;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    @Override
    public int getPrice() {
        return basedPrice * (100 - discount) / 100;
    }

    @Override
    public boolean isSpecial() { return true; }

    @Override
    public String toString() {
        return getName() + ": " + getPrice() + "(" + getDiscount() + "%)";
    }
}
