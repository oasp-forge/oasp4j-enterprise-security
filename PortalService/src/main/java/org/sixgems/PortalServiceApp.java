package org.sixgems;

import com.netflix.zuul.ZuulFilter;
import io.jsonwebtoken.SignatureAlgorithm;
import org.sixgems.filter.JwtTokenRelayFilter;
import org.sixgems.filter.ProxyReverseFilter;
import org.sixgems.service.api.JwtTokenService;
import org.sixgems.service.api.SsoTokenExtractorService;
import org.sixgems.service.api.SsoUserDetailsService;
import org.sixgems.service.impl.JwtTokenServiceImpl;
import org.sixgems.service.impl.OpenAMSsoTokenExtractorService;
import org.sixgems.service.impl.DefaultSsoUserDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Created by Julian on 21.06.2015.
 */
@SpringBootApplication
@EnableZuulProxy
public class PortalServiceApp extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(PortalServiceApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<PortalServiceApp> applicationClass = PortalServiceApp.class;

    @Configuration
    protected static class ZuulConfig{

        @Bean
        public ZuulFilter authenticationHeaderFilter(){
            return new JwtTokenRelayFilter();
        }

        @Bean
        public ZuulFilter proxyReverseFilter(){
            return new ProxyReverseFilter();
        }
    }

    @Configuration
    @ComponentScan(basePackages = {"org.sixgems.service"})
    protected static class ServiceConfig{

        @Bean
        public SsoTokenExtractorService tokenExtractorService(){
            return new OpenAMSsoTokenExtractorService();
        }

        @Bean
        public JwtTokenService tokenConverterService(){
            JwtTokenServiceImpl converter = new JwtTokenServiceImpl();
            converter.setSignatureAlgorithm(SignatureAlgorithm.RS256);
            converter.setValidityPeriodInMin(10);
            return converter;
        }

        @Bean
        public SsoUserDetailsService userDetailsService(){
            return new DefaultSsoUserDetailsService();
        }

    }

    @Configuration
    @ComponentScan(basePackages = {"org.sixgems.model"})
    protected  static class DataConfig{

    }
}
