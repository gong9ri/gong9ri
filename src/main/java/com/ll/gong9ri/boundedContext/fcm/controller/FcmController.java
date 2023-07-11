package com.ll.gong9ri.boundedContext.fcm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.fcm.dto.TokenDTO;
import com.ll.gong9ri.boundedContext.fcm.service.FcmService;
import com.ll.gong9ri.boundedContext.fcm.token.Token;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FcmController {

	private final FcmService fcmService;
	private final Rq rq;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/v1/tokens")
	public ResponseEntity<String> v1(@RequestBody @Valid TokenDTO token) {
		RsData<Token> tokenRsData = fcmService.saveToken(token, rq.getMember());

		return ResponseEntity.ok(tokenRsData.getResultCode());
	}
}