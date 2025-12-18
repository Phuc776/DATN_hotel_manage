package com.do_issac.hotel_manage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "phong")
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
    @JsonIgnore
    private List<ChiTietDatPhong> chiTietDatPhongs;
}
