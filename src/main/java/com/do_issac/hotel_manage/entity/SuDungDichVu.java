package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "su_dung_dich_vu")
@Getter
@Setter
@NoArgsConstructor
public class SuDungDichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phienLuuTruId", nullable = false)
    private PhienLuuTru phienLuuTru;

    @ManyToOne
    @JoinColumn(name = "dichVuId", nullable = false)
    private DichVu dichVu;

    private int soLuong;
    private double donGiaTaiThoiDiem;

    private LocalDateTime thoiDiemSuDung;
}
