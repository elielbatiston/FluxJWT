package com.schoolofnet.fluxjwt.repository;

import com.schoolofnet.fluxjwt.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

	Mono<User> findByUsername(String username);
}
