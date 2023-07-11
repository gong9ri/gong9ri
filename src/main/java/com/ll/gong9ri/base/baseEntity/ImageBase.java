package com.ll.gong9ri.base.baseEntity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ll.gong9ri.base.appConfig.AppConfig;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ImageBase extends BaseEntity{
	private String filePath;
	private String fileName;
	// TODO: write authorites
	// TODO: read authotites

	public String getURL() {
		return AppConfig.getDefaultImageUploadURL() + filePath + "/" + fileName;
	}
}
