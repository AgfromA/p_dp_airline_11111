package app.services.interfaces;

import app.entities.account.search.Search;
import app.entities.account.search.SearchResult;

public interface SearchService {

    SearchResult saveSearch(Search search);
//    Search getSearchById(long id);
//    void saveSearchResult(SearchResult searchResult);
//    SearchResultProjection getSearchResultProjectionByID(Long id);
}