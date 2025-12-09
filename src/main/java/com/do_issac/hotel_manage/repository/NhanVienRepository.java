package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NhanVienRepository extends JpaRepository<NhanVien, Long> {
    List<NhanVien> findAllByKhachSan_ChuKhachSan_Id(Long chuKhachSanId);
    List<NhanVien> findAllByKhachSan_Id(Long khachSanId);

    boolean existsByTaiKhoanIdAndKhachSanId(Long taiKhoanId, Long khachSanId);
}
