package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.KhachSanResponse;
import com.do_issac.hotel_manage.entity.KhachSan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KhachSanMapper {
    @Mapping(source = "chuKhachSan.id", target = "chuKhachSanId")
    @Mapping(source = "chuKhachSan.email", target = "emailChuKhachSan")
    KhachSanResponse toResponse(KhachSan ks);

    List<KhachSanResponse> toResponseList(List<KhachSan> list);
}
