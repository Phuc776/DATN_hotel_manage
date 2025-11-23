package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.request.LoaiPhongRequest;
import com.do_issac.hotel_manage.dto.response.LoaiPhongResponse;
import com.do_issac.hotel_manage.entity.LoaiPhong;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoaiPhongMapper {

    LoaiPhong toEntity(LoaiPhongRequest request);

    LoaiPhongResponse toResponse(LoaiPhong entity);

    List<LoaiPhongResponse> toResponseList(List<LoaiPhong> list);
}

