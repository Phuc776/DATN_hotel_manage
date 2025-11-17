package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "loai_phong")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoaiPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenLoaiPhong;
    private Double gia;
    private int soLuongCon;
    private String moTa;
    private int soNguoiLon;
    private int soTreEm;

    @OneToMany
    @JoinColumn(name = "chiTietHoaDonId")
    private List<ChiTietHoaDon> chiTietHoaDons;

    @OneToMany
    @JoinColumn(name = "phongId")
    private List<Phong> phongs;
}
