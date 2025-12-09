package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.BaiDangPhongResponse;
import com.do_issac.hotel_manage.entity.BaiDangPhong;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BaiDangPhongMapper {
    BaiDangPhongResponse toResponse(BaiDangPhong bdp);

    List<BaiDangPhongResponse> toResponseList(List<BaiDangPhong> list);
}
