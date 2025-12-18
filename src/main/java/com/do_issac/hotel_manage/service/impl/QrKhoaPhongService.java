package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import com.do_issac.hotel_manage.entity.QrKhoaPhong;
import com.do_issac.hotel_manage.entity.TrangThaiDatPhong;
import com.do_issac.hotel_manage.jwt.QrJwtProvider;
import com.do_issac.hotel_manage.repository.QRKhoaPhongRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrKhoaPhongService {

    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    private final QRKhoaPhongRepository qrRepo;
    private final QrJwtProvider qrJwtProvider;

    public QrKhoaPhong generateQrForBooking(ChiTietDatPhong b) {
        if (b.getTrangThai() != TrangThaiDatPhong.DA_XAC_NHAN) {
            throw new RuntimeException("Booking chưa sẵn sàng tạo QR");
        }

        QrKhoaPhong qr = new QrKhoaPhong();

        qr.setDatPhong(b);
        qr.setTrangThai("HOAT_DONG");
        qr.setThoiGianTao(LocalDateTime.now());
        qr.setHieuLucTu(b.getNgayNhan());
        qr.setHieuLucDen(b.getNgayTra());

        qr = qrRepo.save(qr);

        String jwt = qrJwtProvider.generateQrToken(qr);
        qr.setMaJWT(jwt);

        return qrRepo.save(qr);
    }

    public byte[] generateQrImage(String jwt) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); // viền QR

            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    jwt,
                    BarcodeFormat.QR_CODE,
                    QR_WIDTH,
                    QR_HEIGHT,
                    hints
            );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo QR code", e);
        }
    }

    public void invalidateQr(QrKhoaPhong qr) {
        qr.setTrangThai("VO_HIEU_LUC");
        qrRepo.save(qr);
    }
}
