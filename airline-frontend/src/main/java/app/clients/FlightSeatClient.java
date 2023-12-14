package app.clients;

import app.controllers.api.rest.FlightSeatRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "FlightSeats", url = "${app.feign.config.url}")
public interface FlightSeatClient extends FlightSeatRestApi {
}
