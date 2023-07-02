package com.ll.gong9ri.boundedContext.privacy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.base.security.MemberEncryptionUtil;
import com.ll.gong9ri.boundedContext.privacy.dto.PrivacyDTO;
import com.ll.gong9ri.boundedContext.privacy.service.MemberPrivacyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member/privacy")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class MemberPrivacyController {
	private final MemberPrivacyService memberPrivacyService;
	private final Rq rq;

	private PrivacyDTO decryptPrivacyDTO(PrivacyDTO privacyDTO) {
		return PrivacyDTO.builder()
			.recipient(MemberEncryptionUtil.decrypt(privacyDTO.getRecipient()))
			.phoneNumber(MemberEncryptionUtil.decrypt(privacyDTO.getPhoneNumber()))
			.mainAddress(MemberEncryptionUtil.decrypt(privacyDTO.getMainAddress()))
			.subAddress(MemberEncryptionUtil.decrypt(privacyDTO.getSubAddress()))
			.build();
	}

	@GetMapping("/privacyForm")
	public String getForm() {
		return "usr/member/privacy/form";
	}

	@GetMapping("/")
	public String getPrivacy(Model model) {
		final RsData<PrivacyDTO> rsPrivacy = memberPrivacyService.getPrivacy(rq.getMember().getId());
		if (rsPrivacy.isFail()) {
			return rq.historyBack(rsPrivacy);
		}

		model.addAttribute("privacy", decryptPrivacyDTO(rsPrivacy.getData()));

		return "usr/member/privacy/detail";
	}

	@PostMapping("/")
	public String createPrivacy(@RequestBody PrivacyDTO privacyDTO) {
		memberPrivacyService.create(rq.getMember(), privacyDTO);
		return "usr/member/privacy";
	}

	@PutMapping("/")
	public String updatePrivacy(@RequestBody PrivacyDTO privacyDTO) {
		memberPrivacyService.update(rq.getMember(), privacyDTO);
		return "usr/member/privacy";
	}

	@PutMapping("/")
	public String deletePrivacy() {
		memberPrivacyService.delete(rq.getMember().getId());
		return "usr/member/privacy";
	}
}