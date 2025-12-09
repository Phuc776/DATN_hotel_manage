package com.do_issac.hotel_manage.dto.request;

import com.do_issac.hotel_manage.entity.VaiTro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    private String email;
    @NotBlank
    private String matKhau;
    @NotBlank
    private VaiTro vaiTro;
    @NotBlank
    private String hoTen;
    @NotBlank
    private String soDienThoai;
}
