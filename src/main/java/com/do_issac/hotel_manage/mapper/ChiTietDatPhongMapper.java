package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.ChiTietDatPhongResponse;
import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { HinhAnhMapper.class })
public interface ChiTietDatPhongMapper {
    @Mapping(target = "soPhong", source = "phong.soPhong")
    ChiTietDatPhongResponse toResponse(ChiTietDatPhong chiTietDatPhong);
    @Mapping(target = "soPhong", source = "phong.soPhong")
    List<ChiTietDatPhongResponse> toResponseList(List<ChiTietDatPhong> chiTietDatPhongs);
}
