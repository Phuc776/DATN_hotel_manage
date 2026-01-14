package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
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

    private String maGiaoDich; // vnp_TxnRef

    private LocalDateTime ngayThanhToan;

    @Enumerated(EnumType.STRING)
    private TrangThaiThanhToan trangThai;


    @ManyToOne
    @JoinColumn(name = "phienLuuTruId", nullable = false)
    private PhienLuuTru phienLuuTru;
}
