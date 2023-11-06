package app.clients;

import app.controllers.api.rest.DestinationRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "Destination", url = "${app.feign.config.url}")
public interface DestinationClient extends DestinationRestApi {
}
