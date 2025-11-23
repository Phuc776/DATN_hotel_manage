package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

@Data
public class DichVuResponse {
    private Long id;
    private String tenDichVu;
    private Double gia;
    private String moTa;
}
