package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "thong_bao")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String noiDung;
    private String ngayTao;
    private boolean daXem;

    @ManyToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan taiKhoan;
}
