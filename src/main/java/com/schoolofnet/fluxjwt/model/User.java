package com.schoolofnet.fluxjwt.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {

	@Id
	private String id;
	private String username;
	private String password;

	public User(final String username, final String password) {
		this.username = username;
		this.password = password;
	}
}
