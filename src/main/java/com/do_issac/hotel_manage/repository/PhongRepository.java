package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.Phong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhongRepository extends JpaRepository<Phong, Long> {
    List<Phong> findByKhachSan_Id(Long khachSanId);
    List<Phong> findByLoaiPhong_Id(Long loaiPhongId);
    long countByKhachSan_Id(Long khachSanId);

}
