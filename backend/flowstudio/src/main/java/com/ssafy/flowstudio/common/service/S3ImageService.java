package com.ssafy.flowstudio.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new BaseException(ErrorCode.EMPTY_FILE_EXCEPTION);
        }

        if (image.getSize() > 5_242_880) {
            throw new BaseException(ErrorCode.FILE_SIZE_EXCEEDS_LIMIT);
        }

        return this.uploadImage(image);
    }

    public String uploadImage(MultipartFile image) {
        this.validateImageFileExtension(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new BaseException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private void validateImageFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new BaseException(ErrorCode.NO_FILE_EXTENSION);
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtensions.contains(extension)) {
            throw new BaseException(ErrorCode.INVALID_FILE_EXTENSION);
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.PUT_OBJECT_EXCEPTION);
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();

    }


}
