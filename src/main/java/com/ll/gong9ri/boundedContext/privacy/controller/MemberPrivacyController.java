package com.ll.gong9ri.boundedContext.privacy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	private final MemberEncryptionUtil memberEncryptionUtil;
	private final Rq rq;

	private PrivacyDTO decryptPrivacyDTO(PrivacyDTO privacyDTO) {
		return PrivacyDTO.builder()
			.recipient(memberEncryptionUtil.decrypt(privacyDTO.getRecipient()))
			.phoneNumber(memberEncryptionUtil.decrypt(privacyDTO.getPhoneNumber()))
			.mainAddress(memberEncryptionUtil.decrypt(privacyDTO.getMainAddress()))
			.subAddress(memberEncryptionUtil.decrypt(privacyDTO.getSubAddress()))
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
		PrivacyDTO dto = decryptPrivacyDTO(rsPrivacy.getData());
		System.out.println(dto);

		model.addAttribute("privacy", dto);

		return "usr/member/privacy/detail";
	}

	@PostMapping("/create")
	public String createPrivacy(PrivacyDTO privacyDTO) {
		System.out.println(privacyDTO);
		memberPrivacyService.create(rq.getMember(), privacyDTO);

		return rq.redirectWithMsg("/member/privacy/", "성공적으로 등록했습니다.");
	}

	@PutMapping("/update")
	public String updatePrivacy(@RequestBody PrivacyDTO privacyDTO) {
		memberPrivacyService.update(rq.getMember(), privacyDTO);

		return "usr/member/privacy";
	}

	@DeleteMapping("/delete")
	public String deletePrivacy() {
		memberPrivacyService.delete(rq.getMember().getId());

		return "usr/member/privacy";
	}
}