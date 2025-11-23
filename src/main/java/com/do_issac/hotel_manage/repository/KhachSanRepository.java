package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.TrangThaiKhachSan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KhachSanRepository extends JpaRepository<KhachSan, Long> {
    List<KhachSan> findByChuKhachSanId(Long ownerId);
    List<KhachSan> findByTrangThai(TrangThaiKhachSan trangThai);
}
