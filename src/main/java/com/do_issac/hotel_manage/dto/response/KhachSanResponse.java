package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class KhachSanResponse {
    private Long id;
    private String tenKhachSan;
    private String diaChi;
    private String trangThai;
    private TaiKhoanResponse chuKhachSan;
    private List<String> hinhAnh;
}
