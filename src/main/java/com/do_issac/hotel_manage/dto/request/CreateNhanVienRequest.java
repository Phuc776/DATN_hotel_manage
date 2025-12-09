package com.do_issac.hotel_manage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNhanVienRequest {
    @NotBlank
    private String hoTen;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String soDienThoai;

    private String chucVu;

    @NotNull
    private Long khachSanId;
}
