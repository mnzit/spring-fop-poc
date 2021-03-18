package com.anup.spring.fop.poc.util;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.fop.apps.*;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class PdfGenerator {

    public void createPdfFile(InputStream configInputStream,
                              InputStream xmlDataFileInputStream,
                              InputStream templateFileInputStream,
                              OutputStream pdfOutputStream
    ) throws IOException {
        System.out.println("Create pdf file ...");
        File tempFile = File.createTempFile("fop-" + System.currentTimeMillis(), ".pdf");

        //  holds references to configuration information and cached data
        //  reuse this instance if you plan to render multiple documents
        //  holds references to configuration information and cached data
        //  reuse this instance if you plan to render multiple documents
//
        try {
            DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
            Configuration cfg = cfgBuilder.build(configInputStream);
            FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(new File(".").toURI()).setConfiguration(cfg);
            FopFactory fopFactory = fopFactoryBuilder.build();


//        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent userAgent = fopFactory.newFOUserAgent();


            // set output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, pdfOutputStream);

            // Load template
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(templateFileInputStream));

            // Set value of parameters in stylesheet
            transformer.setParameter("version", "1.0");


            String body = IOUtils.toString(xmlDataFileInputStream, StandardCharsets.UTF_8.name());

            body = StringEscapeUtils.unescapeJava(body);

            // Input for XSLT transformations
            Source xmlSource = new StreamSource(IOUtils.toInputStream(body));


            Result result = new SAXResult(fop.getDefaultHandler());


            transformer.transform(xmlSource, result);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            tempFile.delete();
        }
    }

    public ByteArrayInputStream createPdfFile(InputStream configInputStream,
                                              InputStream xmlDataFileInputStream,
                                              InputStream templateFileInputStream) {
        System.out.println("Create pdf file ...");
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        //  holds references to configuration information and cached data
        //  reuse this instance if you plan to render multiple documents
        //  holds references to configuration information and cached data
        //  reuse this instance if you plan to render multiple documents
//
        try {
            DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
            Configuration cfg = cfgBuilder.build(configInputStream);
            FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(new File(".").toURI()).setConfiguration(cfg);
            FopFactory fopFactory = fopFactoryBuilder.build();


//        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent userAgent = fopFactory.newFOUserAgent();

            // set output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, pdfOutputStream);

            // Load template
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(templateFileInputStream));

            // Set value of parameters in stylesheet
            transformer.setParameter("version", "1.0");


            String body = IOUtils.toString(xmlDataFileInputStream, StandardCharsets.UTF_8.name());

            body = StringEscapeUtils.unescapeJava(body);

            // Input for XSLT transformations
            Source xmlSource = new StreamSource(IOUtils.toInputStream(body));


            Result result = new SAXResult(fop.getDefaultHandler());


            transformer.transform(xmlSource, result);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return new ByteArrayInputStream((pdfOutputStream.toByteArray()));
    }
}
