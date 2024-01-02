package app.clients;

import app.controllers.api.rest.FlightRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "FlightGenerator", url = "${app.feign.config.url}")
public interface FlightGeneratorClient extends FlightRestApi {
}
