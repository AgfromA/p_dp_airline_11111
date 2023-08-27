package app.clients;

import app.controllers.api.rest.ExampleRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "Common" , url = "http://localhost:8084")
public interface ExampleClient extends ExampleRestApi {

}
