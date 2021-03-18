package com.anup.spring.fop.poc.controller;

import com.anup.spring.fop.poc.util.PdfGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
