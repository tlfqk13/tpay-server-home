package com.tpay.domains.notice.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.notice.application.dto.CommonNoticeFindDto;
import com.tpay.domains.notice.application.dto.AppNoticeFindDto;
import com.tpay.domains.notice.application.dto.DataList;
import com.tpay.domains.notice.application.dto.InvisibleUpdateDto;
import com.tpay.domains.notice.domain.NoticeEntity;
import com.tpay.domains.notice.domain.NoticeRepository;
import com.tpay.domains.push.application.dto.PushNoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
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

    public List<CommonNoticeFindDto.FindAllResponse> getAll() {
        List<NoticeEntity> noticeEntityList = noticeRepository.findAll(Sort.by("id").descending());
        return noticeEntityList.stream().map(CommonNoticeFindDto.FindAllResponse::new).collect(Collectors.toList());
    }


    public CommonNoticeFindDto.FindOneResponse getOne(Long noticeIndex) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Notice Index"));
        return new CommonNoticeFindDto.FindOneResponse(noticeEntity);
    }

    @Transactional
    public void updateInvisible(Long noticeIndex, InvisibleUpdateDto invisibleUpdateDto) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Notice Index"));
        noticeEntity.updateInvisible(invisibleUpdateDto.getIsInvisible());
    }

    @Transactional
    public void deleteNotification(Long noticeIndex) {
        s3FileUploader.deleteNotice(noticeIndex);
        noticeRepository.deleteById(noticeIndex);
    }

    public AppNoticeFindDto.FindAllResponse getAllApp() {
        List<NoticeEntity> noticeEntityTrueList = noticeRepository.findByIsFixedAndScheduledDateBeforeAndIsInvisible(Boolean.TRUE, LocalDateTime.now(),Boolean.FALSE);
        List<NoticeEntity> noticeEntityFalseList = noticeRepository.findByIsFixedAndScheduledDateBeforeAndIsInvisible(Boolean.FALSE, LocalDateTime.now(),Boolean.FALSE);
        List<CommonNoticeFindDto.FindAllResponse> trueCollect = noticeEntityTrueList.stream().map(CommonNoticeFindDto.FindAllResponse::new).collect(Collectors.toList());
        List<CommonNoticeFindDto.FindAllResponse> falseCollect = noticeEntityFalseList.stream().map(CommonNoticeFindDto.FindAllResponse::new).collect(Collectors.toList());
        return new AppNoticeFindDto.FindAllResponse(trueCollect,falseCollect);
    }

    public CommonNoticeFindDto.FindOneResponse getOneApp(Long noticeIndex) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Notice Index"));
        return new CommonNoticeFindDto.FindOneResponse(noticeEntity);
    }

    @Transactional
    public void updateNotice(Long noticeIndex, DataList dataList) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Parameter"));
        noticeEntity.updateNotice(dataList, LocalDateTime.now());
    }

    public PushNoticeDto findAllNotice() {
        List<NoticeEntity> noticeEntityList = noticeRepository.findByScheduledDateBeforeAndIsInvisible(LocalDateTime.now(), Boolean.FALSE);
        List<PushNoticeDto.PushNoticeResponse> collect = noticeEntityList.stream().map(PushNoticeDto.PushNoticeResponse::new).collect(Collectors.toList());
        return new PushNoticeDto(collect);
    }
}
