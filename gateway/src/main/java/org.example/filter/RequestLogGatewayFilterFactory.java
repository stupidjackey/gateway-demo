package org.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.GatewayToStringStyler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class RequestLogGatewayFilterFactory extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {


    public RequestLogGatewayFilterFactory() {


        super(NameConfig.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {


        return Arrays.asList("name");
    }

    @Override
    public GatewayFilter apply(NameConfig config) {


        return new GatewayFilter() {


            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


                // 获取请求路径
                URI uri = exchange.getRequest().getURI();
                log.info("获取到请求路径：{}", uri.toString());
                //
                log.info("配置属性：{}", config.getName());
                return chain.filter(exchange);
            }

            @Override
            public String toString() {


                return GatewayToStringStyler.filterToStringCreator(RequestLogGatewayFilterFactory.this).toString();
            }
        };
    }
}
