package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.request.ChiTietDatPhongRequest;
import com.do_issac.hotel_manage.dto.request.KhachHangUpdateProfileRequest;
import com.do_issac.hotel_manage.service.impl.DatPhongService;
import com.do_issac.hotel_manage.service.impl.KhachHangService;
import com.do_issac.hotel_manage.service.impl.PhongService;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {
    private final KhachHangService khachHangService;
    private final DatPhongService datPhongService;
    private final PhongService phongService;
    private final AuthUtil authUtil;

    @GetMapping("/profile")
    public ApiResponse<?> getMyProfile() {
        return ApiResponse.success("Lấy thông tin khách hàng thành công", khachHangService.getMyProfile());
    }
    @GetMapping("/profile/reveal-cccd")
    public ApiResponse<?> revealCccd() {
        return ApiResponse.success("Lấy CCCD khách hàng thành công", khachHangService.revealCCCD());
    }

    @PutMapping("/profile")
    public ApiResponse<?> updateMyProfile(
            @RequestBody KhachHangUpdateProfileRequest request
    ) {
        return ApiResponse.success("Cập nhật thông tin khách hàng thành công", khachHangService.updateMyProfile(request));
    }


    @PostMapping("/booking")
    public ApiResponse<?> datPhong(
            @RequestBody ChiTietDatPhongRequest request
    ) {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.datPhong(userId, request);
    }

    @GetMapping("/booking")
    public ApiResponse<?> myBookings() {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.checkBookingHistory(userId);
    }

    @GetMapping("/booking/{id}")
    public ApiResponse<?> getBookingDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.getBookingDetail(userId, id);
    }

    @PostMapping("/booking/{id}/confirm")
    public ApiResponse<?> confirmReturnBooking(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        datPhongService.yeucauTraPhong(userId, id);
        return ApiResponse.success("Yêu cầu trả phòng thành công", null);
    }

    @PostMapping("/booking/{id}/cancel")
    public ApiResponse<?> cancelBooking(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        datPhongService.yeucauHuyBooking(userId, id);
        return ApiResponse.success("Yêu cầu hủy booking thành công", null);
    }

    @PostMapping("/phong/{idPhong}/open-door")
    public ApiResponse<?> openDoor(@PathVariable Long idPhong, @RequestParam String token) {
        phongService.moCuaBangQr(idPhong, token);
        return ApiResponse.success("Mở cửa thành công", null);
    }
}
