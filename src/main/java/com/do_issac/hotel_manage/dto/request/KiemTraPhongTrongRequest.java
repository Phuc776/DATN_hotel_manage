package com.do_issac.hotel_manage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class KiemTraPhongTrongRequest {
    @NotNull
    private LocalDateTime ngayNhan;
    @NotNull
    private LocalDateTime ngayTra;
    @NotNull
    private Integer soNguoiLon;
    @NotNull
    private Integer soTreEm;
    @NotNull
    private Integer soLuongPhong; // số phòng cần đặt
}
