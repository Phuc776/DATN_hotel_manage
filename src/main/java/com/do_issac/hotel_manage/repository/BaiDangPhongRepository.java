package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.BaiDangPhong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaiDangPhongRepository extends JpaRepository<BaiDangPhong, Long> {
    List<BaiDangPhong> findByDaDuyet(boolean daDuyet);
}
