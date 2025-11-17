package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "khach_hang")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hoTen;

    @Column(unique = true)
    private String CCCD;

    private LocalDateTime ngayXacThucCCCD;

    @Column(nullable = false, unique = true)
    private String soDienThoai;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan taiKhoan;

    @OneToMany
    @JoinColumn(name = "chiTietDatPhongId")
    private List<ChiTietDatPhong> chiTietDatPhongs;

    @OneToMany
    @JoinColumn(name = "danhGiaId")
    private List<DanhGia> danhGias;
}
