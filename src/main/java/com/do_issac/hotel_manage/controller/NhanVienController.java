package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.response.NhanVienResponse;
import com.do_issac.hotel_manage.entity.NhanVien;
import com.do_issac.hotel_manage.service.impl.DatPhongService;
import com.do_issac.hotel_manage.service.impl.PhongService;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nhan-vien")
@RequiredArgsConstructor
public class NhanVienController {
    private final AuthUtil authUtil;
    private final PhongService phongService;
    private final DatPhongService datPhongService;


    @GetMapping("/phong")
    public ApiResponse<?> getAllPhongByKhachSan() {
        Long userId = authUtil.getCurrentUserId();

        return phongService.getAllRoomsForNhanVien(userId);
    }

    @GetMapping("/phong/{id}")
    public ApiResponse<?> getPhongDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return phongService.getRoomById(userId, id);
    }

    @GetMapping("/booking")
    public ApiResponse<?> getAllBookings() {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.getAllBookingsForNhanVien(userId);
    }
    @PostMapping("/booking/{id}/check-in")
    public ApiResponse<?> checkIn(@PathVariable Long id) {
        datPhongService.checkIn(id);
        return ApiResponse.success("Check-in thành công", null);
    }
    @PostMapping("/booking/{id}/check-out")
    public ApiResponse<?> checkOut(@PathVariable Long id) {
        datPhongService.checkOut(id);
        return ApiResponse.success("Check-out thành công", null);
    }
    @PostMapping("/booking/{id}/cancel")
    public ApiResponse<?> huyBooking(@PathVariable Long id) {
        datPhongService.huyBooking(id);
        return ApiResponse.success("Hủy booking thành công", null);
    }

}
