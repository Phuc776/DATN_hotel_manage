package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "danh_gia")
@Getter
@Setter
@NoArgsConstructor
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int diemDanhGia;
    private String noiDung;

    private LocalDateTime ngayTao;

    @ManyToOne
    @JoinColumn(name = "khachHangId")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "khachSanId")
    private KhachSan khachSan;
}
