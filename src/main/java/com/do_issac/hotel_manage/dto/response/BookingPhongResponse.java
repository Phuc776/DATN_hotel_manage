package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiDatPhong;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingPhongResponse {
    private Long bookingId;

    private LocalDateTime ngayNhan;
    private LocalDateTime ngayTra;

    private String tenKhachHang;
    private String soDienThoai;

    private TrangThaiDatPhong trangThai;
}
