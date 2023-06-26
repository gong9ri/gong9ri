package com.ll.gong9ri.base.jwt;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.ll.gong9ri.standard.util.Ut;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	private final SecretKey jwtSecretKey;

	private SecretKey getSecretKey() {
		return jwtSecretKey;
	}

	public String genToken(Map<String, Object> claims, int expirationSeconds) {
		return Jwts.builder()
			.claim("body", Ut.json.toStr(claims))
			.setExpiration(new Date(new Date().getTime() + 1000L * expirationSeconds))
			.signWith(getSecretKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	public boolean verify(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSecretKey())
				.build()
				.parseClaimsJws(token);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public Map<String, Object> getClaims(String token) {
		String body = Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("body", String.class);

		return Ut.json.toMap(body);
	}
}