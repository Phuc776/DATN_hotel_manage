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

    public void sendHoaDonEmail(String to, byte[] hoaDonPdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Hóa đơn lưu trú");
            helper.setText("""
                Xin chào,

                Phiên lưu trú của quý khách đã kết thúc.
                Vui lòng xem hóa đơn đính kèm.

                Xin cảm ơn và hẹn gặp lại.
                """);

            helper.addAttachment(
                    "hoa-don-luu-tru.pdf",
                    new ByteArrayResource(hoaDonPdf)
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi email hóa đơn", e);
        }
    }

}
