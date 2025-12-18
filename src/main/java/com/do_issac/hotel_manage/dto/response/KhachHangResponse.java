package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KhachHangResponse {
    private Long id;
    private String CCCD;
    private LocalDateTime ngayXacThucCCCD;
    private TaiKhoanResponse taiKhoan;
}
