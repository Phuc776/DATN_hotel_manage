package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.request.DichVuRequest;
import com.do_issac.hotel_manage.dto.response.DichVuResponse;
import com.do_issac.hotel_manage.entity.DichVu;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DichVuMapper {

    DichVu toEntity(DichVuRequest request);

    DichVuResponse toResponse(DichVu entity);

    List<DichVuResponse> toResponseList(List<DichVu> list);
}

