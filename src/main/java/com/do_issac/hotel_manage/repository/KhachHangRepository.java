package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    boolean existsBySoDienThoai(String soDienThoai);
}
