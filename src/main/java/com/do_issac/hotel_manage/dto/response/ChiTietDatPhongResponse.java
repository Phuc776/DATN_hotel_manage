package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiDatPhong;
import com.do_issac.hotel_manage.entity.TrangThaiPhong;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChiTietDatPhongResponse {
    private Long id;
    private String soPhong;
    private LocalDateTime ngayNhan;
    private LocalDateTime ngayTra;
    private PhongResponse phong;
    private int soNguoiLon;
    private int soTreEm;
    private String ghiChu;
    private TrangThaiDatPhong trangThai;
    private KhachHangResponse khachHang;
}

