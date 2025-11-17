package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chi_tiet_dat_phong")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime ngayDat;
    private LocalDateTime ngayNhan;
    private LocalDateTime ngayTra;
    private double tienCoc;
    private int soNguoiLon;
    private int soTreEm;
    private String ghiChu;

    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "phongId", nullable = false)
    private Phong phong;

    @ManyToOne
    @JoinColumn(name = "khachHangId", nullable = false)
    private KhachHang khachHang;

    @OneToMany
    @JoinColumn(name = "thanhToanId")
    private List<ThanhToan> thanhToans;

    @OneToOne
    @JoinColumn(name = "hoaDonId")
    private HoaDon hoaDon;

    @OneToMany
    @JoinColumn(name = "qrID")
    private List<QrKhoaPhong> qrs;
}
