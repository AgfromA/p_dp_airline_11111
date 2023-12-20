package app.clients;
import app.controllers.api.rest.AccountRestApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "Accounts", url = "${app.feign.config.url}")
public interface AccountClient extends AccountRestApi{

}
