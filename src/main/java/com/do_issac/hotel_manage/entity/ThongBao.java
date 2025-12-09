package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime ngayTao;
    private boolean daXem = false;

    @ManyToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan taiKhoan;

    private Long actorId;
}
