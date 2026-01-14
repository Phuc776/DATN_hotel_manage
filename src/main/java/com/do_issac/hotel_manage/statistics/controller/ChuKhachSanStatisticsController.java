package com.do_issac.hotel_manage.statistics.controller;

import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
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
@RequestMapping("/chu-khach-san/thong-ke")
@RequiredArgsConstructor
public class ChuKhachSanStatisticsController {
    private final AuthUtil authUtil;
    private final RevenueStatisticsService revenueStatisticsService;
    private final KhachSanRepository khachSanRepository;
    private final BookingStatisticsService bookingStatisticsService;

    @GetMapping("/doanh-thu/monthly")
    public ApiResponse<List<DoanhThuTheoThangDTO>> doanhThuTheoThang(
            @RequestParam Long khachSanId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));
        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập thống kê của khách sạn này");
        }

        return ApiResponse.success( "Lấy doanh thu theo tháng thành công",
                revenueStatisticsService.doanhThuTheoThangTheoKhachSan(
                        khachSanId, from, to
                )
        );
    }

    @GetMapping("/doanh-thu/all/monthly")
    public ApiResponse<List<DoanhThuTheoThangDTO>> doanhThuTatCaKhachSanTheoThang(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();

        return ApiResponse.success(
                "Lấy doanh thu tất cả khách sạn theo tháng thành công",
                revenueStatisticsService.doanhThuTheoThangTheoChuKhachSan(
                        userId, from, to
                )
        );
    }

    @GetMapping("/doanh-thu/loai-phong")
    public ApiResponse<List<DoanhThuTheoLoaiPhongDTO>> doanhThuTheoLoaiPhong(
            @RequestParam Long khachSanId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();

        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new RuntimeException("Không có quyền truy cập khách sạn này");
        }

        return ApiResponse.success(
                "Lấy doanh thu theo loại phòng thành công",
                revenueStatisticsService.doanhThuTheoLoaiPhong(
                        khachSanId, from, to
                )
        );
    }
    @GetMapping("/ty-le-lap-day-phong")
    public ApiResponse<TyLeLapDayPhongDTO> tyLeLapDayPhong(
            @RequestParam Long khachSanId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Bạn không có khách sạn nào"));
        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập thống kê của khách sạn này");
        }

        return ApiResponse.success(
                "Lấy tỷ lệ lấp đầy phòng thành công",
                bookingStatisticsService.tyLeLapDayPhong(
                        khachSanId, from, to
                )
        );
    }
    @GetMapping("/top-loai-phong")
    public ApiResponse<List<SuDungLoaiPhongDTO>> loaiPhongSuDungNhieuNhat(
            @RequestParam Long khachSanId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Bạn không có khách sạn nào"));
        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập thống kê của khách sạn này");
        }
        return ApiResponse.success(
                "Lấy loại phòng sử dụng nhiều nhất thành công",
                bookingStatisticsService.loaiPhongSuDungNhieuNhat(
                        khachSanId, from, to
                )
        );
    }

    @GetMapping("/check-in")
    public ApiResponse<SoLuotCheckInDTO> soLuotCheckIn(
            @RequestParam Long khachSanId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Bạn không có khách sạn nào"));
        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập thống kê của khách sạn này");
        }

        return ApiResponse.success(
                "Lấy số lượt check-in thành công",
                bookingStatisticsService.soLuotCheckIn(
                        khachSanId, from, to
                )
        );
    }
    @GetMapping("/ty-le-huy")
    public ApiResponse<TyLeHuyPhongDTO> tyLeHuy(
            @RequestParam Long khachSanId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Long userId = authUtil.getCurrentUserId();
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Bạn không có khách sạn nào"));
        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập thống kê của khách sạn này");
        }

        return ApiResponse.success(
                "Lấy tỷ lệ hủy phòng thành công",
                bookingStatisticsService.tyLeHuyPhong(
                        khachSanId, from, to
                )
        );
    }

}

