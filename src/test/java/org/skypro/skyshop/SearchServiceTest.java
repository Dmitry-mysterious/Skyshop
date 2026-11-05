package org.skypro.skyshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    // Сценарий 1: в хранилище ничего нет
    @Test
    void search_whenStorageIsEmpty_returnsEmptyCollection() {
        when(storageService.getAllArticles()).thenReturn(List.of());
        when(storageService.getAllProducts()).thenReturn(List.of());

        Collection<SearchResult> results = searchService.search("Test");

        assertThat(results).isEmpty();
    }

    // Сценарий 2: объекты есть, но ни один не подходит под запрос
    @Test
    void search_whenStorageHasObjectsButNoneMatches_returnsEmptyCollection() {
        when(storageService.getAllArticles()).thenReturn(List.of(createArticle("TestArticle", "Some content")));
        when(storageService.getAllProducts()).thenReturn(List.of(createProduct("TestProduct", 100)));

        Collection<SearchResult> results = searchService.search("абракадабра");

        assertThat(results).isEmpty();
    }

    // Сценарий 3: в хранилище есть подходящий объект
    @Test
    void search_whenStorageHasMatchingProduct_returnsSingleResult() {
        SimpleProduct product = createProduct("TestProduct", 100);
        when(storageService.getAllArticles()).thenReturn(List.of());
        when(storageService.getAllProducts()).thenReturn(List.of(product));

        Collection<SearchResult> results = searchService.search("Test");

        assertThat(results).hasSize(1);
        SearchResult result = results.iterator().next();
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo("TestProduct");
        assertThat(result.getContentType()).isEqualTo("PRODUCT");
    }

    // Дополнительно: поиск не зависит от регистра
    @Test
    void search_isCaseInsensitive() {
        when(storageService.getAllArticles()).thenReturn(List.of());
        when(storageService.getAllProducts()).thenReturn(List.of(createProduct("TestProduct", 100)));

        Collection<SearchResult> results = searchService.search("tEsTpRoDuCt");

        assertThat(results).hasSize(1);
    }

    // Дополнительно: ищутся и статьи, и продукты; в результат попадают только подходящие
    @Test
    void search_returnsMatchingArticlesAndProductsOnly() {
        Article matchingArticle = createArticle("Test article", "Content");
        when(storageService.getAllArticles()).thenReturn(List.of(
                matchingArticle,
                createArticle("Other article", "Content")));
        when(storageService.getAllProducts()).thenReturn(List.of(
                createProduct("TestProduct", 100),
                createProduct("Other", 50)));

        Collection<SearchResult> results = searchService.search("Test");

        assertThat(results)
                .hasSize(2)
                .extracting(SearchResult::getContentType)
                .containsExactlyInAnyOrder("ARTICLE", "PRODUCT");
    }

    // Дополнительно: поиск идёт и по содержимому статьи (getSearchTerm = заголовок + текст)
    @Test
    void search_matchesArticleByContent() {
        when(storageService.getAllArticles()).thenReturn(List.of(createArticle("Title", "unique-keyword inside")));
        when(storageService.getAllProducts()).thenReturn(List.of());

        Collection<SearchResult> results = searchService.search("unique-keyword");

        assertThat(results).hasSize(1);
        assertThat(results.iterator().next().getName()).isEqualTo("Title");
    }

    private SimpleProduct createProduct(String name, int price) {
        return new SimpleProduct(UUID.randomUUID(), name, price);
    }

    private Article createArticle(String title, String content) {
        return new Article(UUID.randomUUID(), title, content);
    }
}