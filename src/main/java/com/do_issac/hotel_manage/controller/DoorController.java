package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.response.PhongResponse;
import com.do_issac.hotel_manage.mapper.PhongMapper;
import com.do_issac.hotel_manage.repository.PhongRepository;
import com.do_issac.hotel_manage.service.impl.PhongService;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/door")
@RequiredArgsConstructor
public class DoorController {

    private final PhongRepository phongRepository;
    private final PhongService phongService;

    private final PhongMapper phongMapper;

    @GetMapping("/rooms/{khachSanId}")
    public List<PhongResponse> getRooms(@PathVariable Long khachSanId) {
        return phongMapper.toResponseList(
                phongRepository.findByKhachSan_Id(khachSanId)
        );
    }

    @PostMapping("room/{idPhong}/open-door")
    public ApiResponse<?> openDoor(@PathVariable Long idPhong, @RequestParam String token) {
        phongService.moCuaBangQr(idPhong, token);
        return ApiResponse.success("Mở cửa thành công", null);
    }
}

