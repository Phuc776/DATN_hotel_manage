package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import com.do_issac.hotel_manage.util.CccdCryptoUtil;
import com.do_issac.hotel_manage.util.FileRenderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RenderDocumentService {
    private final FileRenderUtils fileRenderUtils;
    private final CccdCryptoUtil cccdCryptoUtil;

    public byte[] generateHopDongPdf(ChiTietDatPhong booking) {
        try {
            Map<String, Object> data = new HashMap<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            String decryptedCccd = cccdCryptoUtil.decrypt(booking.getKhachHang().getCCCD());

            data.put("MA_HOP_DONG", booking.getId());
            data.put("NGAY_LAP", LocalDate.now());

            data.put("TEN_KHACH_SAN", booking.getPhong().getKhachSan().getTenKhachSan());
            data.put("DIA_CHI_KHACH_SAN", booking.getPhong().getKhachSan().getDiaChi());
            data.put("SDT_KHACH_SAN", booking.getPhong().getKhachSan().getChuKhachSan().getSoDienThoai());

            data.put("TEN_KHACH_HANG", booking.getKhachHang().getTaiKhoan().getHoTen());
            data.put("EMAIL_KHACH_HANG", booking.getKhachHang().getTaiKhoan().getEmail());
            data.put("SDT_KHACH_HANG", booking.getKhachHang().getTaiKhoan().getSoDienThoai());
            data.put("CCCD", decryptedCccd);

            data.put("TEN_LOAI_PHONG", booking.getPhong().getLoaiPhong().getTenLoaiPhong());
            data.put("SO_PHONG", booking.getPhong().getSoPhong());
            data.put("NGAY_NHAN", booking.getNgayNhan().format(formatter));
            data.put("NGAY_TRA", booking.getNgayTra().format(formatter));
            data.put("SO_NGUOI_LON", booking.getSoNguoiLon());
            data.put("SO_TRE_EM", booking.getSoTreEm());

            data.put("GIA_PHONG", booking.getPhong().getLoaiPhong().getGia());

            // Base64 PDF
            String pdfBase64 = fileRenderUtils.exportPdfBase64(
                    "/templates/hop_dong_thue_phong_template.docx",
                    data
            );

            return Base64.getDecoder().decode(pdfBase64);

        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo hợp đồng thuê phòng", e);
        }
    }
}
