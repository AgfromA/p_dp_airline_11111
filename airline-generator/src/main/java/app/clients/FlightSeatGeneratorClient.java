package app.clients;

import app.controllers.api.rest.FlightSeatRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "FlightSeatGenerator", url = "${app.feign.config.url}")
public interface FlightSeatGeneratorClient extends FlightSeatRestApi {
}
