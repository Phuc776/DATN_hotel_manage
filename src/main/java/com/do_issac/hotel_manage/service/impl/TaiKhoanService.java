package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.CreateAdminRequest;
import com.do_issac.hotel_manage.dto.response.TaiKhoanResponse;

import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.VaiTro;
import com.do_issac.hotel_manage.mapper.TaiKhoanMapper;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaiKhoanService {

    private final TaiKhoanMapper mapper;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<List<TaiKhoanResponse>> getAllUsers() {
        return ApiResponse.success(
                "Danh sách tài khoản",
                taiKhoanRepository.findAll().stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }


    public ApiResponse<TaiKhoanResponse> getUserById(Long userId) {
        return ApiResponse.success(
                "Chi tiết tài khoản",
                mapper.toResponse(
                        taiKhoanRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"))
                )
        );
    }


    public ApiResponse<TaiKhoanResponse> createAdminUser(CreateAdminRequest request) {
        if (taiKhoanRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setEmail(request.getEmail());
        tk.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        tk.setHoTen("New Admin");
        tk.setVaiTro(VaiTro.ADMIN);
        tk.setNgayTao(LocalDateTime.now());
        tk.setTrangThai(true);

        taiKhoanRepository.save(tk);
        return ApiResponse.success("Tạo tài khoản admin thành công", mapper.toResponse(tk));
    }

    public ApiResponse<TaiKhoanResponse> setUserStatus(Long userId) {
        TaiKhoan tk = taiKhoanRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        tk.setTrangThai(!tk.isTrangThai());
        taiKhoanRepository.save(tk);
        return ApiResponse.success("Cập nhật trạng thái tài khoản thành công", mapper.toResponse(tk));
    }
}
