package com.petrdulnev.gatewayservice.route;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Component
public class OpenRoute {

    public final List<String> openEndpoints = List.of(
            "/api/Authentication/SignUp",
            "/api/Authentication/SignIn",
            "/api/Authentication/status"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
