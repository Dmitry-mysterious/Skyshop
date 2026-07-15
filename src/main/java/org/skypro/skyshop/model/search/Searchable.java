package org.skypro.skyshop.model.search;

import java.util.UUID;

public interface Searchable {
    String getSearchTerm();

    String getContentType();

    String getSearchableName();

    UUID getId();

    default String getStringRepresentation() {
        return "имя " + getSearchableName() + " — тип " + getContentType();
    }
}
