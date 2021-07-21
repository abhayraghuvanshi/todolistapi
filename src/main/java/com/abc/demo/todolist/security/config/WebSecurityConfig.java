package com.abc.demo.todolist.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.formLogin(); //By this , You can  pass user/pass from browser these detail
		http.httpBasic();//This is for enable user/password field from postman.You cant pass from browser these detail.
		http.authorizeRequests()
		.mvcMatchers(HttpMethod.GET,"/api/todo/**").hasAnyRole("USER","ADMIN")
		.mvcMatchers(HttpMethod.POST,  "/api/todo/**").hasAnyRole("USER","ADMIN")
		.mvcMatchers(HttpMethod.DELETE, "/api/todo/**").hasAnyRole("USER","ADMIN")
		.mvcMatchers(HttpMethod.PATCH, "/api/todo/**").hasAnyRole("USER","ADMIN")
		.mvcMatchers(HttpMethod.PUT,  "/api/todo/**").hasAnyRole("USER","ADMIN")
		.anyRequest().denyAll()
		.and().csrf().disable()
		.logout().logoutSuccessUrl("/");
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
