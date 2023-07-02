package com.ll.gong9ri.base.security;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MemberEncryptionUtil {
	private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String CHARSET = StandardCharsets.UTF_8.name();
	@Value("${custom.AES.SALT}")
	private String SALT;

	private SecretKeySpec getSecretKey() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			String key = username + SALT;
			byte[] keyBytes = key.getBytes(CHARSET);
			byte[] truncatedKey = Arrays.copyOf(keyBytes, 16);

			return new SecretKeySpec(truncatedKey, "AES");
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate secret key: " + e.getMessage(), e);
		}
	}

	private IvParameterSpec getIvParameterSpec() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			String key = username + SALT;
			byte[] ivBytes = new byte[16];
			System.arraycopy(key.getBytes(CHARSET), 0, ivBytes, 0, 16);
			return new IvParameterSpec(ivBytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate IV parameter: " + e.getMessage(), e);
		}
	}

	private void initCipher(Cipher cipher, int mode) {
		try {
			SecretKeySpec secretKey = getSecretKey();
			IvParameterSpec ivParameterSpec = getIvParameterSpec();
			cipher.init(mode, secretKey, ivParameterSpec);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize cipher: " + e.getMessage(), e);
		}
	}

	public String encrypt(String plaintext) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			initCipher(cipher, Cipher.ENCRYPT_MODE);
			byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(CHARSET));

			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
		}
	}

	public String decrypt(String ciphertext) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			initCipher(cipher, Cipher.DECRYPT_MODE);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));

			return new String(decryptedBytes, CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
		}
	}
}