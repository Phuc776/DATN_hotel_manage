package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {

    Optional<ThanhToan> findByMaGiaoDich(String maGiaoDich);

    List<ThanhToan> findByPhienLuuTru_IdOrderByNgayThanhToanAsc(Long phienId);

    @Query("""
        SELECT COALESCE(SUM(t.soTien), 0)
        FROM ThanhToan t
        WHERE t.phienLuuTru.id = :phienId
          AND t.trangThai = 'SUCCESS'
    """)
    long tongTienDaThanhToan(@Param("phienId") Long phienId);
}
