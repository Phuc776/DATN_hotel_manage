package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime ngayTao;
    private double tongTien;
    private String noiDung;

    @OneToOne
    @JoinColumn(name = "datPhongId")
    private ChiTietDatPhong datPhong;

    @OneToMany
    @JoinColumn(name = "chiTietHoaDonId")
    private List<ChiTietHoaDon> chiTietHoaDons;
}
