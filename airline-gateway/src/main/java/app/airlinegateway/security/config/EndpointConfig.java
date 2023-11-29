package app.airlinegateway.security.config;

import lombok.Data;

import java.util.List;

@Data
public class EndpointConfig {
    String uri;
    List<String> methods;
}
