package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiThanhToan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThanhToanResponse {

    private Long id;
    private double soTien;
    private String phuongThuc;
    private TrangThaiThanhToan trangThai;
    private LocalDateTime ngayThanhToan;
}
