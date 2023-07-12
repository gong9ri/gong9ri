package com.ll.gong9ri.boundedContext.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRecipientDTO {
	@NotBlank
	private String recipient;
	@Pattern(
			regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$",
			message = "10 ~ 11 자리의 숫자만 입력 가능합니다."
	)
	private String phoneNumber;
	@NotBlank
	private String mainAddress;
	private String subAddress;
}
