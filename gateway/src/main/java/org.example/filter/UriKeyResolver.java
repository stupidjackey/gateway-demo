package org.example.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class UriKeyResolver implements KeyResolver {



    public static final String BEAN_NAME = "uriKeyResolver";

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {


        return Mono.just(exchange.getRequest().getURI().getPath());
    }


}
