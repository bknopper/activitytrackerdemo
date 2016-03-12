package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class SimpleApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2RestOperations oauth2RestTemplate;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.antMatcher("/**")
				.authorizeRequests()
				.antMatchers("/", "/login**", "/webjars/**")
				.permitAll()
				.anyRequest()
				.authenticated();
	}

	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}


	@RequestMapping("/chr")
	public String chr() {

		ResponseEntity<String> response = oauth2RestTemplate.getForEntity(
				"https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1sec/time/00:00/23:59.json",
				String.class);

		System.out.println(response);
		return response.toString();
	}

	public static void main(String[] args) {
		SpringApplication.run(SimpleApplication.class, args);
	}
}
