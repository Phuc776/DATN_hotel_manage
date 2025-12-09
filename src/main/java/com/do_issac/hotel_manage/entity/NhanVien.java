package com.do_issac.hotel_manage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nhan_vien")
@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chucVu;

    @OneToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan taiKhoan;

    @ManyToOne
    @JoinColumn(name = "khachSanId", nullable = false)
    private KhachSan khachSan;
}
