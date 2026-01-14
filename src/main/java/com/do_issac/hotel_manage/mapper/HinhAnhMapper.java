package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.entity.HinhAnh;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HinhAnhMapper {

    default String map(HinhAnh hinhAnh) {
        return hinhAnh == null ? null : hinhAnh.getImageUrl();
    }

    default List<String> map(List<HinhAnh> hinhAnhs) {
        if (hinhAnhs == null) return List.of();
        return hinhAnhs.stream()
                .map(HinhAnh::getImageUrl)
                .toList();
    }
}

