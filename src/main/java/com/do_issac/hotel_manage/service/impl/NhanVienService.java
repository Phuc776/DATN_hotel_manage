package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.CreateNhanVienRequest;
import com.do_issac.hotel_manage.dto.response.NhanVienResponse;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.NhanVien;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.VaiTro;
import com.do_issac.hotel_manage.mapper.NhanVienMapper;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.repository.NhanVienRepository;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NhanVienService {
    private final NhanVienMapper mapper;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhachSanRepository khachSanRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<List<NhanVienResponse>> getAllNhanVien(Long chuKhachSanId) {
        List<NhanVien> nhanViens = nhanVienRepository.findAllByKhachSan_ChuKhachSan_Id(chuKhachSanId);
        return ApiResponse.success("Lấy danh sách nhân viên thành công",
                nhanViens.stream()
                        .map(mapper::toResponse)
                        .toList());
    }

    public ApiResponse<List<NhanVienResponse>> getNhanVienByKhachSan(Long khachSanId, Long chuKhachSanId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));
        if(!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền xem nhân viên của khách sạn này");
        }
        List<NhanVien> nhanViens = nhanVienRepository.findAllByKhachSan_Id(khachSanId);
        return ApiResponse.success("Lấy danh sách nhân viên thành công",
                nhanViens.stream()
                        .map(mapper::toResponse)
                        .toList());
    }

    public ApiResponse<NhanVienResponse> getNhanVienDetail(Long nhanVienId, Long chuKhachSanId) {
        NhanVien nv = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        if(!nv.getKhachSan().getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền xem nhân viên này");
        }
        return ApiResponse.success("Lấy thông tin nhân viên thành công", mapper.toResponse(nv));
    }

    public ApiResponse<NhanVienResponse> addNhanVienToKhachSan(Long chuKhachSanId, CreateNhanVienRequest rq) {
        KhachSan ks = khachSanRepository.findById(rq.getKhachSanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if(!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền thêm nhân viên cho khách sạn này");
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setEmail(rq.getEmail());
        tk.setMatKhau(passwordEncoder.encode("123456")); // mật khẩu tạm
        tk.setHoTen(rq.getHoTen());
        tk.setSoDienThoai(rq.getSoDienThoai());
        tk.setVaiTro(VaiTro.NHAN_VIEN);
        tk.setNgayTao(LocalDateTime.now());
        tk.setTrangThai(true);

        taiKhoanRepository.save(tk);

        NhanVien nv = new NhanVien();

        nv.setChucVu(rq.getChucVu());
        nv.setKhachSan(ks);
        nv.setTaiKhoan(tk);

        nhanVienRepository.save(nv);
        return ApiResponse.success("Thêm nhân viên thành công", mapper.toResponse(nv));
    }


    public ApiResponse<Void> setTrangThaiNhanVien(Long nhanVienId, Long chuKhachSanId) {
        NhanVien nv = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        if(!nv.getKhachSan().getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền thay đổi trạng thái nhân viên này");
        }
        TaiKhoan tk = nv.getTaiKhoan();
        tk.setTrangThai(!tk.isTrangThai());
        taiKhoanRepository.save(tk);
        return ApiResponse.success("Cập nhật trạng thái nhân viên thành công", null);
    }
}
