package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.KhachHangResponse;
import com.do_issac.hotel_manage.entity.KhachHang;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KhachHangMapper {
    KhachHangResponse toResponse(KhachHang entity);

    List<KhachHangResponse> toResponseList(List<KhachHang> list);
}
