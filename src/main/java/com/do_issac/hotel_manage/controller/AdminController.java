package com.do_issac.hotel_manage.controller;


import com.do_issac.hotel_manage.dto.request.CreateAdminRequest;
import com.do_issac.hotel_manage.entity.TrangThaiBaiDang;
import com.do_issac.hotel_manage.service.impl.BaiDangPhongService;
import com.do_issac.hotel_manage.service.impl.KhachSanService;
import com.do_issac.hotel_manage.service.impl.NotificationQueryService;
import com.do_issac.hotel_manage.service.impl.TaiKhoanService;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final KhachSanService khachSanService;
    private final TaiKhoanService taiKhoanService;
    private final BaiDangPhongService baiDangPhongService;
    private final AuthUtil authUtil;
    private final NotificationQueryService notificationQueryService;

    @GetMapping("/thong-bao/all")
    public ApiResponse<?> getNotifications() {
        return notificationQueryService.getAllForAdmin();
    }

    @GetMapping("/thong-bao")
    public ApiResponse<?> getMyNotifications() {
        Long adminId = authUtil.getCurrentUserId();
        return notificationQueryService.getByUser(adminId);
    }

    @PostMapping("/khach-san/{id}/approve")
    public ApiResponse<?> approve(@PathVariable Long id) {
        Long adminId = authUtil.getCurrentUserId();
        return khachSanService.approve(id, adminId);
    }

    @PostMapping("/khach-san/{id}/reject")
    public ApiResponse<?> reject(@PathVariable Long id) {
        Long adminId = authUtil.getCurrentUserId();
        return khachSanService.reject(id, adminId);
    }

    @GetMapping("/khach-san/pending")
    public ApiResponse<?> getPendingHotel() {
        return khachSanService.getPending();
    }

    @GetMapping("/khach-san")
    public ApiResponse<?> getAllHotel() {
        return khachSanService.getAll();
    }

    @GetMapping("/khach-san/{id}")
    public ApiResponse<?> getDetailHotel(@PathVariable Long id) {
        return khachSanService.getDetail(id);
    }
    @GetMapping("/khach-san/{khachSanid}/bai-dang-phong")
    public ApiResponse<?> getBaiDangPhongByKhachSan(@PathVariable Long khachSanid) {
        return baiDangPhongService.getPostsByKhachSanId(khachSanid);
    }

    @GetMapping("/tai-khoan")
    public ApiResponse<?> getAllUsers() {
        return taiKhoanService.getAllUsers();
    }
    @GetMapping("/tai-khoan/{id}")
    public ApiResponse<?> getUserById(@PathVariable Long id) {
        return taiKhoanService.getUserById(id);
    }
    @PostMapping("/tai-khoan/admin")
    public ApiResponse<?> createAdminUser(@RequestBody CreateAdminRequest rq) {
        return taiKhoanService.createAdminUser(rq);
    }
    @PostMapping("/tai-khoan/{id}/change-status")
    public ApiResponse<?> setUserStatus(@PathVariable Long id) {
        return taiKhoanService.setUserStatus(id);
    }

    @GetMapping("/bai-dang-phong")
    public ApiResponse<?> filter(
            @RequestParam(value = "chu-khach-san", required = false) Long ownerId,
            @RequestParam(value = "trang-thai", required = false) String tt) {

        if (ownerId != null && tt != null) {
            return baiDangPhongService.getByOwnerIdAndTrangThai(ownerId, TrangThaiBaiDang.fromString(tt));
        }

        if (ownerId != null) {
            return baiDangPhongService.getByOwnerId(ownerId);
        }

        if (tt != null) {
            return baiDangPhongService.getByTrangThai(TrangThaiBaiDang.fromString(tt));
        }

        return baiDangPhongService.getAll();
    }


    @GetMapping("/bai-dang-phong/{id}")
    public ApiResponse<?> getBaiDangPhongById(@PathVariable Long id) {
        return baiDangPhongService.getById(id);
    }

    @PostMapping("/bai-dang-phong/{id}/approve")
    public ApiResponse<?> approveBaiDangPhong(@PathVariable Long id) {
        return baiDangPhongService.approve(id);
    }
    @PostMapping("/bai-dang-phong/{id}/reject")
    public ApiResponse<?> rejectBaiDangPhong(@PathVariable Long id) {
        return baiDangPhongService.reject(id);
    }

}
