package com.do_issac.hotel_manage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThongBaoResponse {
    private Long id;
    private String noiDung;
    private LocalDateTime ngayTao;
    private boolean daXem;
    private Long taiKhoanId;
}
