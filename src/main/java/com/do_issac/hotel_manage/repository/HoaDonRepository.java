package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    @Query("""
SELECT hd FROM HoaDon hd
JOIN hd.phienLuuTru plt
JOIN plt.datPhongs dp
JOIN dp.phong p
WHERE p.khachSan.id = :khachSanId
""")
    List<HoaDon> findAllByKhachSanId(@Param("khachSanId") Long khachSanId);

    @Query("""
SELECT hd FROM HoaDon hd
JOIN hd.phienLuuTru plt
JOIN plt.datPhongs dp
JOIN dp.phong p
WHERE p.khachSan.chuKhachSan.id = :chuKhachSanId
""")
    List<HoaDon> findAllByChuKhachSan_Id(@Param("chuKhachSanId") Long chuKhachSanId);

    List<HoaDon> findAllByPhienLuuTru_KhachHang_TaiKhoan_Id(Long taiKhoanId);
}
