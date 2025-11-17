package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chi_tiet_hoa_don")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String moTa;
    private double donGia;
    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "loaiPhongId", nullable = false)
    private LoaiPhong loaiPhong;

    @ManyToOne
    @JoinColumn(name = "dichVuId", nullable = false)
    private DichVu dichVu;

    @ManyToOne
    @JoinColumn(name = "hoaDonId", nullable = false)
    private HoaDon hoaDon;
}
