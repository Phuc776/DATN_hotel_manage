package com.do_issac.hotel_manage.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class KhachSanRequest {
    @NotBlank
    private String tenKhachSan;
    @NotBlank
    private String diaChi;

    private List<String> hinhAnh;
}
