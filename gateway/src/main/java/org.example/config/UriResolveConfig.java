package org.example.config;

import org.example.filter.UriKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.PrincipalNameKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UriResolveConfig {

    @Bean
    public UriKeyResolver uriKeyResolver() {


        return new UriKeyResolver();
    }
    @Bean
    @Primary
    public PrincipalNameKeyResolver principalNameKeyResolver() {


        return new PrincipalNameKeyResolver();
    }


}
