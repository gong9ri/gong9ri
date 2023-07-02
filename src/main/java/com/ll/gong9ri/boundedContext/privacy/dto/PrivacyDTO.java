package com.ll.gong9ri.boundedContext.privacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyDTO {
	private String recipient;
	private String phoneNumber;
	private String mainAddress;
	private String subAddress;
}
