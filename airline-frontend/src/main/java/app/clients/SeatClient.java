package app.clients;

import app.controllers.api.rest.SeatRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "Seats", url = "${app.feign.config.url}")
public interface SeatClient extends SeatRestApi {
}
