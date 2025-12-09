package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DichVuRepository extends JpaRepository<DichVu, Long> {
    List<DichVu> findAllByKhachSans_ChuKhachSan_Id(Long chuKhachSanId);
}
