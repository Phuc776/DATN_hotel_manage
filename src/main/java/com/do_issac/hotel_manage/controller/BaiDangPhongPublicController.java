package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.request.KiemTraPhongTrongRequest;
import com.do_issac.hotel_manage.dto.response.BaiDangPhongResponse;
import com.do_issac.hotel_manage.service.impl.BaiDangPhongService;
import com.do_issac.hotel_manage.service.impl.DatPhongService;
import com.do_issac.hotel_manage.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bai-dang-phong/public")
@RequiredArgsConstructor
public class BaiDangPhongPublicController {

    private final BaiDangPhongService baiDangPhongService;
    private final DatPhongService datPhongService;

    @PostMapping("/booking/check-availability")
    public ApiResponse<?> checkAvailability(@RequestBody @Valid KiemTraPhongTrongRequest req) {

        return ApiResponse.success(
                "Danh sách phòng trống",
                datPhongService.checkAvailability(req)
        );
    }
    @PostMapping("/{id}/availability")
    public ApiResponse<?> getDetailAvailability(
            @PathVariable Long id,
            @RequestBody @Valid KiemTraPhongTrongRequest req
    ) {
        return ApiResponse.success(
                "Chi tiết bài đăng phòng",
                datPhongService.getDetailAvailability(id, req)
        );
    }



    @GetMapping
    public ApiResponse<?> getAllApproved() {
        return baiDangPhongService.getAllApproved();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getById(@PathVariable Long id) {
        return baiDangPhongService.getApprovedById(id);
    }

    @GetMapping("/khach-san/{khachSanId}")
    public ApiResponse<?> getApprovedByKhachSanId(@PathVariable Long khachSanId) {
        return baiDangPhongService.getApprovedByKhachSanId(khachSanId);
    }
}

