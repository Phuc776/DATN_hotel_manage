package com.do_issac.hotel_manage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String vaiTro;
    private TaiKhoanResponse taiKhoan;
}
