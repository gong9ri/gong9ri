package com.ll.gong9ri.boundedContext.image.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ll.gong9ri.boundedContext.image.dto.ImageDTO;
import com.ll.gong9ri.boundedContext.image.entity.ChatImage;
import com.ll.gong9ri.boundedContext.image.repository.ChatImageRepository;
import com.ll.gong9ri.boundedContext.member.repository.MemberImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
	private final AmazonS3Client amazonS3Client;
	private final MemberImageRepository memberImageRepository;
	private final ChatImageRepository chatImageRepository;

	@Value("${spring.s3.bucket}")
	private String bucketName;

	@Value("${custom.image.upload-dir}")
	private String uploadDir;

	private String getUniqueFileName(String originalFilename) {
		String extension = FilenameUtils.getExtension(originalFilename);
		return UUID.randomUUID().toString() + "." + extension;
	}

	public List<ImageDTO> uploadImagesSample(List<MultipartFile> multipartFiles){

		return uploadImages(multipartFiles, "sample-folder");

	}

	/**
	 * 네이버 스토리지에 이미지 업로드
	 * @param multipartFiles 이미지 파일
	 * @param filePath 이미지 저장 마지막 경로 : 앞에 '/' 안붙여도 됨
	 * @return List<ImageDTO>
	 */

	public List<ImageDTO> uploadImages(List<MultipartFile> multipartFiles, String filePath) {

		List<ImageDTO> s3Images = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {

			String originalFileName = multipartFile.getOriginalFilename();
			String uploadFileName = getUniqueFileName(originalFileName);
			String uploadFileUrl = "";

			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(multipartFile.getSize());
			objectMetadata.setContentType(multipartFile.getContentType());

			try (InputStream inputStream = multipartFile.getInputStream()) {

				String keyName = filePath + "/" + uploadFileName;

				// S3에 폴더 및 파일 업로드
				amazonS3Client.putObject(
					new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
						.withCannedAcl(CannedAccessControlList.PublicRead));

				// S3에 업로드한 폴더 및 파일 URL
				uploadFileUrl = uploadDir + keyName;

			} catch (IOException e) {
				e.printStackTrace();
			}

			s3Images.add(
				ImageDTO.builder()
					.uploadFileName(uploadFileName)
					.uploadFilePath(filePath)
					.uploadFileUrl(uploadFileUrl)
					.build());
		}

		return s3Images;
	}

	public List<ImageDTO> uploadChatImages(List<MultipartFile> multipartFiles, String chatRoomId) {
		List<ImageDTO> dtos = uploadImages(multipartFiles, "chat" + "/" + chatRoomId);

		ChatImage chatImage = ChatImage.builder()
			.chatRoomId(chatRoomId)
			.filePath(dtos.get(0).getUploadFilePath())
			.fileName(dtos.get(0).getUploadFileName())
			.build();

		chatImageRepository.save(chatImage);

		return dtos;
	}

}
