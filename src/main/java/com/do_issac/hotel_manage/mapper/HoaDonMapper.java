package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.ChiTietHoaDonResponse;
import com.do_issac.hotel_manage.dto.response.HoaDonDetailResponse;
import com.do_issac.hotel_manage.dto.response.HoaDonResponse;
import com.do_issac.hotel_manage.entity.ChiTietHoaDon;
import com.do_issac.hotel_manage.entity.HoaDon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HoaDonMapper {

    @Mapping(target = "tenKhachSan",
            expression = "java(layTenKhachSan(hd))")
    @Mapping(target = "tenKhachHang",
            expression = "java(hd.getPhienLuuTru().getKhachHang().getTaiKhoan().getHoTen())")
    HoaDonResponse toResponse(HoaDon hd);

    List<HoaDonResponse> toResponseList(List<HoaDon> list);

    @Mapping(target = "tenKhachSan",
            expression = "java(layTenKhachSan(hd))")
    @Mapping(target = "tenKhachHang",
            expression = "java(hd.getPhienLuuTru().getKhachHang().getTaiKhoan().getHoTen())")
    @Mapping(target = "chiTietHoaDons",
            source = "chiTietHoaDons")
    HoaDonDetailResponse toDetailResponse(HoaDon hd);

    @Mapping(target = "thanhTien",
            expression = "java(ct.getDonGia() * ct.getSoLuong())")
    ChiTietHoaDonResponse toChiTietResponse(ChiTietHoaDon ct);

    List<ChiTietHoaDonResponse> toChiTietResponseList(List<ChiTietHoaDon> list);

    // helper
    default String layTenKhachSan(HoaDon hd) {
        return hd.getPhienLuuTru()
                .getDatPhongs()
                .isEmpty()
                ? null
                : hd.getPhienLuuTru()
                .getDatPhongs()
                .get(0)
                .getPhong()
                .getKhachSan()
                .getTenKhachSan();
    }
}

