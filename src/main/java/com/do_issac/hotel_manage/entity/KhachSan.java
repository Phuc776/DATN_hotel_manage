package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "khach_san")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachSan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenKhachSan;
    private String diaChi;

    @Enumerated(EnumType.STRING)
    private TrangThaiKhachSan trangThai = TrangThaiKhachSan.CHO_DUYET;

    @ManyToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan chuKhachSan;

    @OneToMany(mappedBy = "khachSan", cascade = CascadeType.ALL)
    private List<Phong> phongs;

    @OneToMany(mappedBy = "khachSan", cascade = CascadeType.ALL)
    private List<NhanVien> nhanViens;

    @OneToMany(mappedBy = "khachSan", cascade = CascadeType.ALL)
    private List<DanhGia> danhGias;
}
