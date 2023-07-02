package com.ll.gong9ri.base.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEncryptionUtil {
	private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
	private static final String SALT = "_Gong9Ri"; // Salt 값을 공유해서 사용

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static String getEncryptionKey() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			String key = username + SALT;

			// SHA-256 해시 알고리즘을 사용하여 키 해시 생성
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedHash = digest.digest(key.getBytes());

			// 해시 결과를 16진수 문자열로 변환하여 리턴
			return bytesToHex(encodedHash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to generate encryption key.", e);
		}
	}

	public static String encrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			SecretKeySpec secretKeySpec = new SecretKeySpec(
				getEncryptionKey().getBytes(StandardCharsets.UTF_8),
				AES_ALGORITHM
			);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to encrypt data.", e);
		}
	}

	public static String decrypt(String encryptedData) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			SecretKeySpec secretKeySpec = new SecretKeySpec(
				getEncryptionKey().getBytes(StandardCharsets.UTF_8),
				AES_ALGORITHM
			);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);

			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Failed to decrypt data.", e);
		}
	}
}