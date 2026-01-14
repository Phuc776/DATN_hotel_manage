package com.do_issac.hotel_manage.statistics.service;

import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import com.do_issac.hotel_manage.repository.PhongRepository;
import com.do_issac.hotel_manage.statistics.dto.SoLuotCheckInDTO;
import com.do_issac.hotel_manage.statistics.dto.SuDungLoaiPhongDTO;
import com.do_issac.hotel_manage.statistics.dto.TyLeHuyPhongDTO;
import com.do_issac.hotel_manage.statistics.dto.TyLeLapDayPhongDTO;
import com.do_issac.hotel_manage.statistics.repository.BookingStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingStatisticsService {
    private final BookingStatisticsRepository bookingStatisticsRepository;
    private final PhongRepository phongRepository;

    public TyLeLapDayPhongDTO tyLeLapDayPhong(
            Long khachSanId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        long soPhong = phongRepository.countByKhachSan_Id(khachSanId);

        long totalRoomDays =
                soPhong * ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate());

        if (totalRoomDays == 0) return new TyLeLapDayPhongDTO(0);

        List<ChiTietDatPhong> bookings =
                bookingStatisticsRepository.datPhongTrongKhoang(khachSanId, from, to);

        long usedDays = 0;

        for (ChiTietDatPhong dp : bookings) {
            LocalDate start = dp.getNgayNhan().toLocalDate().isBefore(from.toLocalDate())
                    ? from.toLocalDate()
                    : dp.getNgayNhan().toLocalDate();

            LocalDate end = dp.getNgayTra().toLocalDate().isAfter(to.toLocalDate())
                    ? to.toLocalDate()
                    : dp.getNgayTra().toLocalDate();

            usedDays += ChronoUnit.DAYS.between(start, end);
        }

        double rate = (double) usedDays / totalRoomDays * 100;
        return new TyLeLapDayPhongDTO(Math.round(rate * 100.0) / 100.0);
    }


    public List<SuDungLoaiPhongDTO> loaiPhongSuDungNhieuNhat(
            Long khachSanId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return bookingStatisticsRepository
                .loaiPhongSuDungNhieuNhat(khachSanId, from, to)
                .stream()
                .map(r -> new SuDungLoaiPhongDTO(
                        (String) r[0],
                        (Long) r[1]
                ))
                .toList();
    }

    public SoLuotCheckInDTO soLuotCheckIn(
            Long khachSanId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        Long count = bookingStatisticsRepository.soLuotCheckIn(khachSanId, from, to);
        return new SoLuotCheckInDTO(count == null ? 0 : count);
    }

    public TyLeHuyPhongDTO tyLeHuyPhong(
            Long khachSanId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        Long tong = bookingStatisticsRepository.tongDatPhong(khachSanId, from, to);
        if (tong == null || tong == 0) {
            return new TyLeHuyPhongDTO(0);
        }

        Long huy = bookingStatisticsRepository.soDatPhongBiHuy(khachSanId, from, to);

        double rate = (double) (huy == null ? 0 : huy) / tong * 100;
        return new TyLeHuyPhongDTO(Math.round(rate * 100.0) / 100.0);
    }
}
