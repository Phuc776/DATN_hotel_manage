package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HoaDonResponse {
    private Long id;
    private LocalDateTime ngayTao;
    private double tongTien;

    // để hiển thị ngữ cảnh
    private String tenKhachSan;
    private String tenKhachHang; // nhân viên / chủ KS cần
}

