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

    @ManyToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan chuKhachSan;

    @OneToMany
    @JoinColumn(name = "phongId")
    private List<Phong> phongs;

    @OneToMany
    @JoinColumn(name = "nhanVienId")
    private List<NhanVien> nhanViens;

    @OneToMany
    @JoinColumn(name = "danhGiaId")
    private List<DanhGia> danhGias;
}
