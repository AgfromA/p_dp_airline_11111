package app.services.interfaces;

import app.entities.search.Search;
import app.entities.search.SearchResult;

public interface SearchService {

    SearchResult getSearch(Search search);
}
