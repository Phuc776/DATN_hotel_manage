package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.TrangThaiPhien;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PhienLuuTruDetailResponse {
    private Long id;
    private LocalDateTime batDau;
    private LocalDateTime ketThuc;
    private TrangThaiPhien trangThai;

    private List<ChiTietDatPhongResponse> datPhongs;
    private List<SuDungDichVuResponse> suDungDichVus;

    private Double tongTienTamTinh;
}
