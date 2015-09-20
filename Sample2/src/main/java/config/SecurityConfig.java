package config;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
    private final TokenAuthenticationService tokenAuthenticationService;
 
    @Autowired
    public SecurityConfig() {
        super(true);
      		
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
		KeyHandler kh = new KeyHandler();
		try {
			privateKey = kh.getPrivateKey("private_key.der");
			publicKey = kh.getPublicKey("public_key.der");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("reading failed");
		}

        tokenAuthenticationService = new TokenAuthenticationService(privateKey, publicKey);        
           
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl().and()
                .authorizeRequests()
 
                // Allow anonymous resource requests
                .antMatchers("/").permitAll()
                .antMatchers("/welcome/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("**/*.html").permitAll()
                .antMatchers("**/*.css").permitAll()
                .antMatchers("**/*.js").permitAll()
 
                // Allow anonymous logins
                .antMatchers("/auth/**").permitAll()
                
                // dba page
                .antMatchers("/dba/**").hasAnyRole("DBA","ADMIN")
                
                // admin page
                .antMatchers("/admin/**").hasRole("ADMIN")
 
                // All other request need to be authenticated
                .anyRequest().authenticated().and()
 
                // Custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class);
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }
 
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
 
    @Bean
    public TokenAuthenticationService tokenAuthenticationService() {
        return tokenAuthenticationService;
    }
}