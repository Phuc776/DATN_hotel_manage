package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.service.KhachSanService;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final KhachSanService khachSanService;

    @PostMapping("/khach-san/{id}/approve")
    public ApiResponse<?> approve(@PathVariable Long id) {
        return khachSanService.approve(id);
    }

    @PostMapping("/khach-san/{id}/reject")
    public ApiResponse<?> reject(@PathVariable Long id) {
        return khachSanService.reject(id);
    }

    @GetMapping("/khach-san/pending")
    public ApiResponse<?> pending() {
        return khachSanService.getPending();
    }

    @GetMapping("/khach-san")
    public ApiResponse<?> getAll() {
        return khachSanService.adminGetAll();
    }

    @GetMapping("/khach-san/{id}")
    public ApiResponse<?> detail(@PathVariable Long id) {
        return khachSanService.adminGetDetail(id);
    }
}
