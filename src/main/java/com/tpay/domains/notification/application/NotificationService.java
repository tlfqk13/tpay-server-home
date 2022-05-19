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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Integer S3_BASE_URI_LENGTH = 50;
    private final S3FileUploader s3FileUploader;
    private final NotificationRepository notificationRepository;
    private static final Map<String, MultipartFile> multipartFiles = new LinkedHashMap<>();
    private static final List<String> multipartFileNames = new ArrayList<>();

    @Transactional
    public void registration(String dataListString, MultipartFile mainImg, MultipartFile subImg1, MultipartFile subImg2, MultipartFile subImg3) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            DataList dataList = objectMapper.readValue(dataListString, DataList.class);
            NotificationEntity notificationEntity = new NotificationEntity(dataList);
            NotificationEntity notificationEntityAfterSave = notificationRepository.save(notificationEntity);
            Long notificationIndex = notificationEntityAfterSave.getId();

            nullOrPut("mainImg", mainImg);
            nullOrPut("subImg1", subImg1);
            nullOrPut("subImg2", subImg2);
            nullOrPut("subImg3", subImg3);

            if (multipartFiles.size() == 0) {
                return;
            }

            Map<String, String> s3Paths = s3FileUploader.uploadNotifications(notificationIndex, multipartFiles, multipartFileNames);
            for (String multipartFileName : multipartFileNames) {
                notificationEntityAfterSave.setImages(multipartFileName, s3Paths.get(multipartFileName).substring(S3_BASE_URI_LENGTH));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void nullOrPut(String key, MultipartFile multipartFile) {
        if (multipartFile != null) {
            multipartFiles.put(key, multipartFile);
            multipartFileNames.add(key);
        }
    }

}
