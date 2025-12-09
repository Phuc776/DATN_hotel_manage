package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiPhong;
import lombok.Data;

@Data
public class PhongResponse {
    private Long id;
    private String soPhong;
    private TrangThaiPhong trangThaiPhong;
    private KhachSanResponse khachSan;
    private LoaiPhongResponse loaiPhong;
}
