package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.PhongResponse;
import com.do_issac.hotel_manage.entity.*;
import com.do_issac.hotel_manage.jwt.QrJwtProvider;
import com.do_issac.hotel_manage.mapper.PhongMapper;
import com.do_issac.hotel_manage.repository.*;
import com.do_issac.hotel_manage.util.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhongService {
    private final PhongRepository phongRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NotificationService notificationService;
    private final NhanVienRepository nhanVienRepository;
    private final QRKhoaPhongRepository qrRepo;
    private final ChiTietDatPhongRepository datPhongRepository;

    private final QrJwtProvider qrJwtProvider;

    private final PhongMapper phongMapper;

    private boolean isOwnerOfHotel(Long ownerId, Long hotelId) {
        return taiKhoanRepository.existsByIdAndKhachSans_Id(ownerId, hotelId);
    }

    private boolean isEmployeeOfHotel(Long userId, Long hotelId) {
        return nhanVienRepository.existsByTaiKhoanIdAndKhachSanId(userId, hotelId);
    }

    private void validateHotelPermission(Long userId, Long hotelId) {
        if (!(isOwnerOfHotel(userId, hotelId) || isEmployeeOfHotel(userId, hotelId))) {
            throw new RuntimeException("Bạn không có quyền truy cập khách sạn này");
        }
    }

    public ApiResponse<List<PhongResponse>> getAllRoomsByOwner(Long userId) {
        List<Phong> phongList = phongRepository.findByKhachSan_ChuKhachSan_Id(userId);
        return ApiResponse.success("Lấy danh sách phòng thành công",
                phongMapper.toResponseList(phongList));
    }

    public ApiResponse<?> getAllRoomsForNhanVien(Long userId) {
        NhanVien nhanVien = nhanVienRepository.findByTaiKhoan_Id(userId);
        KhachSan khachSan = nhanVien.getKhachSan();

        List<Phong> phongList = phongRepository.findByKhachSan_Id(khachSan.getId());
        return ApiResponse.success("Lấy danh sách phòng thành công",
                phongMapper.toResponseList(phongList)
        );
    }

    public ApiResponse<List<PhongResponse>> getAllRoomsByHotelId(Long userId, Long hotelId) {

        validateHotelPermission(userId, hotelId);

        List<Phong> phongList = phongRepository.findByKhachSan_Id(hotelId);
        return ApiResponse.success("Lấy danh sách phòng thành công",
                phongMapper.toResponseList(phongList));
    }

    public ApiResponse<PhongResponse> getRoomById(Long userId, Long roomId) {
        Phong p = phongRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));

        validateHotelPermission(userId, p.getKhachSan().getId());

        return ApiResponse.success("Lấy thông tin phòng thành công",
                phongMapper.toResponse(p));
    }

    public ApiResponse<PhongResponse> updateRoomNumber(Long userId, Long roomId, String newRoomNumber) {

        Phong p = phongRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));

        validateHotelPermission(userId, p.getKhachSan().getId());

        p.setSoPhong(newRoomNumber);
        phongRepository.save(p);

        // Gửi thông báo cho chủ KS
        notificationService.push(
                p.getKhachSan().getChuKhachSan().getId(),
                "Phòng " + p.getId() + " đã được cập nhật số phòng thành: " + newRoomNumber
                , userId
        );

        return ApiResponse.success("Cập nhật số phòng thành công",
                phongMapper.toResponse(p));
    }

    @Transactional
    public void moCuaBangQr(Long idPhong, String token) {
        // 1. Parse & verify JWT
        Claims claims;
        try {
            claims = qrJwtProvider.parseQrToken(token);
        } catch (Exception e) {
            throw new RuntimeException("QR không hợp lệ hoặc đã hết hạn");
        }

        // 2. Lấy QR id (jti)
        Long qrId = Long.valueOf(claims.getId());

        QrKhoaPhong qr = qrRepo.findById(qrId)
                .orElseThrow(() -> new RuntimeException("QR không tồn tại"));

        // 3. Kiểm tra trạng thái QR
        if (!"HOAT_DONG".equals(qr.getTrangThai())) {
            throw new RuntimeException("QR đã bị thu hồi");
        }

        // 4. Kiểm tra thời gian hiệu lực
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(qr.getHieuLucTu()) || now.isAfter(qr.getHieuLucDen())) {
            throw new RuntimeException("QR chưa hoặc đã hết hiệu lực");
        }

        // 5. Kiểm tra booking & phòng
        ChiTietDatPhong b = qr.getDatPhong();
        Phong phong = b.getPhong();

        if (!phong.getId().equals(idPhong)) {
            throw new RuntimeException("QR không hợp lệ cho phòng này");
        }

        if (b.getTrangThai() == TrangThaiDatPhong.DA_XAC_NHAN) {

            b.setTrangThai(TrangThaiDatPhong.DANG_O);
            phong.setTrangThaiPhong(TrangThaiPhong.CO_KHACH);

            datPhongRepository.save(b);
            phongRepository.save(phong);
        } else {
            throw new RuntimeException("Booking không hợp lệ để mở cửa");
        }
    }
}
