package com.schoolofnet.fluxjwt;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class AuthManager implements ReactiveAuthenticationManager {

	private final JwtUtils jwtUtils;

	public AuthManager(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	@Override
	public Mono<Authentication> authenticate(final Authentication authentication) {
		final String token = authentication.getCredentials().toString();
		final String username = jwtUtils.getUsername(token);

//		if (username != null) {
//			final UsernamePasswordAuthenticationToken auth =
//				new UsernamePasswordAuthenticationToken(username, username);
//
//			return Mono.just(auth);
//		} else {
//			return Mono.empty();
//		}

		return Mono.justOrEmpty(username)
			.map(u -> new UsernamePasswordAuthenticationToken(u, null, Collections.emptyList()));
	}
}
