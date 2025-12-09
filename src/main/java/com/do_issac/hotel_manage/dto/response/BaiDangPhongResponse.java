package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiBaiDang;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaiDangPhongResponse {
    private Long id;
    private String tieuDe;
    private String moTa;
    private Integer soLuongPhong;
    private TrangThaiBaiDang trangThaiBaiDang;
    private LoaiPhongResponse loaiPhong;
    private KhachSanResponse khachSan;
    private LocalDateTime ngayDang;
}
