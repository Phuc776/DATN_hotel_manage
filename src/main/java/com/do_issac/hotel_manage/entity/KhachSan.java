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

    @ManyToMany
    @JoinTable(
            name = "khachsan_loaiphong",
            joinColumns = @JoinColumn(name = "khach_san_id"),
            inverseJoinColumns = @JoinColumn(name = "loai_phong_id")
    )
    private List<LoaiPhong> loaiPhongs;

    @ManyToMany
    @JoinTable(
            name = "khachsan_dichvu",
            joinColumns = @JoinColumn(name = "khach_san_id"),
            inverseJoinColumns = @JoinColumn(name = "dich_vu_id")
    )
    private List<DichVu> dichVus;
}
