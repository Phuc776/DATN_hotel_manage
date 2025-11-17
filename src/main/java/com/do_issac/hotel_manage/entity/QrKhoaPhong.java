package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_khoa_phong")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QrKhoaPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String maJWT;
    private LocalDateTime thoiGianTao;
    private LocalDateTime hieuLucTu;
    private LocalDateTime hieuLucDen;

    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "datPhongId", nullable = false)
    private ChiTietDatPhong datPhong;
}
