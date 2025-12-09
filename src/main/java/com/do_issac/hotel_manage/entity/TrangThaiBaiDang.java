package com.do_issac.hotel_manage.entity;

public enum TrangThaiBaiDang {
    CHO_DUYET,
    DA_DUYET,
    TU_CHOI;

    public static TrangThaiBaiDang fromString(String tt) {
        for (TrangThaiBaiDang status : TrangThaiBaiDang.values()) {
            if (status.name().equalsIgnoreCase(tt)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không có trạng thái bài đăng phù hợp: " + tt);
    }
}
