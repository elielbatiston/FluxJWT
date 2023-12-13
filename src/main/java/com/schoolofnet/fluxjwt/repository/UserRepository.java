package com.schoolofnet.fluxjwt.repository;

import com.schoolofnet.fluxjwt.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> { }
