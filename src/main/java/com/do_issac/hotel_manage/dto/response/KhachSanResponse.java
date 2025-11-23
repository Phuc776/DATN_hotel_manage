package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

@Data
public class KhachSanResponse {
    private Long id;
    private String tenKhachSan;
    private String diaChi;
    private String trangThai;
    private Long chuKhachSanId;
    private String emailChuKhachSan;
}
