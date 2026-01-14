package com.do_issac.hotel_manage.entity;

public enum TrangThaiPhien {
    TAO,   // tạo khi booking đầu tiên sinh ra
    MO,    // khi khách thực sự ở (mở cửa lần đầu)
    CHO_THANH_TOAN, // khi khách kết thúc ở nhưng chưa thanh toán
    DONG   // tất cả booking kết thúc
}
