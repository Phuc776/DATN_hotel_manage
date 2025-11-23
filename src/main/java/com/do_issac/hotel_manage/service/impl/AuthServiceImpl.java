package com.do_issac.hotel_manage.service;

import com.do_issac.hotel_manage.dto.request.LoginRequest;
import com.do_issac.hotel_manage.dto.response.LoginResponse;
import com.do_issac.hotel_manage.dto.request.RegisterRequest;
import com.do_issac.hotel_manage.dto.response.RegisterResponse;
import com.do_issac.hotel_manage.entity.KhachHang;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.VaiTro;
import com.do_issac.hotel_manage.jwt.JwtTokenProvider;
import com.do_issac.hotel_manage.mapper.TaiKhoanMapper;
import com.do_issac.hotel_manage.model.CustomUserDetails;
import com.do_issac.hotel_manage.repository.KhachHangRepository;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final TaiKhoanRepository taiKhoanRepository;
    private final TaiKhoanMapper taiKhoanMapper;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;
    private final KhachHangRepository khachHangRepository;


    @Override
    public LoginResponse login(LoginRequest request) {
        TaiKhoan tk = taiKhoanRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));
        if (!encoder.matches(request.getMatKhau(), tk.getMatKhau()))
            throw new RuntimeException("Sai mật khẩu");


        String token = jwtProvider.generateToken(new CustomUserDetails(tk));

        tk.setLanCuoiDangNhap(java.time.LocalDateTime.now());
        tk.setTrangThai(true);
        taiKhoanRepository.save(tk);

        return new LoginResponse(token, tk.getVaiTro().name());
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (taiKhoanRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        if(request.getVaiTro() == VaiTro.ADMIN || request.getVaiTro() == VaiTro.NHAN_VIEN) {
            throw new RuntimeException("Không thể đăng ký");
        }

        // 1. Tạo tài khoản
        TaiKhoan tk = new TaiKhoan();
        tk.setEmail(request.getEmail());
        tk.setMatKhau(encoder.encode(request.getMatKhau()));
        tk.setVaiTro(request.getVaiTro());
        tk.setNgayTao(LocalDateTime.now());
        tk.setTrangThai(true);

        taiKhoanRepository.save(tk);

        // 2. Nếu là khách hàng → tạo KhachHang
        if (request.getVaiTro() == VaiTro.KHACH_HANG) {

            if(request.getSoDienThoai() == null || request.getEmail() == null) {
                throw new RuntimeException("Thiếu thông tin khách hàng");
            }
            if(khachHangRepository.existsBySoDienThoai(request.getSoDienThoai())) {
                throw new RuntimeException("Số điện thoại đã tồn tại");
            }

            KhachHang kh = new KhachHang();
            kh.setHoTen(request.getHoTen() != null ? request.getHoTen() : "Người dùng mới");
            kh.setSoDienThoai(request.getSoDienThoai());
            kh.setEmail(request.getEmail());
            kh.setTaiKhoan(tk);

            khachHangRepository.save(kh);

            // Gán ngược (nếu muốn trả response đầy đủ)
            tk.setKhachHang(kh);
        }

        return taiKhoanMapper.toRegisterResponse(tk);
    }
}
