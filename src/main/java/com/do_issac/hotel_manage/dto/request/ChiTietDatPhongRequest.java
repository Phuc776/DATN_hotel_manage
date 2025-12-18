package com.do_issac.hotel_manage.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChiTietDatPhongRequest {
    private Long baiDangPhongId;
    @NotBlank
    private LocalDateTime ngayNhan;
    @NotBlank
    private LocalDateTime ngayTra;

    private int soLuongPhongDat;

    private int soNguoiLon;
    private int soTreEm;
    private String ghiChu;
}
