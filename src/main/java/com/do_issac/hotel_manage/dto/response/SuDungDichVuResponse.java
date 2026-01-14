package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SuDungDichVuResponse {
    private String tenDichVu;
    private int soLuong;
    private double donGiaTaiThoiDiem;
    private LocalDateTime thoiDiemSuDung;

    private double thanhTien;
}
