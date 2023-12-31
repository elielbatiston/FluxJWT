package com.schoolofnet.fluxjwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfigsFilter {

	private final AuthManager authManager;

	private final SecurityContext securityContext;

	public SecurityConfigsFilter(final AuthManager authManager, final SecurityContext securityContext) {
		this.authManager = authManager;
		this.securityContext = securityContext;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
		return http
			.cors().disable()
			.csrf().disable()
			.authenticationManager(authManager)
			.securityContextRepository(securityContext)
			.authorizeExchange()
			.pathMatchers("/sign-up/**", "/login/**").permitAll()
			.pathMatchers(HttpMethod.OPTIONS).permitAll()
			.anyExchange().authenticated()
			.and()
			.build();
	}

	@Bean
	public BCryptPasswordEncoder passEncode() {
		return new BCryptPasswordEncoder();
	}
}
