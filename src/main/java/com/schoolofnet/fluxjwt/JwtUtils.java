package com.schoolofnet.fluxjwt;

import com.schoolofnet.fluxjwt.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.function.Function;

@Component
public class JwtUtils implements Serializable {

	public String getUsername(final String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(final String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(final String token) {
		return Jwts.parser()
			.setSigningKey("MY_SECRET")
			.parseClaimsJwt(token)
			.getBody();
	}

	public String genToken(final User user) {
		return Jwts.builder()
			.setSubject(user.getUsername())
			.signWith(SignatureAlgorithm.HS256, "MY_SECRET")
			.compact();
	}
}
