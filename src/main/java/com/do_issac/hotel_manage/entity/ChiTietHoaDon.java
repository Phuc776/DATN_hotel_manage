package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chi_tiet_hoa_don")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoaiChiTietHoaDon loai;

    private String moTa;
    private double donGia;
    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "hoaDonId", nullable = false)
    private HoaDon hoaDon;
}
