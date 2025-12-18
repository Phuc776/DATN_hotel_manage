package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.KhachHangUpdateProfileRequest;
import com.do_issac.hotel_manage.dto.response.KhachHangResponse;
import com.do_issac.hotel_manage.entity.KhachHang;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.mapper.KhachHangMapper;
import com.do_issac.hotel_manage.repository.KhachHangRepository;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import com.do_issac.hotel_manage.util.AuthUtil;
import com.do_issac.hotel_manage.util.CccdCryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final AuthUtil authUtil;
    private final CccdCryptoUtil cccdCryptoUtil;

    private final KhachHangMapper khachHangMapper;

    @Transactional(readOnly = true)
    public KhachHangResponse getMyProfile() {
        Long userId = authUtil.getCurrentUserId();

        KhachHang khachHang = khachHangRepository
                .findByTaiKhoan_Id(userId);

        return khachHangMapper.toResponse(khachHang);
    }

    public String revealCCCD() {
        Long userId = authUtil.getCurrentUserId();

        KhachHang khachHang = khachHangRepository
                .findByTaiKhoan_Id(userId);

        String decryptedCccd = cccdCryptoUtil.decrypt(khachHang.getCCCD());

        return decryptedCccd;
    }

    @Transactional
    public KhachHangResponse updateMyProfile(KhachHangUpdateProfileRequest request) {
        Long userId = authUtil.getCurrentUserId();

        KhachHang khachHang = khachHangRepository
                .findByTaiKhoan_Id(userId);

        TaiKhoan taiKhoan = khachHang.getTaiKhoan();

        // 1. Update họ tên
        if (request.getHoTen() != null && !request.getHoTen().isBlank()) {
            taiKhoan.setHoTen(request.getHoTen());
        }

        // 2. Update số điện thoại
        if (request.getSoDienThoai() != null && !request.getSoDienThoai().isBlank()) {
            taiKhoan.setSoDienThoai(request.getSoDienThoai());
        }

        // 3. Update CCCD (RAW → ENCRYPT)
        if (request.getCccd() != null && !request.getCccd().isBlank()) {

            String encryptedCccd = cccdCryptoUtil.encrypt(request.getCccd());

            // tránh trùng CCCD
            if (khachHangRepository.existsByCCCD(encryptedCccd)) {
                throw new RuntimeException("CCCD đã được sử dụng");
            }

            khachHang.setCCCD(encryptedCccd);
            khachHang.setNgayXacThucCCCD(LocalDateTime.now());
        }

        taiKhoanRepository.save(taiKhoan);
        khachHangRepository.save(khachHang);

        return khachHangMapper.toResponse(khachHang);
    }
}
