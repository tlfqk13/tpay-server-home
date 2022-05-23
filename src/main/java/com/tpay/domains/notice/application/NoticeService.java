package com.tpay.domains.notice.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.notice.application.dto.DataList;
import com.tpay.domains.notice.application.dto.AdminNoticeFindDto;
import com.tpay.domains.notice.domain.NoticeEntity;
import com.tpay.domains.notice.domain.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private static final Integer S3_BASE_URI_LENGTH = 50;
    private final S3FileUploader s3FileUploader;
    private final NoticeRepository noticeRepository;

    @Transactional
    public void registration(String dataListString, MultipartFile mainImg, MultipartFile subImg1, MultipartFile subImg2, MultipartFile subImg3) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            DataList dataList = objectMapper.readValue(dataListString, DataList.class);
            NoticeEntity noticeEntity = new NoticeEntity(dataList);
            NoticeEntity noticeEntityAfterSave = noticeRepository.save(noticeEntity);

            nullOrUpload(noticeEntityAfterSave, "mainImg", mainImg);
            nullOrUpload(noticeEntityAfterSave, "subImg1", subImg1);
            nullOrUpload(noticeEntityAfterSave, "subImg2", subImg2);
            nullOrUpload(noticeEntityAfterSave, "subImg3", subImg3);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void nullOrUpload(NoticeEntity noticeEntity, String fileName, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            String result = s3FileUploader.uploadNotice(noticeEntity.getId(), multipartFile, fileName);
            noticeEntity.setImages(fileName, result.substring(S3_BASE_URI_LENGTH));
        }
    }

    public List<AdminNoticeFindDto.FindAllResponse> getAll() {
        List<NoticeEntity> noticeEntityList = noticeRepository.findAll();
        return noticeEntityList.stream().map(AdminNoticeFindDto.FindAllResponse::new).collect(Collectors.toList());
    }


    public AdminNoticeFindDto.FindOneResponse getOne(Long noticeIndex) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Notice Index"));
        return new AdminNoticeFindDto.FindOneResponse(noticeEntity);
    }

    @Transactional
    public void updateInvisible(Long noticeIndex) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Notice Index"));
        noticeEntity.updateInvisible();
    }

    @Transactional
    public void deleteNotification(Long noticeIndex) {
        s3FileUploader.deleteNotice(noticeIndex);
        noticeRepository.deleteById(noticeIndex);
    }
}
