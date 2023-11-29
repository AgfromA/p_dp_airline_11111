package app.airlinegateway.security;

import app.airlinegateway.security.config.EndpointConfig;
import app.airlinegateway.security.config.RouterValidatorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
public class RouterValidator {

    private final List<EndpointConfig> openEndpoints;
    private final Map<String, List<EndpointConfig>> authorityEndpoints;

    @Autowired
    public RouterValidator(RouterValidatorConfig routerValidatorConfig) {
        this.openEndpoints = routerValidatorConfig.getOpenEndpoints();
        this.authorityEndpoints = routerValidatorConfig.getAuthorityEndpoints();
    }

    public boolean isSecured(ServerHttpRequest request) {
        return openEndpoints.stream().noneMatch(
                endpointConfig ->
                        findEndpoint(request.getURI().getPath(), endpointConfig.getUri()) &&
                                isHttpMethodAllowed(request.getMethod(), endpointConfig.getMethods())
        );
    }

    public boolean authorize(ServerHttpRequest request, List<String> roles) {
        for (String role : roles) {
            List<EndpointConfig> endpointConfigs = authorityEndpoints.get(role);

            for (EndpointConfig endpointConfig : endpointConfigs) {
                String uri = endpointConfig.getUri();
                List<String> allowedMethods = endpointConfig.getMethods();

                if (findEndpoint(request.getURI().getPath(), uri) &&
                        isHttpMethodAllowed(request.getMethod(), allowedMethods)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean needAuthority(ServerHttpRequest request) {
        for (List<EndpointConfig> endpointConfigs : authorityEndpoints.values()) {
            for (EndpointConfig endpointConfig : endpointConfigs) {
                if (findEndpoint(request.getURI().getPath(), endpointConfig.getUri()))
                    return true;
            }
        }
        return false;
    }

    public static boolean findEndpoint(String requestUri, String endpointUri) {
        if (endpointUri.endsWith("/**")) {
            return requestUri.contains(endpointUri.substring(0, endpointUri.length() - 4));
        }
        return requestUri.equals(endpointUri);
    }

    public static boolean isHttpMethodAllowed(HttpMethod requestMethod, List<String> allowedMethods) {
        return allowedMethods == null || allowedMethods.stream().anyMatch(method -> HttpMethod.resolve(method) == requestMethod);
    }
}
