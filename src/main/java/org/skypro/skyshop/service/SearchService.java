package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchService {
    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Collection<Searchable> createSingleCollection() {
        return Stream.concat(storageService.getAllArticles().stream(), storageService.getAllProducts().stream()).toList();
    }

    public Collection<SearchResult> search(String name) {
        return createSingleCollection().stream()
                .filter(v -> v.getSearchTerm().toLowerCase().contains(name.toLowerCase()))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }
}
