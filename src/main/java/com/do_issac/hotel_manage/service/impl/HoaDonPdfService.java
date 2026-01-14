package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.HoaDonDetailResponse;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class HoaDonPdfService {

    private final HoaDonTemplateService templateService;

    public byte[] generatePdf(HoaDonDetailResponse hoaDon) {
        try {
            String html = templateService.renderHoaDonHtml(hoaDon);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.useFont(
                    () -> getClass().getClassLoader()
                            .getResourceAsStream("fonts/DejaVuSans.ttf"),
                    "DejaVu Sans"
            );

            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Không thể render PDF hóa đơn", e);
        }
    }
}

