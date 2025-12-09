package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tai_khoan")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hoTen;

    @Column(nullable = false)
    private String matKhau;

    @Column(unique = true)
    private String soDienThoai;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VaiTro vaiTro;

    private LocalDateTime ngayTao;

    private LocalDateTime lanCuoiDangNhap;

    private boolean trangThai;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL)
    private List<ThongBao> thongBaos;

    @OneToOne(mappedBy = "taiKhoan")
    private NhanVien nhanVien;

    @OneToOne(mappedBy = "taiKhoan")
    private KhachHang khachHang;

    @OneToMany(mappedBy = "chuKhachSan")
    private List<KhachSan> khachSans;

    @OneToMany(mappedBy = "chuKhachSan")
    private List<BaiDangPhong> baiDangs;

}
