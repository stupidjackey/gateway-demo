//package org.example.filter;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.micrometer.core.instrument.util.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class AuthGlobalFilter implements GlobalFilter, Ordered {
//
//
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    // 放行路径，可以编写配置类，配置在YML中
//    private static final String[] SKIP_PATH = {
//
//            "/app1/login", "/app1/skip/**"};
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//
//        // 1. 判断是否放行路径
//        String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
//        boolean match = Arrays.stream(SKIP_PATH).map(path -> path.replaceAll("/\\*\\*", "")).anyMatch(path -> path.startsWith(requestPath));
//        if (match) {
//
//
//            return chain.filter(exchange);
//        }
//        // 2. 判断是否包含Oauth2 令牌
//        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//        if (StringUtils.isEmpty(authorizationHeader) ) {
//
//
//            // 如果消息头中没有 Authorization ，并且不是 Bearer开头，则抛出异常
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
//            String result = "";
//            try {
//
//
//                Map<String, Object> map = new HashMap<>(16);
//                map.put("code", HttpStatus.UNAUTHORIZED.value());
//                map.put("msg", "当前请求未认证，不允许访问");
//                map.put("data", null);
//                result = objectMapper.writeValueAsString(map);
//            } catch (JsonProcessingException e) {
//
//
//                log.error(e.getMessage(), e);
//            }
//            DataBuffer buffer = response.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
//            return response.writeWith(Flux.just(buffer));
//        }
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//
//
//        return -1;
//    }
//}
