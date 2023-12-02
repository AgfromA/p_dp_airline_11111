package app.airlinegateway.security;

import app.airlinegateway.security.config.EndpointConfig;
import app.airlinegateway.security.config.RouterValidatorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class RouterValidator {

    private final Map<String, List<EndpointConfig>> openEndpoints;
    private final Set<String> needAuthoritySet;
    private final Map<String, Map<String, List<EndpointConfig>>> authorityEndpoints = new HashMap<>();

    @Autowired
    public RouterValidator(RouterValidatorConfig routerValidatorConfig) {

        openEndpoints = constructEndpointsMap(routerValidatorConfig.getOpenEndpoints());
        for(Map.Entry<String, List<EndpointConfig>> entry : routerValidatorConfig.getAuthorityEndpoints().entrySet()) {
            authorityEndpoints.put(entry.getKey(), constructEndpointsMap(entry.getValue()));
        }
        needAuthoritySet = authorityEndpoints.values().stream()
                .flatMap(map -> map.keySet().stream()).collect(Collectors.toSet());
    }

    private Map<String, List<EndpointConfig>> constructEndpointsMap(List<EndpointConfig> endpointConfigs) {
        return endpointConfigs.stream()
                .collect(Collectors.groupingBy(this::getPath));
    }

    private String getPath(EndpointConfig x) {
        String endpointUri = x.getUri();
        if (endpointUri.endsWith("/**")) {
            return endpointUri.substring(0, endpointUri.length() - 3);
        }
        return endpointUri;
    }

    public boolean isSecured(ServerHttpRequest request) {
        if(openEndpoints.containsKey(request.getURI().getPath())) {
            return openEndpoints.get(request.getURI().getPath()).stream()
                    .noneMatch(x -> isHttpMethodAllowed(request.getMethod(), x.getMethods()));
        }
        return false;
    }

    public boolean authorize(ServerHttpRequest request, List<String> roles) {
        for (String role : roles) {
            if (authorityEndpoints.get(role).containsKey(request.getURI().getPath())) {
                if (authorityEndpoints.get(role)
                        .get(request.getURI().getPath()).stream()
                        .anyMatch(x -> isHttpMethodAllowed(request.getMethod(), x.getMethods())))
                    return true;
            }
        }
        return false;
    }

    public boolean needAuthority(ServerHttpRequest request) {
        return needAuthoritySet.contains(request.getURI().getPath());
    }

    public static boolean isHttpMethodAllowed(HttpMethod requestMethod, List<String> allowedMethods) {
        return allowedMethods == null || allowedMethods.stream().anyMatch(method -> HttpMethod.resolve(method) == requestMethod);
    }
}
