package com.do_issac.hotel_manage.service;

import com.do_issac.hotel_manage.dto.request.DichVuRequest;
import com.do_issac.hotel_manage.util.ApiResponse;

public interface DichVuService {

    ApiResponse<?> createDichVuForKhachSan(Long khachSanId, DichVuRequest request, Long chuKhachSanId);

    ApiResponse<?> getDichVuByKhachSan(Long khachSanId, Long chuKhachSanId);
}

