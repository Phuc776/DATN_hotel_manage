package com.do_issac.hotel_manage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DanhGiaCuaToiResponse {

    private Long id;

    private int diemDanhGia;
    private String noiDung;

    private Long khachSanId;
    private String tenKhachSan;

    private LocalDateTime thoiGianDanhGia;
}

