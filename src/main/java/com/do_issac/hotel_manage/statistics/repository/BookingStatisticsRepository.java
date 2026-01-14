package com.do_issac.hotel_manage.statistics.repository;

import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStatisticsRepository extends JpaRepository<ChiTietDatPhong, Long> {

    @Query("""
    SELECT dp
    FROM ChiTietDatPhong dp
    JOIN dp.phong ph
    WHERE ph.khachSan.id = :khachSanId
      AND dp.trangThai <> com.do_issac.hotel_manage.entity.TrangThaiDatPhong.DA_HUY
      AND dp.ngayNhan < :to
      AND dp.ngayTra > :from
""")
    List<ChiTietDatPhong> datPhongTrongKhoang(
            @Param("khachSanId") Long khachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
    SELECT 
        lp.tenLoaiPhong,
        COUNT(dp.id)
    FROM ChiTietDatPhong dp
    JOIN dp.phong ph
    JOIN ph.loaiPhong lp
    WHERE ph.khachSan.id = :khachSanId
      AND dp.trangThai <> com.do_issac.hotel_manage.entity.TrangThaiDatPhong.DA_HUY
      AND dp.ngayNhan BETWEEN :from AND :to
    GROUP BY lp.id, lp.tenLoaiPhong
    ORDER BY COUNT(dp.id) DESC
""")
    List<Object[]> loaiPhongSuDungNhieuNhat(
            @Param("khachSanId") Long khachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
    SELECT COUNT(dp.id)
    FROM ChiTietDatPhong dp
    JOIN dp.phong ph
    WHERE ph.khachSan.id = :khachSanId
      AND dp.trangThai IN (
          com.do_issac.hotel_manage.entity.TrangThaiDatPhong.DA_XAC_NHAN,
          com.do_issac.hotel_manage.entity.TrangThaiDatPhong.DANG_O,
          com.do_issac.hotel_manage.entity.TrangThaiDatPhong.CHO_TRA,
          com.do_issac.hotel_manage.entity.TrangThaiDatPhong.DA_TRA_PHONG
      )
      AND dp.ngayNhan BETWEEN :from AND :to
""")
    Long soLuotCheckIn(
            @Param("khachSanId") Long khachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
    SELECT COUNT(dp.id)
    FROM ChiTietDatPhong dp
    JOIN dp.phong ph
    WHERE ph.khachSan.id = :khachSanId
      AND dp.ngayDat BETWEEN :from AND :to
""")
    Long tongDatPhong(
            @Param("khachSanId") Long khachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
    SELECT COUNT(dp.id)
    FROM ChiTietDatPhong dp
    JOIN dp.phong ph
    WHERE ph.khachSan.id = :khachSanId
      AND dp.trangThai = com.do_issac.hotel_manage.entity.TrangThaiDatPhong.DA_HUY
      AND dp.ngayDat BETWEEN :from AND :to
""")
    Long soDatPhongBiHuy(
            @Param("khachSanId") Long khachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
