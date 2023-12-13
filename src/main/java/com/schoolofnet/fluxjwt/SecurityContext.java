package com.schoolofnet.fluxjwt;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityContext implements ServerSecurityContextRepository {

	private final AuthManager authManager;

	public SecurityContext(final AuthManager authManager) {
		this.authManager = authManager;
	}

	@Override
	public Mono<Void> save(
		final ServerWebExchange exchange,
		final org.springframework.security.core.context.SecurityContext context
	) {
		return null;
	}

	@Override
	public Mono<org.springframework.security.core.context.SecurityContext> load(final ServerWebExchange exchange) {
		final ServerHttpRequest req = exchange.getRequest();
		final String authHeader = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String token = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.replace("Bearer ", "");
		}

		if (token != null) {
			final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
			return authManager.authenticate(auth).map(SecurityContextImpl::new);
		} else {
			return Mono.empty();
		}
	}
}
