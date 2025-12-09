package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

@Data
public class NhanVienResponse {
    private Long id;
    private String chucVu;
    private KhachSanResponse khachSan;
    private TaiKhoanResponse taiKhoan;
}
