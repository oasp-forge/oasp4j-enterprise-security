package org.sixgems;

import com.netflix.zuul.ZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
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
        public ZuulFilter customZuulFilter(){
            return new CustomPortallFilter();
        }

    }
}
