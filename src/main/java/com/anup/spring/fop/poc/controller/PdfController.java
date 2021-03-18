package com.anup.spring.fop.poc.controller;

import com.anup.spring.fop.poc.util.PdfGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("download")
public class PdfController {

    private final PdfGenerator pdfGenerator;

    public PdfController(PdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    @GetMapping("pdf")
    public String downloadPdf() {

        String userDirectory = System.getProperty("user.dir");
        String outputFile = userDirectory + File.separator + "poc.pdf";

        try {
            InputStream configInputStream = new ClassPathResource("conf" + File.separator + "fop.xconf").getInputStream();
            InputStream dataInputStream = new ClassPathResource("data" + File.separator + "cars.xml").getInputStream();
            InputStream styleFileInputStream = new ClassPathResource("xslt" + File.separator + "cars-template.xsl").getInputStream();

            try (FileOutputStream pdfOutput = new FileOutputStream(outputFile)) {
                pdfGenerator.createPdfFile(configInputStream, dataInputStream, styleFileInputStream, pdfOutput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "PDF Created";
    }

    @RequestMapping(
            value = "pdf/v2",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<InputStreamResource> downloadPdfV2() throws Exception {
        String fileName = "plain.pdf";

        InputStream configInputStream = new ClassPathResource("conf" + File.separator + "fop.xconf").getInputStream();
        InputStream dataInputStream = new ClassPathResource("data" + File.separator + "cars.xml").getInputStream();
        InputStream templateFileInputStream = new ClassPathResource("xslt" + File.separator + "cars-template.xsl").getInputStream();

        final ByteArrayInputStream byteArrayInputStream = pdfGenerator
                .createPdfFile(configInputStream, dataInputStream, templateFileInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }
}
