package com.do_issac.hotel_manage.service;

import com.do_issac.hotel_manage.dto.request.KhachSanRequest;
import com.do_issac.hotel_manage.dto.response.KhachSanResponse;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.util.ApiResponse;

import java.util.List;

public interface KhachSanService {
    ApiResponse<List<KhachSan>> getAllHotelsByOwner(Long ownerId);
    ApiResponse<KhachSan> getHotelDetail(Long id, Long ownerId);
    ApiResponse<KhachSanResponse> createHotel(KhachSanRequest request, Long ownerId);
    ApiResponse<KhachSanResponse> updateHotel(Long khachSanId, KhachSanRequest request, Long userId);
    ApiResponse<List<KhachSanResponse>> adminGetAll();
    ApiResponse<KhachSan> adminGetDetail(Long id);
    ApiResponse<List<KhachSanResponse>> getPending();
    ApiResponse<Void> approve(Long id);
    ApiResponse<Void> reject(Long id);
}
