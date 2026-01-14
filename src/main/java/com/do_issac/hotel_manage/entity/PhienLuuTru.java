package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "phien_luu_tru")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhienLuuTru {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime batDau;
    private LocalDateTime ketThuc;

    @Enumerated(EnumType.STRING)
    private TrangThaiPhien trangThai;

    @ManyToOne
    @JoinColumn(name = "khachHangId", nullable = false)
    private KhachHang khachHang;

    @OneToMany(mappedBy = "phienLuuTru")
    private List<ThanhToan> thanhToans;

    @OneToMany(mappedBy = "phienLuuTru")
    private List<SuDungDichVu> suDungDichVus;

    @OneToMany(mappedBy = "phienLuuTru")
    private List<ChiTietDatPhong> datPhongs = new ArrayList<>();

    @OneToOne(mappedBy = "phienLuuTru")
    private HoaDon hoaDon;
}
