package com.do_issac.hotel_manage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Long taiKhoanId;
    private String email;
    private String vaiTro;
    private LocalDateTime ngayTao;

}
