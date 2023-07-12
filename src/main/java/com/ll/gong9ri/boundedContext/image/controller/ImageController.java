package com.ll.gong9ri.boundedContext.image.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class ImageController {

	private final ImageService imageService;
	private final Rq rq;

	@GetMapping("")
	public String getUpload(){
		return "usr/member/upload";
	}

	@PostMapping("/sample")
	public ResponseEntity<Object> uploadImagesSample(
		@RequestPart(value = "files") List<MultipartFile> multipartFiles) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(imageService.uploadImagesSample(multipartFiles));
	}

	@PostMapping("/chats/{chatRoomId}")
	public ResponseEntity<Object> uploadChatImages(
		@RequestPart(value = "files") List<MultipartFile> multipartFiles, @PathVariable String chatRoomId) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(imageService.uploadChatImages(multipartFiles, chatRoomId));
	}


	/*
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_STORE')")
	@PostMapping("/product")
	public ResponseEntity<Object> uploadImagesProduct(
	// TODO : 나중에 구현
		@RequestPart(value = "files") List<MultipartFile> multipartFiles) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(imageService.uploadImagesProduct(rq.getMember(), multipartFiles));
	}*/
}
