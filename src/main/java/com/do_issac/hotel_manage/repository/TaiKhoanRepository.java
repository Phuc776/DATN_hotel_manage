package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    boolean existsByEmail(String email);
    Optional<TaiKhoan> findByEmail(String email);
}
