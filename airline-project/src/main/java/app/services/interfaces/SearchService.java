package app.services.interfaces;

import app.dto.search.Search;
import app.dto.search.SearchResult;

public interface SearchService {

    SearchResult getSearch(Search search);
}