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

    @OneToMany(mappedBy = "loaiPhong")
    private List<ChiTietHoaDon> chiTietHoaDons;

    @OneToMany(mappedBy = "loaiPhong")
    private List<Phong> phongs;

    @ManyToMany(mappedBy = "loaiPhongs")
    private List<KhachSan> khachSans;
}
