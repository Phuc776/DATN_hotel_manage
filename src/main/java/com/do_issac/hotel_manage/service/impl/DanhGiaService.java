package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.DanhGiaRequest;
import com.do_issac.hotel_manage.dto.response.DanhGiaCuaToiResponse;
import com.do_issac.hotel_manage.dto.response.DanhGiaResponse;
import com.do_issac.hotel_manage.entity.*;
import com.do_issac.hotel_manage.repository.DanhGiaRepository;
import com.do_issac.hotel_manage.repository.KhachHangRepository;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.repository.PhienLuuTruRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanhGiaService {
    private final DanhGiaRepository danhGiaRepo;
    private final KhachSanRepository khachSanRepo;
    private final KhachHangRepository khachHangRepo;
    private final PhienLuuTruRepository phienRepo;

    public List<DanhGiaResponse> xemDanhGiaKhachSan(Long khachSanId) {

        return danhGiaRepo
                .findByKhachSan_Id(khachSanId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DanhGiaCuaToiResponse> xemDanhGiaCuaToi(Long userId) {
        Long khachHangId = khachHangRepo.findKhachHangIdByTaiKhoanId(userId)
                .orElseThrow( () -> new RuntimeException("Khách hàng không tồn tại"));

        return danhGiaRepo
                .findByKhachHang_Id(khachHangId)
                .stream()
                .map(this::toCuaToiResponse)
                .toList();
    }

    @Transactional
    public ApiResponse<?> taoDanhGia(
            Long khachSanId,
            Long userId,
            DanhGiaRequest req
    ) {
        KhachSan ks = khachSanRepo.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        Long khachHangId = khachHangRepo.findKhachHangIdByTaiKhoanId(userId)
                .orElseThrow( () -> new RuntimeException("Khách hàng không tồn tại"));

        boolean daOThucSu = phienRepo
                .existsByKhachHang_IdAndTrangThaiAndDatPhongs_Phong_KhachSan_Id(
                        khachHangId,
                        TrangThaiPhien.DONG,
                        khachSanId
                );

        if (!daOThucSu) {
            throw new RuntimeException(
                    "Chỉ có thể đánh giá sau khi hoàn tất lưu trú tại khách sạn này"
            );
        }

        // (optional) chặn đánh giá trùng
        if (danhGiaRepo.existsByKhachHang_TaiKhoan_IdAndKhachSan_Id(userId, khachSanId)) {
            throw new RuntimeException("Bạn đã đánh giá khách sạn này rồi");
        }

        DanhGia dg = new DanhGia();
        dg.setKhachHang(khachHangRepo.getReferenceById(khachHangId));
        dg.setKhachSan(khachSanRepo.getReferenceById(khachSanId));
        dg.setDiemDanhGia(req.getDiemDanhGia());
        dg.setNoiDung(req.getNoiDung());
        dg.setNgayTao(LocalDateTime.now());

        return ApiResponse.success(
                "Đánh giá khách sạn thành công",
                toResponse(danhGiaRepo.save(dg))
        );
    }


    public double tinhDiemTrungBinh(Long khachSanId) {
        return Optional.ofNullable(
                danhGiaRepo.tinhDiemTrungBinh(khachSanId)
        ).orElse(0.0);
    }

    public long demSoDanhGia(Long khachSanId) {
        return danhGiaRepo.demSoDanhGia(khachSanId);
    }

    private DanhGiaResponse toResponse(DanhGia dg) {
        return new DanhGiaResponse(
                dg.getId(),
                dg.getDiemDanhGia(),
                dg.getNoiDung(),
                dg.getKhachHang().getTaiKhoan().getHoTen(), // hoặc ten
                dg.getNgayTao()
        );
    }
    private DanhGiaCuaToiResponse toCuaToiResponse(DanhGia dg) {
        KhachSan ks = dg.getKhachSan();
        return new DanhGiaCuaToiResponse(
                dg.getId(),
                dg.getDiemDanhGia(),
                dg.getNoiDung(),
                ks.getId(),
                ks.getTenKhachSan(),
                dg.getNgayTao()
        );
    }
}
