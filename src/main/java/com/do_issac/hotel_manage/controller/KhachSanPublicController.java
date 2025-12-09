package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.response.KhachSanResponse;
import com.do_issac.hotel_manage.service.impl.KhachSanService;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/khach-san/public")
@RequiredArgsConstructor
public class KhachSanPublicController {
    private final KhachSanService khachSanService;

    @GetMapping
    public ApiResponse<?> getAllApproved() {
        return khachSanService.getAllApproved();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getById(@PathVariable Long id) {
        return khachSanService.getApprovedById(id);
    }
}
