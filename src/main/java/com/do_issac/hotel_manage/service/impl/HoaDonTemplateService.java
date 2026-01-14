package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.HoaDonDetailResponse;
import com.do_issac.hotel_manage.entity.LoaiChiTietHoaDon;
import com.openhtmltopdf.resource.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class HoaDonTemplateService {
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String renderHoaDonHtml(HoaDonDetailResponse hd) {
        try {
            String template = loadTemplate("templates/hoa-don.html");

            template = template
                    .replace("{{tenKhachSan}}", hd.getTenKhachSan())
                    .replace("{{tenKhachHang}}", hd.getTenKhachHang())
                    .replace("{{ngayTao}}", hd.getNgayTao().format(DATE_TIME_FMT))
                    .replace("{{tongTien}}", formatMoney(hd.getTongTien()))
                    .replace("{{CHI_TIET}}", buildChiTietHtml(hd));
            return template;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi render HTML hóa đơn", e);
        }
    }

    private String loadTemplate(String path) throws IOException {
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (is == null) {
                throw new FileNotFoundException("Không tìm thấy file: " + path);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String buildChiTietHtml(HoaDonDetailResponse hd) {
        StringBuilder sb = new StringBuilder();

        hd.getChiTietHoaDons().forEach(ct -> {
            sb.append("""
                <tr>
                    <td>%s</td>
                    <td>%s</td>
                    <td style="text-align:center">%d</td>
                    <td style="text-align:right">%s</td>
                    <td style="text-align:right">%s</td>
                </tr>
            """.formatted(
                    mapLoaiChiTiet(ct.getLoai()),
                    ct.getMoTa(),
                    ct.getSoLuong(),
                    formatMoney(ct.getDonGia()),
                    formatMoney(ct.getThanhTien())
                        )
                    );
        });

        return sb.toString();
    }

    private String formatMoney(Double value) {
        return String.format("%,.0f VNĐ", value);
    }
    private String mapLoaiChiTiet(LoaiChiTietHoaDon loai) {
        return switch (loai) {
            case TIEN_PHONG -> "Tiền phòng";
            case DICH_VU -> "Dịch vụ";
        };
    }

}

