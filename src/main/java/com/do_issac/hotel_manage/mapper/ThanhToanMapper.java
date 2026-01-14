package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.ThanhToanResponse;
import com.do_issac.hotel_manage.entity.ThanhToan;

public class ThanhToanMapper {
    public static ThanhToanResponse toResponse(ThanhToan tt) {
        return new ThanhToanResponse(
                tt.getId(),
                tt.getSoTien(),
                tt.getPhuongThuc(),
                tt.getTrangThai(),
                tt.getNgayThanhToan()
        );
    }
}
