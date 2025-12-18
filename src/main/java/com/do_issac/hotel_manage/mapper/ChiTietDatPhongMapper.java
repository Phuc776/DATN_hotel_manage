package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.ChiTietDatPhongResponse;
import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChiTietDatPhongMapper {
    ChiTietDatPhongResponse toResponse(ChiTietDatPhong chiTietDatPhong);

    List<ChiTietDatPhongResponse> toResponseList(List<ChiTietDatPhong> chiTietDatPhongs);
}
