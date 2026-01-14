package com.do_issac.hotel_manage.statistics.controller;

import com.do_issac.hotel_manage.entity.NhanVien;
import com.do_issac.hotel_manage.repository.NhanVienRepository;
import com.do_issac.hotel_manage.statistics.dto.*;
import com.do_issac.hotel_manage.statistics.service.BookingStatisticsService;
import com.do_issac.hotel_manage.statistics.service.RevenueStatisticsService;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/nhan-vien/thong-ke")
@RequiredArgsConstructor
public class NhanVienStatisticsController {
    private final AuthUtil authUtil;
    private final RevenueStatisticsService revenueStatisticsService;
    private final BookingStatisticsService bookingStatisticsService;
    private final NhanVienRepository nhanVienRepo;

    @GetMapping("/doanh-thu/monthly")
    public ApiResponse<List<DoanhThuTheoThangDTO>> doanhThuTheoThang(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        NhanVien nv = nhanVienRepo.findByTaiKhoan_Id(userId);

        Long khachSanId = nv.getKhachSan().getId();

        return ApiResponse.success( "Lấy doanh thu theo tháng thành công",
                revenueStatisticsService.doanhThuTheoThangTheoKhachSan(
                        khachSanId, from, to
                )
        );
    }
    @GetMapping("/doanh-thu/loai-phong")
    public ApiResponse<List<DoanhThuTheoLoaiPhongDTO>> doanhThuTheoLoaiPhong(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        NhanVien nv = nhanVienRepo.findByTaiKhoan_Id(userId);

        Long khachSanId = nv.getKhachSan().getId();

        return ApiResponse.success(
                "Lấy doanh thu theo loại phòng thành công",
                revenueStatisticsService.doanhThuTheoLoaiPhong(
                        khachSanId, from, to
                )
        );
    }
    @GetMapping("/ty-le-lap-day-phong")
    public ApiResponse<TyLeLapDayPhongDTO> tyLeLapDayPhong(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        NhanVien nv = nhanVienRepo.findByTaiKhoan_Id(userId);
        return ApiResponse.success(
                "Lấy tỷ lệ lấp đầy phòng thành công",
                bookingStatisticsService.tyLeLapDayPhong(
                        nv.getKhachSan().getId(), from, to
                )
        );
    }
    @GetMapping("/top-loai-phong")
    public ApiResponse<List<SuDungLoaiPhongDTO>> loaiPhongSuDungNhieuNhat(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        NhanVien nv = nhanVienRepo.findByTaiKhoan_Id(userId);
        return ApiResponse.success(
                "Lấy loại phòng sử dụng nhiều nhất thành công",
                bookingStatisticsService.loaiPhongSuDungNhieuNhat(
                        nv.getKhachSan().getId(), from, to
                )
        );
    }

    @GetMapping("/check-in")
    public ApiResponse<SoLuotCheckInDTO> soLuotCheckIn(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        NhanVien nv = nhanVienRepo.findByTaiKhoan_Id(userId);

        return ApiResponse.success(
                "Lấy số lượt check-in thành công",
                bookingStatisticsService.soLuotCheckIn(
                        nv.getKhachSan().getId(), from, to
                )
        );
    }
    @GetMapping("/ty-le-huy")
    public ApiResponse<TyLeHuyPhongDTO> tyLeHuy(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        NhanVien nv = nhanVienRepo.findByTaiKhoan_Id(userId);

        return ApiResponse.success(
                "Lấy tỷ lệ hủy phòng thành công",
                bookingStatisticsService.tyLeHuyPhong(
                        nv.getKhachSan().getId(), from, to
                )
        );
    }
}

