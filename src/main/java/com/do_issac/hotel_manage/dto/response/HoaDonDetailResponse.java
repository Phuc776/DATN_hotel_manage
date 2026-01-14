package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HoaDonDetailResponse {
    private Long id;
    private LocalDateTime ngayTao;
    private double tongTien;
    private String noiDung;

    private String tenKhachSan;
    private String tenKhachHang;

    private List<ChiTietHoaDonResponse> chiTietHoaDons;
}

