package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "danh_gia")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int diemDanhGia;
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "khachHangId")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "khachSanId")
    private KhachSan khachSan;
}
