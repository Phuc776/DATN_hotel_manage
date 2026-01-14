package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiPhien;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhienLuuTruResponse {
    private Long id;
    private LocalDateTime batDau;
    private LocalDateTime ketThuc;
    private TrangThaiPhien trangThai;
    private String tenKhachSan;
}
