package app.clients;

import app.controllers.api.rest.ExampleRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${app.feign.config.name}", url = "${app.feign.config.url}")
public interface ExampleClient extends ExampleRestApi {

}
