package com.schoolofnet.fluxjwt.handler;

import com.schoolofnet.fluxjwt.JwtUtils;
import com.schoolofnet.fluxjwt.model.User;
import com.schoolofnet.fluxjwt.repository.UserRepository;
import org.springframework.http.HttpStatus;
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

	private final JwtUtils jwtUtils;

	public AuthHandler(
		final UserRepository repository,
		final BCryptPasswordEncoder encoder,
		final JwtUtils jwtUtils
	) {
		this.repository = repository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
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

	public Mono<ServerResponse> login(final ServerRequest req) {
		final Mono<User> userMono = req.bodyToMono(User.class);
		return userMono
			.flatMap(u -> this.repository.findByUsername(u.getUsername())
				.flatMap(user -> {
					if (user != null && encoder.matches(u.getPassword(), user.getPassword())) {
						return ServerResponse.ok()
							.body(BodyInserters.fromValue(jwtUtils.genToken(user)));
					} else {
						return ServerResponse.status(HttpStatus.UNAUTHORIZED)
							.body(BodyInserters.fromValue("Invalid credentials"));
					}
				}))
			.switchIfEmpty(ServerResponse.badRequest()
				.body(BodyInserters.fromValue("User does not exist")));
	}

}
