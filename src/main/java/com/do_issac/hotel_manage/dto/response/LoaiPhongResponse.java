package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

@Data
public class LoaiPhongResponse {
    private Long id;
    private String tenLoaiPhong;
    private Double gia;
    private int soLuongCon;
    private int soNguoiLon;
    private int soTreEm;
    private String moTa;
}

