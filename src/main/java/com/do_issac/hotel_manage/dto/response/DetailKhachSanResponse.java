package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

@Data
public class DetailKhachSanResponse {
    private Long id;
    private String tenKhachSan;
    private String diaChi;
    private String trangThai;
    private TaiKhoanResponse chuKhachSan;
    private int soPhongKinhDoanh;
    private int soNhanVien;
    private int soBaiDangPhong;
    private double danhGiaTrungBinh;
}
