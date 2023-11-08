package app.clients;

import app.controllers.api.rest.AircraftRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "Aircrafts", url = "${app.feign.config.url}")
public interface AircraftClient extends AircraftRestApi {
}
