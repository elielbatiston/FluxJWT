package com.schoolofnet.fluxjwt.handler;

import com.schoolofnet.fluxjwt.model.User;
import com.schoolofnet.fluxjwt.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class AuthHandler {

	private final UserRepository repository;

	private final BCryptPasswordEncoder encoder;

	public AuthHandler(final UserRepository repository, final BCryptPasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	public Mono<ServerResponse> signUp(final ServerRequest req) {
		final Mono<User> userMono = req.bodyToMono(User.class);
		return userMono.map(u -> {
				final String password = encoder.encode(u.getPassword());
				return new User(u.getUsername(), password);
			})
			.flatMap(this.repository::save)
			.flatMap(user -> ServerResponse.ok().body(BodyInserters.fromValue(user)));
	}
}
