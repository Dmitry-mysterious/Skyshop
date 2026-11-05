package org.skypro.skyshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    // 1. Добавление несуществующего товара -> исключение
    @Test
    void addInBasket_whenProductDoesNotExist_throwsException() {
        UUID invalidId = UUID.randomUUID();
        when(storageService.getProductById(invalidId)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> basketService.addInBasket(invalidId));

        // и в корзину ничего не попало
        verify(productBasket, never()).addProduct(invalidId);
    }

    // 2. Добавление существующего товара -> вызван addProduct у ProductBasket
    @Test
    void addInBasket_whenProductExists_callsAddProductOnBasket() {
        UUID validId = UUID.randomUUID();
        Product product = new SimpleProduct(validId, "TestProduct", 100);
        when(storageService.getProductById(validId)).thenReturn(Optional.of(product));

        basketService.addInBasket(validId);

        verify(productBasket, times(1)).addProduct(validId);
    }

    // 3. Пустая корзина
    @Test
    void getUserBasket_whenProductBasketIsEmpty_returnsEmptyBasket() {
        when(productBasket.getAllProducts()).thenReturn(Map.of());

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getBasketItems()).isEmpty();
        assertThat(userBasket.getTotal()).isZero();
    }

    // 4. Корзина с товаром: проверяем размер списка и total = цена * количество
    @Test
    void getUserBasket_whenProductBasketHasItems_returnsFilledBasket() {
        UUID id = UUID.randomUUID();
        int price = 120;
        int quantity = 3;
        Product product = new SimpleProduct(id, "TestProduct", price);
        when(productBasket.getAllProducts()).thenReturn(Map.of(id, quantity));
        when(storageService.getProductById(id)).thenReturn(Optional.of(product));

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getBasketItems()).hasSize(1);
        BasketItem item = userBasket.getBasketItems().get(0);
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQuantity()).isEqualTo(quantity);
        assertThat(userBasket.getTotal()).isEqualTo(price * quantity);
    }

    // Дополнительно: несколько разных товаров, total суммируется
    @Test
    void getUserBasket_withSeveralProducts_sumsTotal() {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();
        when(productBasket.getAllProducts()).thenReturn(Map.of(firstId, 2, secondId, 1));
        when(storageService.getProductById(firstId))
                .thenReturn(Optional.of(new SimpleProduct(firstId, "First", 100)));
        when(storageService.getProductById(secondId))
                .thenReturn(Optional.of(new SimpleProduct(secondId, "Second", 50)));

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getBasketItems()).hasSize(2);
        assertThat(userBasket.getTotal()).isEqualTo(2 * 100 + 50);
    }

    // Дополнительно: в корзине лежит id, которого уже нет в хранилище
    @Test
    void getUserBasket_whenProductMissingInStorage_throwsException() {
        UUID id = UUID.randomUUID();
        when(productBasket.getAllProducts()).thenReturn(Map.of(id, 1));
        when(storageService.getProductById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> basketService.getUserBasket());
    }
}