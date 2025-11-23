package com.do_issac.hotel_manage.dto.request;

import lombok.Data;

@Data
public class LoaiPhongRequest {
    private String tenLoaiPhong;
    private Double gia;
    private int soNguoiLon;
    private int soTreEm;
    private String moTa;
}
