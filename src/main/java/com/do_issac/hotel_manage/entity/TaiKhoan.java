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
    private String matKhau;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VaiTro vaiTro;

    private LocalDateTime ngayTao;

    private LocalDateTime lanCuoiDangNhap;

    private boolean trangThai;

    @OneToMany
    @JoinColumn(name = "thongBaoId")
    private List<ThongBao> thongBaos;

    @OneToOne
    @JoinColumn(name = "nhanVienId")
    private NhanVien nhanVien;

    @OneToOne
    @JoinColumn(name = "khachHangId")
    private KhachHang khachHang;

    @OneToMany
    @JoinColumn(name = "khachSanId")
    private List<KhachSan> khachSans;

    @OneToMany
    @JoinColumn(name = "baiDangId")
    private List<BaiDangPhong> baiDangs;

}
