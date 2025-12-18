package com.do_issac.hotel_manage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class KiemTraPhongTrongRequest {
    private LocalDateTime ngayNhan;
    private LocalDateTime ngayTra;
}
