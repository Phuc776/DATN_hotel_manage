package com.do_issac.hotel_manage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

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

    private List<String> hinhAnh;
}
