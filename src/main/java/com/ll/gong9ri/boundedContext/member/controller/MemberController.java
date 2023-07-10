package com.ll.gong9ri.boundedContext.member.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.dto.MemberInfoForm;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.entity.MemberImage;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.store.dto.StoreJoinDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final Rq rq;

	@PreAuthorize("isAnonymous()")
	@GetMapping("/login")
	public String showLogin() {
		return "usr/member/login";
	}

	@GetMapping("/storeForm")
	public String storeJoinForm() {
		return "usr/member/storeForm";
	}

	@PostMapping("/store")
	@PreAuthorize("not hasAuthority('store')")
	public String storeJoin(@Valid StoreJoinDTO dto) {
		final RsData<Member> rsStore = memberService.storeJoin(dto.getUsername(), dto.getPassword());

		return rq.redirectWithMsg("/", rsStore);
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public String showMyPage(Model model) {
		if(rq.isLogout()){
			return rq.redirectWithMsg("/", "로그인이 필요합니다.");
		}

		Optional<MemberImage> optionalMemberImage = memberService.findMemberImage(rq.getMember().getId());

		model.addAttribute("profileImage", optionalMemberImage.orElse(null));

		return "usr/member/me";
	}

/*	@PostMapping("/nickname")
	@PreAuthorize("isAuthenticated()")
	public String setNickname(@Valid MemberInfoForm form) {
		final RsData<Member> rsMember = memberService.setNickname(rq.getMember(), form.getNickname());
		if (rsMember.isFail())
			return rq.historyBack(rsMember);

		return rq.redirectWithMsg("/", rsMember);
	}

	@PostMapping("/image")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Object> uploadImageMember(
		@RequestPart(value = "files") List<MultipartFile> multipartFiles) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.uploadMemberImage(rq.getMember(), multipartFiles));
	}*/

	@PostMapping("/update-profile")
	@PreAuthorize("isAuthenticated()")
	public String updateProfile(
		@RequestPart(value = "files") List<MultipartFile> multipartFiles,
		@Valid MemberInfoForm form,
		BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return rq.redirectWithErrorMsg("/member/me", "프로필 수정 실패");
		}
		final RsData<Member> rsMember = memberService.setNickname(rq.getMember(), form.getNickname());
		if (rsMember.isFail())
			return rq.historyBack(rsMember);

		if (!multipartFiles.isEmpty()) {
			memberService.uploadMemberImage(rq.getMember(), multipartFiles);
		}

		return rq.redirectWithMsg("/member/me", "프로필이 변경되었습니다.");
	}
}
