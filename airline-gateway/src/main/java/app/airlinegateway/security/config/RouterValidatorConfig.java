package app.airlinegateway.security.config;

import app.airlinegateway.util.YmlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "router-validator")
@PropertySource(value = "classpath:routerValidatorConfig.yml",
        factory = YmlPropertySourceFactory.class)
public class RouterValidatorConfig {
    private List<EndpointConfig> openEndpoints;
    private Map<String, List<EndpointConfig>> authorityEndpoints;

}

