package com.do_issac.hotel_manage.service;

import com.do_issac.hotel_manage.dto.LoginRequest;
import com.do_issac.hotel_manage.dto.LoginResponse;
import com.do_issac.hotel_manage.dto.RegisterRequest;
import com.do_issac.hotel_manage.dto.RegisterResponse;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.VaiTro;
import com.do_issac.hotel_manage.jwt.JwtTokenProvider;
import com.do_issac.hotel_manage.model.CustomUserDetails;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private TaiKhoanRepository taiKhoanRepository;
    private PasswordEncoder encoder;
    private JwtTokenProvider jwtProvider;


    @Override
    public LoginResponse login(LoginRequest request) {
        TaiKhoan tk = taiKhoanRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));
        if (!encoder.matches(request.getMatKhau(), tk.getMatKhau()))
            throw new RuntimeException("Sai mật khẩu");


        String token = jwtProvider.generateToken(new CustomUserDetails(tk));
        tk.setLanCuoiDangNhap(java.time.LocalDateTime.now());
        return new LoginResponse(token, tk.getVaiTro().name());
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (taiKhoanRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        TaiKhoan tk = new TaiKhoan();

        if(request.getVaiTro() == VaiTro.ADMIN) {
            throw new RuntimeException("Không thể đăng ký với vai trò ADMIN");
        }

        tk.setEmail(request.getEmail());
        tk.setMatKhau(encoder.encode(request.getMatKhau()));
        tk.setVaiTro(request.getVaiTro());
        tk.setNgayTao(java.time.LocalDateTime.now());
        taiKhoanRepository.save(tk);
        return new RegisterResponse(tk.getId(), tk.getEmail(), tk.getVaiTro().name(), tk.getNgayTao());
    }
}
