package com.do_issac.hotel_manage.service;

import com.do_issac.hotel_manage.dto.request.LoaiPhongRequest;
import com.do_issac.hotel_manage.util.ApiResponse;

public interface LoaiPhongService {

    ApiResponse<?> createLoaiPhongForKhachSan(Long khachSanId, LoaiPhongRequest request, Long chuKhachSanId);

    ApiResponse<?> getLoaiPhongByKhachSan(Long khachSanId, Long chuKhachSanId);
}
