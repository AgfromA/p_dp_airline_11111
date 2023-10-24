package app.services.interfaces;

import app.entities.search.Search;
import app.dto.SearchResult;

public interface SearchService {

    SearchResult getSearch(Search search);
}
