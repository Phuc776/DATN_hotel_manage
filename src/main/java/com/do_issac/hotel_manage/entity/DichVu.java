package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "dich_vu")
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
    private String hinhAnhUrl;

    @ManyToMany(mappedBy = "dichVus")
    private List<KhachSan> khachSans;
}
