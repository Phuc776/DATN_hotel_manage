package com.do_issac.hotel_manage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateAdminRequest {

    private String hoTen;
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String matKhau;
    private String soDienThoai;
}
