package app.airlinegateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class testController {
    @GetMapping("/test")
    public String test() {
        log.info("test");
        return "test";
    }

}
