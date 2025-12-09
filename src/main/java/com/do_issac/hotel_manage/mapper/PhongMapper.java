package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.PhongResponse;
import com.do_issac.hotel_manage.entity.Phong;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhongMapper {
    PhongResponse toResponse(Phong entity);

    List<PhongResponse> toResponseList(List<Phong> list);
}
