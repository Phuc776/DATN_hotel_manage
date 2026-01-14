package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.LoaiChiTietHoaDon;
import lombok.Data;

@Data
public class ChiTietHoaDonResponse {
    private LoaiChiTietHoaDon loai;
    private String moTa;
    private double donGia;
    private int soLuong;
    private double thanhTien;
}

