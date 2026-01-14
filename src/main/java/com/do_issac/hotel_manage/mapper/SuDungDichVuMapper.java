package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.SuDungDichVuResponse;
import com.do_issac.hotel_manage.entity.SuDungDichVu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SuDungDichVuMapper {

    @Mapping(target = "tenDichVu", source = "dichVu.tenDichVu")
    @Mapping(target = "thanhTien",
            expression = "java(suDung.getSoLuong() * suDung.getDonGiaTaiThoiDiem())")
    SuDungDichVuResponse toResponse(SuDungDichVu suDung);

    List<SuDungDichVuResponse> toResponseList(List<SuDungDichVu> suDungDichVus);
}

