package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {
    List<DanhGia> findByKhachSan_Id(Long khachSanId);
    List<DanhGia> findByKhachHang_Id(Long khachHangId);

    @Query("select avg(d.diemDanhGia) from DanhGia d where d.khachSan.id = :ksId")
    Double tinhDiemTrungBinh(@Param("ksId") Long ksId);

    @Query("select count(d) from DanhGia d where d.khachSan.id = :ksId")
    long demSoDanhGia(@Param("ksId") Long ksId);

    boolean existsByKhachHang_TaiKhoan_IdAndKhachSan_Id(Long taiKhoanId, Long khachSanId);
}
