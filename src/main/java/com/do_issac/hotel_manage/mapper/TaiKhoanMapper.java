package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.RegisterResponse;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TaiKhoanMapper {

    @Mapping(source = "vaiTro", target = "vaiTro")
    @Mapping(source = "khachHang.hoTen", target = "hoTen")
    @Mapping(source = "khachHang.soDienThoai", target = "soDienThoai")
    RegisterResponse toRegisterResponse(TaiKhoan tk);
}
