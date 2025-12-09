package com.do_issac.hotel_manage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaiDangPhongRequest {
    @NotNull
    private Long loaiPhongId;
    @NotNull
    private String tieuDe;
    @NotNull
    private String moTa;
    @NotNull
    private Long khachSanId;
    @NotNull
    private Integer soLuongPhong;
}
