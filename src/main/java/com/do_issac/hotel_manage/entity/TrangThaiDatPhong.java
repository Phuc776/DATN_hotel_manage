package com.do_issac.hotel_manage.entity;

public enum TrangThaiDatPhong {
    CHO_XAC_NHAN, // Đã đặt phòng, chờ nhân viên xác nhận
    DA_XAC_NHAN, // Nhân viên check-in
    CHO_HUY, // Khách hàng yêu cầu hủy, chờ nhân viên duyệt hủy
    DA_HUY, // Đã hủy đặt phòng
    DANG_O, // Khách hàng đã dùng QR / bắt đầu ở
    CHO_TRA, // Khách hàng yêu cầu trả phòng, chờ nhân viên duyệt
    DA_TRA_PHONG // Khách hàng đã trả phòng
}
