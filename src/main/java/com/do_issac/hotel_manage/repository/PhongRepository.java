package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.Phong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhongRepository extends JpaRepository<Phong, Long> {
    List<Phong> findByKhachSanId(Long khachSanId);
    List<Phong> findByLoaiPhongId(Long loaiPhongId);
}
