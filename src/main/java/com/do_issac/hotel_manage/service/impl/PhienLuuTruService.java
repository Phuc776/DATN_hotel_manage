package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.HoaDonDetailResponse;
import com.do_issac.hotel_manage.entity.*;
import com.do_issac.hotel_manage.mapper.HoaDonMapper;
import com.do_issac.hotel_manage.mapper.PhienLuuTruMapper;
import com.do_issac.hotel_manage.repository.PhienLuuTruRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PhienLuuTruService {
    private final PhienLuuTruRepository phienRepo;
    private final PhienLuuTruMapper phienMapper;
    private final HoaDonMapper hoaDonMapper;
    private final HoaDonPdfService hoaDonPdfService;
    private final EmailService emailService;

    private final HoaDonService hoaDonService;

    public PhienLuuTru taoPhienChoKhach(KhachHang khachHang) {
        PhienLuuTru phien = new PhienLuuTru();
        phien.setTrangThai(TrangThaiPhien.TAO);
        phien.setKhachHang(khachHang);

        return phienRepo.save(phien);
    }

    @Transactional
    public void moPhien(ChiTietDatPhong booking) {
        PhienLuuTru phien = booking.getPhienLuuTru();

        if (phien.getTrangThai() == TrangThaiPhien.TAO) {
            phien.setTrangThai(TrangThaiPhien.MO);
            phien.setBatDau(LocalDateTime.now());
            phienRepo.save(phien);
        }
    }

    public ApiResponse<?> getPhienDangMo(Long userId) {
        PhienLuuTru phien = phienRepo
                .findByKhachHang_TaiKhoan_IdAndTrangThai(userId, TrangThaiPhien.MO)
                .orElse(null);

        if (phien == null) {
            return ApiResponse.success("Không có phiên đang mở", null);
        }

        return ApiResponse.success(
                "Phiên đang lưu trú",
                phienMapper.toResponse(phien)
        );
    }

    public ApiResponse<?> getPhienDetail(Long userId, Long phienId) {
        PhienLuuTru phien = phienRepo.findById(phienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên"));

        if (!phien.getKhachHang().getTaiKhoan().getId().equals(userId)) {
            throw new RuntimeException("Không có quyền truy cập phiên này");
        }

        return ApiResponse.success(
                "Chi tiết phiên",
                phienMapper.toDetailResponse(phien)
        );
    }

    public ApiResponse<?> getPhienHistory(Long userId) {
        return ApiResponse.success(
                "Lịch sử phiên lưu trú",
                phienMapper.toResponseList(
                        phienRepo.findAllByKhachHang_TaiKhoan_Id(userId)
                )
        );
    }

    @Transactional
    public void kiemTraDongPhien(PhienLuuTru phien) {

        boolean allFinished = phien.getDatPhongs().stream()
                .allMatch(b ->
                        b.getTrangThai() == TrangThaiDatPhong.DA_TRA_PHONG
                                || b.getTrangThai() == TrangThaiDatPhong.DA_HUY
                );

        if (!allFinished) return;

        boolean allHuy = phien.getDatPhongs().stream()
                .allMatch(b -> b.getTrangThai() == TrangThaiDatPhong.DA_HUY);

        // tất cả đều hủy → đóng thẳng, không cần hóa đơn
        if (allHuy) {
            phien.setTrangThai(TrangThaiPhien.DONG);
            phien.setKetThuc(LocalDateTime.now());
            phienRepo.save(phien);
            return;
        }

        // CÓ ít nhất 1 booking đã ở → cần thanh toán
        phien.setTrangThai(TrangThaiPhien.CHO_THANH_TOAN);
        phienRepo.save(phien);

        // chỉ tạo hóa đơn, CHƯA gửi mail
        hoaDonService.taoHoaDonChoPhien(phien);
    }


}
