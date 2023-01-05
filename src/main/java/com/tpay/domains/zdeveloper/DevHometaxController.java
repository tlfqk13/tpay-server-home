package com.tpay.domains.zdeveloper;

import com.tpay.domains.vat.application.VatHomeTaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/dev/hometax")
@RequiredArgsConstructor
public class DevHometaxController {

    private final VatHomeTaxService homeTaxService;

    @GetMapping("/downloads/{franchiseeId}")
    public ResponseEntity<String> homeTaxFile(
            @RequestParam String requestDate
            , @PathVariable String franchiseeId) throws IOException {
        homeTaxService.homeTaxAdminDownloads(franchiseeId, requestDate);
        return ResponseEntity.ok("Admin homeTaxFile");
    }
}
