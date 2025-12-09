package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.ThongBaoResponse;
import com.do_issac.hotel_manage.entity.ThongBao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThongBaoMapper {
    @Mapping(source = "taiKhoan.id", target = "taiKhoanId")
    ThongBaoResponse toResponse(ThongBao tb);

    List<ThongBaoResponse> toResponseList(List<ThongBao> list);
}

