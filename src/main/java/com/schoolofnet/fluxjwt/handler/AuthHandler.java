package com.schoolofnet.fluxjwt.handler;

import com.schoolofnet.fluxjwt.model.User;
import com.schoolofnet.fluxjwt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class AuthHandler {

	private final UserRepository repository;

	public AuthHandler(final UserRepository repository) {
		this.repository = repository;
	}

	public Mono<ServerResponse> signUp(final ServerRequest req) {
		final Mono<User> userMono = req.bodyToMono(User.class);
		return userMono.map(u -> new User(u.getUsername(), u.getPassword()))
			.flatMap(this.repository::save)
			.flatMap(user -> ServerResponse.ok().body(user, User.class));
	}
}
