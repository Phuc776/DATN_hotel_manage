package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "khach_hang")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String CCCD;

    private LocalDateTime ngayXacThucCCCD;


    @OneToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "khachHang")
    private List<ChiTietDatPhong> chiTietDatPhongs;

    @OneToMany(mappedBy = "khachHang")
    private List<DanhGia> danhGias;
}
