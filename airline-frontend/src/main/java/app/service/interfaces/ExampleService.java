package app.service.interfaces;

import app.controllers.api.rest.ExampleRestApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "${app.feign.config.name}", url = "${app.feign.config.url}")
public interface ExampleService extends ExampleRestApi {
}
