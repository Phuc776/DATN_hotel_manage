package com.do_issac.hotel_manage.repository;

import com.do_issac.hotel_manage.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    List<ThongBao> findAllByOrderByNgayTaoDesc();
    List<ThongBao> findByTaiKhoanIdOrderByNgayTaoDesc(Long taiKhoanId);
    List<ThongBao> findByActorIdOrderByNgayTaoDesc(Long actorId);

//    List<ThongBao> findByTaiKhoanIdInOrderByNgayTaoDesc(List<Long> ids);

}
