package com.do_issac.hotel_manage.statistics.repository;

import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import com.do_issac.hotel_manage.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DoanhThuStatisticsRepository extends JpaRepository<ThanhToan, Long> {
    @Query("""
    SELECT 
        YEAR(t.ngayThanhToan),
        MONTH(t.ngayThanhToan),
        SUM(t.soTien)
    FROM ThanhToan t
    JOIN t.phienLuuTru p
    JOIN p.datPhongs dp
    JOIN dp.phong ph
    JOIN ph.khachSan ks
    WHERE t.trangThai = com.do_issac.hotel_manage.entity.TrangThaiThanhToan.SUCCESS
      AND ks.chuKhachSan.id = :chuKhachSanId
      AND t.ngayThanhToan BETWEEN :from AND :to
    GROUP BY YEAR(t.ngayThanhToan), MONTH(t.ngayThanhToan)
    ORDER BY YEAR(t.ngayThanhToan), MONTH(t.ngayThanhToan)
""")
    List<Object[]> doanhThuTheoThangTheoChuKhachSan(
            @Param("chuKhachSanId") Long chuKhachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
    SELECT 
        YEAR(t.ngayThanhToan),
        MONTH(t.ngayThanhToan),
        SUM(t.soTien)
    FROM ThanhToan t
    JOIN t.phienLuuTru p
    JOIN p.datPhongs dp
    JOIN dp.phong ph
    WHERE t.trangThai = com.do_issac.hotel_manage.entity.TrangThaiThanhToan.SUCCESS
      AND ph.khachSan.id = :khachSanId
      AND t.ngayThanhToan BETWEEN :from AND :to
    GROUP BY YEAR(t.ngayThanhToan), MONTH(t.ngayThanhToan)
    ORDER BY YEAR(t.ngayThanhToan), MONTH(t.ngayThanhToan)
""")
    List<Object[]> doanhThuTheoThangTheoKhachSan(
            @Param("khachSanId") Long khachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
    SELECT 
        lp.tenLoaiPhong,
        SUM(t.soTien)
    FROM ThanhToan t
    JOIN t.phienLuuTru p
    JOIN p.datPhongs dp
    JOIN dp.phong ph
    JOIN ph.loaiPhong lp
    WHERE t.trangThai = com.do_issac.hotel_manage.entity.TrangThaiThanhToan.SUCCESS
      AND ph.khachSan.id = :khachSanId
      AND t.ngayThanhToan BETWEEN :from AND :to
    GROUP BY lp.id, lp.tenLoaiPhong
    ORDER BY SUM(t.soTien) DESC
""")
    List<Object[]> doanhThuTheoLoaiPhong(
            @Param("khachSanId") Long khachSanId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
