package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "phong")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Phong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String soPhong;

    @Enumerated(EnumType.STRING)
    private TrangThaiPhong trangThaiPhong;

    @ManyToOne
    @JoinColumn(name = "khachSanId", nullable = false)
    private KhachSan khachSan;

    @ManyToOne
    @JoinColumn(name = "loaiPhongId", nullable = false)
    private LoaiPhong loaiPhong;

    @OneToMany(mappedBy = "phong")
    private List<ChiTietDatPhong> chiTietDatPhongs;
}
