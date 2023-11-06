package app.clients;

import app.controllers.api.rest.SearchControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "SearchResult", url = "${app.feign.config.url}")
public interface SearchClient extends SearchControllerApi {
}
