package com.tpay.domains.pos.application;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static com.tpay.commons.custom.CustomValue.BARCODE_SAVE_PATH;

@Component
@Slf4j
public class BarcodeService {

    public ResponseEntity<Resource> createResource(String deductionPadding, String idPadding, String filename) {
        Resource resource = new FileSystemResource(BARCODE_SAVE_PATH);
        HttpHeaders headers = new HttpHeaders();
        try {
            Barcode barcode = BarcodeFactory.createCode128A(idPadding + deductionPadding);
            barcode.setBarHeight(100);
            filename = LocalDateTime.now() + "_" + filename + ".png";
            File file = new File(BARCODE_SAVE_PATH + filename);
            BarcodeImageHandler.savePNG(barcode, file);
            resource = new FileSystemResource(BARCODE_SAVE_PATH + filename);
            Path filePath = Paths.get(BARCODE_SAVE_PATH + filename);
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (OutputException | BarcodeException | IOException e) {
            log.error("Barcode Create Error : {}", e.getMessage());
        }
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

}
