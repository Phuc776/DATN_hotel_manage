package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.service.impl.PhongService;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nhan-vien")
@RequiredArgsConstructor
public class NhanVienController {
    private final AuthUtil authUtil;
    private final PhongService phongService;

    @GetMapping("/khach-san/{id}/phong")
    public ApiResponse<?> getAllPhongByKhachSan(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return phongService.getAllRooms(userId, id);
    }

    @GetMapping("/phong/{id}")
    public ApiResponse<?> getPhongDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return phongService.getRoomById(userId, id);
    }
}
