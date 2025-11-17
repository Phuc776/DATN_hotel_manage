package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double soTien;
    private String phuongThuc;
    private LocalDateTime ngayThanhToan;

    @ManyToOne
    @JoinColumn(name = "datPhongId", nullable = false)
    private ChiTietDatPhong datPhong;
}
