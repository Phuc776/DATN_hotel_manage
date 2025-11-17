package com.do_issac.hotel_manage.dto;

import com.do_issac.hotel_manage.entity.VaiTro;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String matKhau;
    @NotBlank
    private VaiTro vaiTro;
}
