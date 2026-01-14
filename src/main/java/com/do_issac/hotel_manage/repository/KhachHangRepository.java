package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    KhachHang findByTaiKhoan_Id(Long taiKhoanId);

    boolean existsByCCCD(String cccd);

    @Query("""
        select k.id
        from KhachHang k
        where k.taiKhoan.id = :userId
    """)
    Optional<Long> findKhachHangIdByTaiKhoanId(@Param("userId") Long userId);
}
