package com.do_issac.hotel_manage.mapper;

import com.do_issac.hotel_manage.dto.response.PhienLuuTruResponse;
import com.do_issac.hotel_manage.dto.response.PhienLuuTruDetailResponse;
import com.do_issac.hotel_manage.entity.PhienLuuTru;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ChiTietDatPhongMapper.class,
        SuDungDichVuMapper.class
})
public interface PhienLuuTruMapper {

    @Mapping(target = "tenKhachSan", expression = """
        java(
            phien.getDatPhongs().isEmpty()
                ? null
                : phien.getDatPhongs().get(0).getPhong().getKhachSan().getTenKhachSan()
        )
    """)
    PhienLuuTruResponse toResponse(PhienLuuTru phien);

    List<PhienLuuTruResponse> toResponseList(List<PhienLuuTru> list);

    @Mapping(target = "tongTienTamTinh", expression = "java(tinhTongTien(phien))")
    PhienLuuTruDetailResponse toDetailResponse(PhienLuuTru phien);

    // helper
    default Double tinhTongTien(PhienLuuTru phien) {
        double tienPhong = phien.getDatPhongs().stream()
                .mapToDouble(dp -> dp.getPhong().getLoaiPhong().getGia())
                .sum();

        double tienDichVu = phien.getSuDungDichVus().stream()
                .mapToDouble(sd -> sd.getSoLuong() * sd.getDonGiaTaiThoiDiem())
                .sum();

        return tienPhong + tienDichVu;
    }
}

