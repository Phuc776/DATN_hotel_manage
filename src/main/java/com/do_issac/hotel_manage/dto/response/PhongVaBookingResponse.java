package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiPhong;
import lombok.Data;

import java.util.List;

@Data
public class PhongVaBookingResponse {
    private Long id;
    private String soPhong;
    private TrangThaiPhong trangThaiPhong;
    private String tenLoaiPhong;
    private List<BookingPhongResponse> bookings;
}
