package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.PhienLuuTru;
import com.do_issac.hotel_manage.entity.TrangThaiDatPhong;
import com.do_issac.hotel_manage.entity.TrangThaiPhien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhienLuuTruRepository extends JpaRepository<PhienLuuTru, Long> {
    Optional<PhienLuuTru> findByKhachHang_TaiKhoan_IdAndTrangThai(Long taiKhoanId, TrangThaiPhien trangThai);

    List<PhienLuuTru> findAllByKhachHang_TaiKhoan_Id(Long userId);

    boolean existsByKhachHang_IdAndTrangThaiAndDatPhongs_Phong_KhachSan_Id(
            Long khachHangId,
            TrangThaiPhien trangThai,
            Long khachSanId
    );

}

