package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.Phong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PhongRepository extends JpaRepository<Phong, Long> {
    List<Phong> findByKhachSan_ChuKhachSan_Id(Long chuKhachSanId);
    List<Phong> findByKhachSan_Id(Long khachSanId);
    List<Phong> findByLoaiPhong_Id(Long loaiPhongId);
    long countByKhachSan_Id(Long khachSanId);

    @Query("""
        SELECT DISTINCT p
        FROM Phong p
        LEFT JOIN FETCH p.chiTietDatPhongs
        WHERE p.khachSan.id = :khachSanId
    """)
    List<Phong> findByKhachSanWithBookings(Long khachSanId);

    @Query("""
        SELECT p
        FROM Phong p
        LEFT JOIN FETCH p.chiTietDatPhongs
        WHERE p.id = :id
    """)
    Optional<Phong> findByIdWithBookings(Long id);

}
