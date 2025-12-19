package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bai_dang_phong")
@Getter @Setter @NoArgsConstructor
public class BaiDangPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tieuDe;
    private String moTa;
    private LocalDateTime ngayDang;

    @Enumerated(EnumType.STRING)
    private TrangThaiBaiDang trangThaiBaiDang;

    private Long nguoiDuyetId;

    private Integer soLuongPhong;

    @ManyToOne
    @JoinColumn(name = "loaiPhongId", nullable = false)
    private LoaiPhong loaiPhong;

    @ManyToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan chuKhachSan;

    @ManyToOne
    @JoinColumn(name = "khachSanId", nullable = false)
    private KhachSan khachSan;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bai_dang_phong_id")
    private List<HinhAnh> hinhAnh;
}
