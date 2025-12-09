package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.request.*;

import com.do_issac.hotel_manage.entity.TrangThaiBaiDang;
import com.do_issac.hotel_manage.service.impl.*;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chu-khach-san")
@RequiredArgsConstructor
public class ChuKhachSanController {
    private final AuthUtil authUtil;
    private final KhachSanService khachSanService;
    private final LoaiPhongService loaiPhongService;
    private final DichVuService dichVuService;
    private final NhanVienService nhanVienService;
    private final BaiDangPhongService baiDangPhongService;
    private final PhongService phongService;
    private final NotificationQueryService notificationQueryService;

    @GetMapping("/thong-bao")
    public ApiResponse<?> getNotifications() {
        Long userId = authUtil.getCurrentUserId();
        return notificationQueryService.getByUser(userId);
    }

    @PostMapping("/khach-san")
    public ApiResponse<?> create(@RequestBody KhachSanRequest request) {
        Long id = authUtil.getCurrentUserId();
        return khachSanService.createHotel(request, id);
    }

    @PutMapping("/khach-san/{id}")
    public ApiResponse<?> update(@PathVariable Long id,
                                 @RequestBody KhachSanRequest request) {
        Long userId = authUtil.getCurrentUserId();
        return khachSanService.updateHotel(id, request, userId);
    }

    @PutMapping("/khach-san/{id}/stop")
    public ApiResponse<?> stopOperatingHotel(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return khachSanService.stopHotelStatus(id, userId);
    }

    @GetMapping("/khach-san")
    public ApiResponse<?> getAll() {
        Long id = authUtil.getCurrentUserId();
        return khachSanService.getAllHotelsByOwner(id);
    }
    @GetMapping("/khach-san/{id}")
    public ApiResponse<?> getDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return khachSanService.getHotelDetail(id, userId);
    }

    @GetMapping("/khach-san/{id}/phong")
    public ApiResponse<?> getAllPhongByKhachSan(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return phongService.getAllRooms(userId, id);
    }

    @GetMapping("/loai-phong")
    public ApiResponse<?> getAllLoaiPhong() {
        Long userId = authUtil.getCurrentUserId();
        return loaiPhongService.getAllLoaiPhong(userId);
    }

    @GetMapping("/loai-phong/{id}")
    public ApiResponse<?> getLoaiPhongDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return loaiPhongService.getLoaiPhongById(id, userId);
    }

    @GetMapping("/khach-san/{khachSanId}/loai-phong")
    public ApiResponse<?> getLoaiPhongByKhachSan(@PathVariable Long khachSanId) {
        Long userId = authUtil.getCurrentUserId();
        return loaiPhongService.getLoaiPhongByKhachSan(khachSanId, userId);
    }

    @PostMapping("/khach-san/{khachSanId}/loai-phong")
    public ApiResponse<?> createLoaiPhongForKhachSan(@PathVariable Long khachSanId,
                                                     @RequestBody LoaiPhongRequest request) {
        Long userId = authUtil.getCurrentUserId();
        return loaiPhongService.createLoaiPhongForKhachSan(khachSanId, request, userId);
    }

    @GetMapping("/dich-vu")
    public ApiResponse<?> getAllDichVu() {
        Long userId = authUtil.getCurrentUserId();
        return dichVuService.getAllDichVu(userId);
    }

    @GetMapping("/dich-vu/{id}")
    public ApiResponse<?> getDichVuDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return dichVuService.getDichVuDetail(id, userId);
    }

    @GetMapping("/khach-san/{khachSanId}/dich-vu")
    public ApiResponse<?> getDichVuByKhachSan(@PathVariable Long khachSanId) {
        Long userId = authUtil.getCurrentUserId();
        return dichVuService.getDichVuByKhachSan(khachSanId, userId);
    }

    @PostMapping("/khach-san/{khachSanId}/dich-vu")
    public ApiResponse<?> createDichVuForKhachSan(@PathVariable Long khachSanId,
                                                  @RequestBody DichVuRequest request) {
        Long userId = authUtil.getCurrentUserId();
        return dichVuService.createDichVuForKhachSan(khachSanId, request, userId);
    }

    @PutMapping("/dich-vu/{id}")
    public ApiResponse<?> updateDichVuForKhachSan(@PathVariable Long id,
                                                  @RequestBody DichVuRequest request) {
        Long userId = authUtil.getCurrentUserId();
        return dichVuService.updateDichVuForKhachSan(id, request, userId);
    }

    @GetMapping("/nhan-vien")
    public ApiResponse<?> getAllNhanVien() {
        Long userId = authUtil.getCurrentUserId();
        return nhanVienService.getAllNhanVien(userId);
    }

    @GetMapping("nhan-vien/{id}")
    public ApiResponse<?> getNhanVienDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return nhanVienService.getNhanVienDetail(id, userId);
    }
    @PostMapping("/nhan-vien/{id}/status")
    public ApiResponse<?> setNhanVienStatus(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return nhanVienService.setTrangThaiNhanVien(id, userId);
    }

    @GetMapping("/khach-san/{khachSanId}/nhan-vien")
    public ApiResponse<?> getNhanVienByKhachSan(@PathVariable Long khachSanId) {
        Long userId = authUtil.getCurrentUserId();
        return nhanVienService.getNhanVienByKhachSan(khachSanId, userId);
    }

    @PostMapping("/khach-san/{khachSanId}/nhan-vien")
    public ApiResponse<?> addNhanVienToKhachSan(@PathVariable Long khachSanId,
                                                @RequestBody CreateNhanVienRequest request) {
        Long userId = authUtil.getCurrentUserId();
        return nhanVienService.addNhanVienToKhachSan(userId, request);
    }

    @GetMapping("/bai-dang-phong")
    public ApiResponse<?> filter(
                @RequestParam(required = false) TrangThaiBaiDang trangThai,
                @RequestParam(required = false) Long khachSanId) {
        Long userId = authUtil.getCurrentUserId();
        if (khachSanId != null && trangThai != null) {
            return baiDangPhongService.getByKhachSanIdAndTrangThai(userId, khachSanId, trangThai);
        }
        if (khachSanId != null) {
            return baiDangPhongService.getPostsByKhachSanId(userId, khachSanId);
        }
        if (trangThai != null) {
            return baiDangPhongService.getByTrangThai(userId, trangThai);
        }
        return baiDangPhongService.getAll(userId);
    }

    @GetMapping("/bai-dang-phong/{id}")
    public ApiResponse<?> getBaiDangPhongById(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return baiDangPhongService.getById(userId, id);
    }

    @PostMapping("/bai-dang-phong")
    public ApiResponse<?> createBaiDangPhong(@RequestBody BaiDangPhongRequest request) {
        Long userId = authUtil.getCurrentUserId();
        return baiDangPhongService.create(userId, request);
    }

    @GetMapping("/phong/{id}")
    public ApiResponse<?> getPhongDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return phongService.getRoomById(userId, id);
    }
}