package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.LoaiPhong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoaiPhongRepository extends JpaRepository<LoaiPhong, Long> {
    List<LoaiPhong> findAllByKhachSans_ChuKhachSan_Id(Long chuKhachSanId);
}
