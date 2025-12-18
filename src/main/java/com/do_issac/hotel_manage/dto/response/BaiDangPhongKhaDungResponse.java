package com.do_issac.hotel_manage.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaiDangPhongKhaDungResponse {
    private Long baiDangPhongId;
    private String tieuDe;
    private String moTa;

    private String tenLoaiPhong;
    private Double giaLoaiPhong;
    private int soNguoiLon;
    private int soTreEm;

    private String tenKhachSan;
    private String diaChiKhachSan;

    private int tongSoPhong;
    private int soPhongCon;

    private boolean conPhong;
}
