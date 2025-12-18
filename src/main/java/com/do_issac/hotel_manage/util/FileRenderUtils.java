package com.do_issac.hotel_manage.util;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;


@Component
public class FileRenderUtils {

    public String exportPdfBase64(String templatePath, Map<String, Object> data) throws Exception {
        File wordFile = generateWordFromTemplate(templatePath, data);
        File pdfFile = convertDocxToPdf(wordFile);
        try {
            byte[] fileBytes = Files.readAllBytes(pdfFile.toPath());
            return Base64.getEncoder().encodeToString(fileBytes);
        } finally {
            safeDelete(wordFile);
            safeDelete(pdfFile);
        }
    }

    public File generateWordFromTemplate(String templatePath, Map<String, Object> data) throws Exception {
        try (InputStream templateFile = getClass().getResourceAsStream(templatePath)) {
            if (templateFile == null) {
                throw new FileNotFoundException("Template not found: " + templatePath);
            }

            IXDocReport report = XDocReportRegistry.getRegistry()
                    .loadReport(templateFile, TemplateEngineKind.Freemarker);

            IContext context = report.createContext();
            data.forEach(context::put);
            File wordFile = File.createTempFile("xdoc_", ".docx");
            try (OutputStream out = new FileOutputStream(wordFile)) {
                report.process(context, out);
            }

            return wordFile;
        }
    }

    public File convertDocxToPdf(File wordFile) throws Exception {
        File pdfFile = File.createTempFile("xdoc_", ".pdf");
        try (InputStream docxInputStream = new FileInputStream(wordFile);
             OutputStream pdfOutputStream = new FileOutputStream(pdfFile)) {

            XWPFDocument document = new XWPFDocument(docxInputStream);
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(document, pdfOutputStream, options);
        }
        return pdfFile;
    }


    private void safeDelete(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}

