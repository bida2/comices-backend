package com.vuetify.config;

import java.util.Collections;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class HTTPConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer  {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	CorsConfiguration config = new CorsConfiguration();
    	config.setAllowCredentials(true);
    	config.setAllowedOrigins(Collections.singletonList("http://localhost:9000"));
    	config.setAllowedMethods(Collections.singletonList("*"));
    	config.setAllowedHeaders(Collections.singletonList("*"));
    	source.registerCorsConfiguration("/**", config);
    	FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
    	bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    	return bean;
    }
    
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    	// Anonymous users can search for posts, get all posts (REST API) , get and see all resources and view posts in their entirety
    	http.authorizeRequests().antMatchers("/","/register","/login","/postComment","/contact").permitAll();
    	// Testing lines for h2 console -> comment them out or delete later
    	//http.csrf().ignoringAntMatchers("/h2-console/**");
    	//http.headers().frameOptions().sameOrigin();
    	// Only administrators can access the following resources
    	//http.authorizeRequests().antMatchers("/addPost","/delPost","/changePost").hasRole("ADMIN");
    }
	 

}
