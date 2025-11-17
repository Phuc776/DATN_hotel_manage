package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.ChiTietDatPhong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChiTietDatPhongRepository extends JpaRepository<ChiTietDatPhong, Long> {
    List<ChiTietDatPhong> findByKhachHangId(Long khachHangId);
    List<ChiTietDatPhong> findByPhongId(Long phongId);

    // Tìm phòng còn trống theo ngày
    List<ChiTietDatPhong> findByNgayNhanLessThanEqualAndNgayTraGreaterThanEqual(
            LocalDate ngayTra,
            LocalDate ngayNhan
    );
}
