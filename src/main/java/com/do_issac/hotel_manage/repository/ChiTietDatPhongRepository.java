package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import com.do_issac.hotel_manage.entity.TrangThaiDatPhong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ChiTietDatPhongRepository extends JpaRepository<ChiTietDatPhong, Long> {
    List<ChiTietDatPhong> findByKhachHang_Id(Long khachHangId);
    List<ChiTietDatPhong> findByPhong_Id(Long phongId);

    // Tìm phòng còn trống theo ngày
    List<ChiTietDatPhong> findByNgayNhanLessThanEqualAndNgayTraGreaterThanEqual(
            LocalDate ngayTra,
            LocalDate ngayNhan
    );

    // Tìm các đặt phòng trùng thời gian
    @Query("""
    SELECT c
    FROM ChiTietDatPhong c
    WHERE c.phong.id = :phongId
    AND c.trangThai IN :activeStatuses
    AND (:ngayNhan < c.ngayTra AND :ngayTra > c.ngayNhan)
""")
    List<ChiTietDatPhong> findConflict(
            @Param("phongId") Long phongId,
            @Param("ngayNhan") LocalDateTime ngayNhan,
            @Param("ngayTra") LocalDateTime ngayTra,
            @Param("activeStatuses") List<TrangThaiDatPhong> activeStatuses
    );

    // Đếm số phòng đã được đặt theo loại phòng và ngày
    @Query("""
    SELECT COUNT(c)
    FROM ChiTietDatPhong c
    WHERE c.phong.loaiPhong.id = :loaiPhongId
    AND c.trangThai IN :activeStatuses
    AND (:ngayNhan < c.ngayTra AND :ngayTra > c.ngayNhan)
""")
    long countBookedRooms(
            @Param("loaiPhongId") Long loaiPhongId,
            @Param("ngayNhan") LocalDateTime ngayNhan,
            @Param("ngayTra") LocalDateTime ngayTra,
            @Param("activeStatuses") List<TrangThaiDatPhong> activeStatuses
    );


    @Query("""
        SELECT c FROM ChiTietDatPhong c
        WHERE c.phong.khachSan.id = :khachSanId
    """)
    List<ChiTietDatPhong> findByKhachSan(@Param("khachSanId") Long khachSanId);
}
