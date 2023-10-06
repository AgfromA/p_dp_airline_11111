package app.clients;

import app.controllers.api.rest.TimezoneRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "TimezoneClient", url = "${app.feign.config.url}")
public interface TimezoneClient extends TimezoneRestApi {
}
