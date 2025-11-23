package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.request.DichVuRequest;
import com.do_issac.hotel_manage.dto.request.KhachSanRequest;
import com.do_issac.hotel_manage.dto.request.LoaiPhongRequest;
import com.do_issac.hotel_manage.model.CustomUserDetails;
import com.do_issac.hotel_manage.service.DichVuService;
import com.do_issac.hotel_manage.service.KhachSanService;
import com.do_issac.hotel_manage.service.LoaiPhongService;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chu-khach-san")
@RequiredArgsConstructor
public class ChuKhachSanController {
    private final KhachSanService khachSanService;
    private final LoaiPhongService loaiPhongService;
    private final DichVuService dichVuService;

    @PostMapping("/khach-san")
    public ApiResponse<?> create(@RequestBody KhachSanRequest request) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return khachSanService.createHotel(request, user.getId());
    }

    @PutMapping("/khach-san/{id}")
    public ApiResponse<?> update(@PathVariable Long id,
                                 @RequestBody KhachSanRequest request) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return khachSanService.updateHotel(id, request, user.getId());
    }

    @GetMapping("/khach-san")
    public ApiResponse<?> getAll() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return khachSanService.getAllHotelsByOwner(user.getId());
    }
    @GetMapping("/khach-san/{id}")
    public ApiResponse<?> getDetail(@PathVariable Long id) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return khachSanService.getHotelDetail(id, user.getId());
    }

    @GetMapping("/loai-phong/khach-san/{khachSanId}")
    public ApiResponse<?> getLoaiPhongByKhachSan(@PathVariable Long khachSanId) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loaiPhongService.getLoaiPhongByKhachSan(khachSanId, user.getId());
    }

    @PostMapping("/loai-phong/khach-san/{khachSanId}")
    public ApiResponse<?> createLoaiPhongForKhachSan(@PathVariable Long khachSanId,
                                                     @RequestBody LoaiPhongRequest request) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loaiPhongService.createLoaiPhongForKhachSan(khachSanId, request, user.getId());
    }

    @GetMapping("/dich-vu/khach-san/{khachSanId}")
    public ApiResponse<?> getDichVuByKhachSan(@PathVariable Long khachSanId) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return dichVuService.getDichVuByKhachSan(khachSanId, user.getId());
    }

    @PostMapping("/dich-vu/khach-san/{khachSanId}")
    public ApiResponse<?> createDichVuForKhachSan(@PathVariable Long khachSanId,
                                                  @RequestBody DichVuRequest request) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return dichVuService.createDichVuForKhachSan(khachSanId, request, user.getId());
    }
}