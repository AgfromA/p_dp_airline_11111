package app.clients;

import app.controllers.api.rest.FlightRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "Flight", url = "${app.feign.config.url}")
public interface FlightClient extends FlightRestApi {


}
