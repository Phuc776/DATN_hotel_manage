package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.NhanVienResponse;
import com.do_issac.hotel_manage.entity.NhanVien;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NhanVienMapper {
    NhanVienResponse toResponse(NhanVien nhanVien);
}
