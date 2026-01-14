package com.do_issac.hotel_manage.statistics.service;

import com.do_issac.hotel_manage.statistics.dto.DoanhThuTheoLoaiPhongDTO;
import com.do_issac.hotel_manage.statistics.dto.DoanhThuTheoThangDTO;
import com.do_issac.hotel_manage.statistics.repository.DoanhThuStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueStatisticsService {

    private final DoanhThuStatisticsRepository doanhThuStatisticsRepository;

    // Dùng cho NHÂN VIÊN (1 KS) hoặc CHỦ KHÁCH SẠN (1 KS)
    public List<DoanhThuTheoThangDTO> doanhThuTheoThangTheoKhachSan(
            Long khachSanId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return map(
                doanhThuStatisticsRepository
                        .doanhThuTheoThangTheoKhachSan(khachSanId, from, to)
        );
    }

    // Dùng cho CHỦ KHÁCH SẠN (nhiều KS)
    public List<DoanhThuTheoThangDTO> doanhThuTheoThangTheoChuKhachSan(
            Long chuKhachSanId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return map(
                doanhThuStatisticsRepository
                        .doanhThuTheoThangTheoChuKhachSan(chuKhachSanId, from, to)
        );
    }

    private List<DoanhThuTheoThangDTO> map(List<Object[]> raw) {
        return raw.stream()
                .map(r -> {
                    Integer year = (Integer) r[0];
                    Integer month = (Integer) r[1];
                    Double revenue = (Double) r[2];

                    String thang = String.format("%04d-%02d", year, month);
                    return new DoanhThuTheoThangDTO(thang, revenue);
                })
                .toList();
    }
    public List<DoanhThuTheoLoaiPhongDTO> doanhThuTheoLoaiPhong(
            Long khachSanId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return doanhThuStatisticsRepository
                .doanhThuTheoLoaiPhong(khachSanId, from, to)
                .stream()
                .map(r -> new DoanhThuTheoLoaiPhongDTO(
                        (String) r[0],
                        (Double) r[1]
                ))
                .toList();
    }
}
