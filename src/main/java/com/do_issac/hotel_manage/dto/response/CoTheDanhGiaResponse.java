package com.do_issac.hotel_manage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoTheDanhGiaResponse {
    private Long khachSanId;
    private String tenKhachSan;
    private boolean daDanhGia;
}
