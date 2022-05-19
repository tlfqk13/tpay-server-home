package com.tpay.domains.notification.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.aws.S3FileUploader;
import com.tpay.domains.notification.application.dto.DataList;
import com.tpay.domains.notification.domain.NotificationEntity;
import com.tpay.domains.notification.domain.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Integer S3_BASE_URI_LENGTH = 50;
    private final S3FileUploader s3FileUploader;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void registration(String dataListString, MultipartFile mainImg, MultipartFile subImg1, MultipartFile subImg2, MultipartFile subImg3) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            DataList dataList = objectMapper.readValue(dataListString, DataList.class);
            NotificationEntity notificationEntity = new NotificationEntity(dataList);
            NotificationEntity notificationEntityAfterSave = notificationRepository.save(notificationEntity);

            nullOrUpload(notificationEntityAfterSave, "mainImg", mainImg);
            nullOrUpload(notificationEntityAfterSave, "subImg1", subImg1);
            nullOrUpload(notificationEntityAfterSave, "subImg2", subImg2);
            nullOrUpload(notificationEntityAfterSave, "subImg3", subImg3);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void nullOrUpload(NotificationEntity notificationEntity, String fileName, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            String result = s3FileUploader.uploadNotification(notificationEntity.getId(), multipartFile, fileName);
            notificationEntity.setImages(fileName, result.substring(S3_BASE_URI_LENGTH));
        }
    }

}
