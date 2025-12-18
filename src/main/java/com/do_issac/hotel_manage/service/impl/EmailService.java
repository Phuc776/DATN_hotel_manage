package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.entity.QrKhoaPhong;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final QrKhoaPhongService qrService;

    public void sendCheckInEmail(String to, byte[] hopDongPdf, QrKhoaPhong qr) {
        try {
            // Implement email sending logic here
            // Attach hopDongPdf and include QR code information in the email body
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            byte[] qrImage = qrService.generateQrImage(qr.getMaJWT());


            helper.setTo(to);
            helper.setSubject("Xác nhận nhận phòng");
            helper.setText("""
                    Xin chào,

                    Quý khách đã check-in thành công.
                    Vui lòng sử dụng mã QR đính kèm để mở cửa phòng.

                    Trân trọng.
                    """);
            // pdf attachment
            helper.addAttachment(
                    "hop-dong-thue-phong.pdf",
                    new ByteArrayResource(hopDongPdf)
            );
            // qr code attachment
            helper.addAttachment(
                    "qr-khoa-phong.png",
                    new ByteArrayResource(qrImage)
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi email xác nhận nhận phòng", e);
        }
    }
}
