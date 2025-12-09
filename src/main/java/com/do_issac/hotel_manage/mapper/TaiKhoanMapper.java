package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.RegisterResponse;
import com.do_issac.hotel_manage.dto.response.TaiKhoanResponse;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TaiKhoanMapper {
    TaiKhoanResponse toResponse(TaiKhoan tk);

    @Mapping(source = "vaiTro", target = "vaiTro")
    RegisterResponse toRegisterResponse(TaiKhoan tk);
}
