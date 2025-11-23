package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "dich_vu")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenDichVu;
    private double donGia;
    private String moTa;

    @OneToMany(mappedBy = "dichVu")
    private List<ChiTietHoaDon> chiTietHoaDons;

    @ManyToMany(mappedBy = "dichVus")
    private List<KhachSan> khachSans;
}
