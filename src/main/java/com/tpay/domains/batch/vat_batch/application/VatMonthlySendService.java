package com.tpay.domains.batch.vat_batch.application;

import com.tpay.domains.order.application.CmsService;
import com.tpay.domains.refund.application.RefundDetailFindService;
import com.tpay.domains.vat.application.VatDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.tpay.commons.util.KtpCommonUtil.isApplicationInitBeforeOneMinute;

@Slf4j
@Service
@RequiredArgsConstructor
public class VatMonthlySendService {

    private final VatDownloadService vatDownloadService;
    private final CmsService cmsService;
    private final RefundDetailFindService refundDetailFindService;
    @Transactional
    @Scheduled(cron = "0 1 01 15 * *")
    public void vatMonthlyFile() {
        // TODO: 2022/07/11 매월 15일 새벽 1시에 시작되는 vatMonthly 파일 만들기
        String requestYear = String.valueOf(LocalDate.now().getYear()).substring(2);
        String requestMonthly = String.valueOf(LocalDate.now().getMonthValue());
        String requestYearMonthly = requestYear + requestMonthly;

        List<String> date = cmsService.setUpDate(requestYearMonthly);
        String year = date.get(0);
        String month = date.get(1);
        List<List<String>> totalResult = refundDetailFindService.findFranchiseeId(year, month);

        if(isApplicationInitBeforeOneMinute()){
            log.trace("First Call - vatMonthlyFile");
        }else if(totalResult.isEmpty()){
            log.trace("TotalResult is Nothing");
        }else{
            for (List<String> strings : totalResult) {
                vatDownloadService.vatMonthlySendMailFile(Long.valueOf(strings.get(0)), requestYearMonthly);
            }
        }
    }
}
