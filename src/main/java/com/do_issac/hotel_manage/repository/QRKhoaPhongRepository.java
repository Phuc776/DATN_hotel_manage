package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.QrKhoaPhong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QRKhoaPhongRepository extends JpaRepository<QrKhoaPhong, Long> {
    Optional<QrKhoaPhong> findByDatPhongId(Long datPhongId);
    Optional<QrKhoaPhong> findByMaJWT(String maJWT);
}
