package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.DetailKhachSanResponse;
import com.do_issac.hotel_manage.entity.DanhGia;
import com.do_issac.hotel_manage.entity.KhachSan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetailKhachSanMapper {

    @Mapping(target = "soPhongKinhDoanh", expression = "java(ks.getPhongs() != null ? ks.getPhongs().size() : 0)")
    @Mapping(target = "soNhanVien", expression = "java(ks.getNhanViens() != null ? ks.getNhanViens().size() : 0)")
    @Mapping(target = "soBaiDangPhong", expression = "java(ks.getBaiDangPhongs() != null ? ks.getBaiDangPhongs().size() : 0)")
    @Mapping(target = "danhGiaTrungBinh", expression = "java(tinhDiemTrungBinh(ks))")
    DetailKhachSanResponse toDetailResponse(KhachSan ks);

    // Hàm xử lý tính trung bình điểm đánh giá
    default double tinhDiemTrungBinh(KhachSan ks) {
        if (ks.getDanhGias() == null || ks.getDanhGias().isEmpty()) return 0;

        return ks.getDanhGias().stream()
                .mapToInt(DanhGia::getDiemDanhGia)
                .average()
                .orElse(0);
    }
}

