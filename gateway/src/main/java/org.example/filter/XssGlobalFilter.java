package org.example.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class XssGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    ObjectMapper objectMapper;

    private final static Pattern[] scriptPatterns = {


            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        HttpMethod method = serverHttpRequest.getMethod();
        URI uri = exchange.getRequest().getURI();
        if (method == HttpMethod.GET) {


            String paramsString = uri.getRawQuery().replaceAll("[\u0000\n\r]", "");
            if (StringUtils.isBlank(paramsString)) {


                return chain.filter(exchange);
            }
            String lowerValue = URLDecoder.decode(paramsString, "UTF-8").toLowerCase();
            for (Pattern pattern : scriptPatterns) {


                if (pattern.matcher(lowerValue).find()) {


                    return xssResponse(exchange);
                }
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {


        return 0;
    }

    public Mono<Void> xssResponse(ServerWebExchange exchange) {


        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String result = "";
        try {


            Map<String, Object> map = new HashMap<>(16);
            map.put("code", HttpStatus.BAD_REQUEST.value());
            map.put("msg", "当前请求可能存在恶意脚本，拒绝访问");
            result = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {


            log.error(e.getMessage(), e);
        }
        DataBuffer buffer = response.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }
}
