package com.do_issac.hotel_manage.dto.response;

import com.do_issac.hotel_manage.entity.VaiTro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanResponse {
    private Long id;
    private String email;
    private String hoTen;
    private String soDienThoai;
    private VaiTro vaiTro;
    private LocalDateTime ngayTao;
    private boolean trangThai;
}
