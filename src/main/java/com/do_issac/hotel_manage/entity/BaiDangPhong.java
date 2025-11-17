package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bai_dang_phong")
@Data @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BaiDangPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tieuDe;
    private String moTa;
    private LocalDateTime ngayDang;
    private boolean daDuyet;

    @ManyToOne
    @JoinColumn(name = "phongId", nullable = false)
    private Phong phong;

    @ManyToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan chuKhachSan;
}
