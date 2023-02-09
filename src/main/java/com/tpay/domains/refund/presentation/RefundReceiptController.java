package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundBarcodeService;
import com.tpay.domains.refund.application.RefundReceiptDownloadsService;
import com.tpay.domains.refund.application.RefundReceiptFindService;
import com.tpay.domains.refund.application.RefundReceiptUploadService;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/refund/receipt")
public class RefundReceiptController {

    private final RefundReceiptFindService refundReceiptFindService;
    private final RefundReceiptDownloadsService refundReceiptDownloadsService;
    private final RefundReceiptUploadService refundReceiptUploadService;
    private final RefundBarcodeService refundBarcodeService;

    @PostMapping("/home")
    public ResponseEntity<RefundReceiptDto.ResponseCustomer> receiptHome(
            @RequestBody RefundReceiptDto.Request request
    ){
        RefundReceiptDto.ResponseCustomer responseCustomer = refundReceiptFindService.findCustomer(request);
        return ResponseEntity.ok(responseCustomer);
    }

    @PostMapping("/passport-validate") // 여권번호를 입력해주세요
    public ResponseEntity<RefundReceiptDto.ResponseCustomer> findCustomer(
            @RequestBody RefundReceiptDto.Request request
    ){
        RefundReceiptDto.ResponseCustomer responseCustomer = refundReceiptFindService.findCustomer(request);
        return ResponseEntity.ok(responseCustomer);
    }

    @PutMapping("/departure")//출국 예정일을 입력해주세요
    public ResponseEntity<RefundReceiptDto.ResponseCustomer> updateDepartureDate(
            @RequestBody RefundReceiptDto.Request request
    ){
        RefundReceiptDto.ResponseCustomer responseCustomer = refundReceiptFindService.updateDepartureDate(request);
        return ResponseEntity.ok(responseCustomer);
    }

    @PostMapping("/upload/{refundIndex}")//영수증 이미지 업로드
    public ResponseEntity<String> uploadRefundReceiptImage(
            @PathVariable Long refundIndex,
            @RequestParam MultipartFile uploadImage
    ){
        String s3Path = refundReceiptUploadService.uploadReceiptImage(refundIndex,uploadImage);
        return ResponseEntity.ok(s3Path);
    }

    @PatchMapping("/upload/{refundIndex}")//영수증 이미지 업로드 수정
    public ResponseEntity<String> updateRefundReceiptImage(
            @PathVariable Long refundIndex,
            @RequestParam MultipartFile uploadImage
    ){
        String s3Path = refundReceiptUploadService.updateReceiptImage(refundIndex,uploadImage);
        return ResponseEntity.ok(s3Path);
    }

    @DeleteMapping("/upload/{refundIndex}")//영수증 이미지 업로드 삭제
    public ResponseEntity<String> deleteRefundReceiptImage(
            @PathVariable Long refundIndex
    ){
        refundReceiptUploadService.deleteReceiptImage(refundIndex);
        return ResponseEntity.ok("delete");
    }

    @PostMapping("/list")//영수증 이미지 업로드 리스트
    public ResponseEntity<List<RefundReceiptDto.RefundReceiptUploadListDto>> findRefundReceiptUploadList(
            @RequestBody RefundReceiptDto.Request request
    ) {
        List<RefundReceiptDto.RefundReceiptUploadListDto> refundReceiptUploadList = refundReceiptFindService.findRefundReceiptUploadList(request);
        return ResponseEntity.ok(refundReceiptUploadList);
    }

    @PostMapping
    public ResponseEntity<List<RefundReceiptDto.Response>> findRefundReceipt(
            @RequestBody RefundReceiptDto.Request request
    ) {
        List<RefundReceiptDto.Response> refundReceiptList = refundReceiptFindService.findRefundReceiptDetail(request);
        return ResponseEntity.ok(refundReceiptList);
    }

    @PostMapping("/downloads")
    public ResponseEntity<String> downloadsRefundReceipt(
            @RequestBody RefundReceiptDto.Request request
    ) {
        refundReceiptDownloadsService.downloadsRefundReceipt(request);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/barcode/{orderIndex}")
    public ResponseEntity<String> refundBarcode(
            @PathVariable Long orderIndex
    ) {
        refundBarcodeService.saveAndCreateBarcode(orderIndex);
        return ResponseEntity.ok("ok");
    }
}
