package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.KhachSanRequest;
import com.do_issac.hotel_manage.dto.response.DetailKhachSanResponse;
import com.do_issac.hotel_manage.dto.response.KhachSanResponse;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.TrangThaiKhachSan;
import com.do_issac.hotel_manage.mapper.DetailKhachSanMapper;
import com.do_issac.hotel_manage.mapper.KhachSanMapper;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachSanService {
    private final KhachSanRepository khachSanRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final KhachSanMapper khachSanMapper;
    private final DetailKhachSanMapper detailKhachSanMapper;
    private final NotificationService notificationService;

    //CHU_KHACH_SAN: xem danh sách KS của mình
    public ApiResponse<List<KhachSanResponse>> getAllHotelsByOwner(Long ownerId) {
        return ApiResponse.success(
                "Danh sách khách sạn",
                khachSanMapper.toResponseList(khachSanRepository.findByChuKhachSanId(ownerId))
        );
    }
    //CHU_KHACH_SAN: xem danh sách KS hoạt động của mình
    public ApiResponse<List<KhachSanResponse>> getAllActiveHotelsByOwner(Long ownerId) {
        return ApiResponse.success(
                "Danh sách khách sạn hoạt động",
                khachSanMapper.toResponseList(
                        khachSanRepository.findByChuKhachSan_IdAndTrangThai(
                                ownerId,
                                TrangThaiKhachSan.DA_DUYET
                        )
                )
        );
    }

    //PUBLIC: xem danh sách KS đã duyệt
    public ApiResponse<List<KhachSanResponse>> getAllApproved() {
        List<KhachSan> list = khachSanRepository.findByTrangThai(TrangThaiKhachSan.DA_DUYET);
        return ApiResponse.success( "Danh sách khách sạn đã duyệt", khachSanMapper.toResponseList(list));
    }

    //PUBLIC: xem chi tiết KS đã duyệt
    public ApiResponse<KhachSanResponse> getApprovedById(Long id) {
        KhachSan ks = khachSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if (ks.getTrangThai() != TrangThaiKhachSan.DA_DUYET) {
            throw new RuntimeException("Khách sạn chưa được duyệt");
        }

        return ApiResponse.success("Chi tiết khách sạn", khachSanMapper.toResponse(ks));
    }

    //CHU_KHACH_SAN: xem chi tiết KS của mình
    public ApiResponse<DetailKhachSanResponse> getHotelDetail(Long id, Long ownerId) {
        KhachSan ks = khachSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if (!ks.getChuKhachSan().getId().equals(ownerId)) {
            throw new AccessDeniedException("Bạn không có quyền xem chi tiết khách sạn này");
        }

        return ApiResponse.success("Chi tiết khách sạn", detailKhachSanMapper.toDetailResponse(ks));
    }

    //CHU_KHACH_SAN: Tạo khách sạn mới
    public ApiResponse<KhachSanResponse> createHotel(KhachSanRequest request, Long ownerId) {
        TaiKhoan owner = taiKhoanRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        KhachSan ks = new KhachSan();
        ks.setTenKhachSan(request.getTenKhachSan());
        ks.setDiaChi(request.getDiaChi());
        ks.setChuKhachSan(owner);
        ks.setTrangThai(TrangThaiKhachSan.CHO_DUYET);

        ks = khachSanRepository.save(ks);

        notificationService.push(ownerId,
                "Bạn đã tạo khách sạn " + ks.getTenKhachSan() + " (đang chờ duyệt)",
                ownerId);

        return ApiResponse.success("Tạo khách sạn thành công", khachSanMapper.toResponse(ks));
    }

    // CHU_KHACH_SAN: cập nhật KS
    public ApiResponse<KhachSanResponse> updateHotel(Long khachSanId, KhachSanRequest request, Long userId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật khách sạn này");
        }
        if (ks.getTrangThai() == TrangThaiKhachSan.NGUNG_HOAT_DONG) {
            throw new RuntimeException("Không thể cập nhật khách sạn đã ngừng hoạt động");
        }

        ks.setTenKhachSan(request.getTenKhachSan());
        ks.setDiaChi(request.getDiaChi());

        if(ks.getTrangThai() == TrangThaiKhachSan.DA_DUYET) {
            ks.setTrangThai(TrangThaiKhachSan.CAP_NHAT_THONG_TIN);
        } else if(ks.getTrangThai() == TrangThaiKhachSan.TU_CHOI || ks.getTrangThai() == TrangThaiKhachSan.CHO_DUYET) {
            ks.setTrangThai(TrangThaiKhachSan.CHO_DUYET);
        }
        ks = khachSanRepository.save(ks);
        notificationService.push(
                userId,
                "Bạn đã cập nhật khách sạn " + ks.getTenKhachSan(),
                userId
        );

        return ApiResponse.success("Cập nhật khách sạn thành công", khachSanMapper.toResponse(ks));
    }
    // CHU_KHACH_SAN: dừng hoạt động KS
    public ApiResponse<KhachSanResponse> stopHotelStatus(Long khachSanId, Long userId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if (!ks.getChuKhachSan().getId().equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền dừng hoạt động khách sạn này");
        }

        ks.setTrangThai(TrangThaiKhachSan.NGUNG_HOAT_DONG);
        ks = khachSanRepository.save(ks);

        notificationService.push(
                userId,
                "Bạn đã dừng hoạt động khách sạn " + ks.getTenKhachSan(),
                userId
        );

        return ApiResponse.success("Dừng hoạt động khách sạn", khachSanMapper.toResponse(ks));
    }

    //ADMIN: danh sách tất cả KS
    public ApiResponse<List<KhachSanResponse>> getAll() {
        return ApiResponse.success("Tất cả khách sạn", khachSanMapper.toResponseList(khachSanRepository.findAll()) );
    }

    //ADMIN: Xem chi tiết KS
    public ApiResponse<DetailKhachSanResponse> getDetail(Long id) {
        return ApiResponse.success(
                "Chi tiết khách sạn",
                detailKhachSanMapper.toDetailResponse(
                        khachSanRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"))
                )
        );
    }

    //ADMIN: danh sách chờ duyệt
    public ApiResponse<List<KhachSanResponse>> getPending() {
        List<KhachSan> list = khachSanRepository.findByTrangThai(TrangThaiKhachSan.CHO_DUYET);
        return ApiResponse.success( "Danh sách chờ duyệt", khachSanMapper.toResponseList(list));
    }

    //ADMIN: duyệt KS
    public ApiResponse<Void> approve(Long id, Long adminId) {
        KhachSan ks = khachSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));   

        ks.setTrangThai(TrangThaiKhachSan.DA_DUYET);
        khachSanRepository.save(ks);

        Long ownerId = ks.getChuKhachSan().getId();

        notificationService.push(
                ownerId,
                "ADMIN đã duyệt khách sạn \"" + ks.getTenKhachSan() + "\"",
                adminId
        );

        // 📌 Log cho admin
        notificationService.push(
                adminId,
                "(ADMIN) đã duyệt khách sạn \"" + ks.getTenKhachSan() + "\"",
                adminId

        );

        return ApiResponse.success("Duyệt khách sạn thành công", null);
    }

    //ADMIN: từ chối KS
    public ApiResponse<Void> reject(Long id, Long adminId) {
        KhachSan ks = khachSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        Long ownerId = ks.getChuKhachSan().getId();

        if(ks.getTrangThai() == TrangThaiKhachSan.CHO_DUYET) {
            ks.setTrangThai(TrangThaiKhachSan.TU_CHOI);

            notificationService.push(
                    ownerId,
                    "ADMIN đã từ chối khách sạn \"" + ks.getTenKhachSan() + "\"",
                    adminId
            );
        }
        else if(ks.getTrangThai() == TrangThaiKhachSan.CAP_NHAT_THONG_TIN) {

            notificationService.push(
                    ownerId,
                    "ADMIN đã từ chối yêu cầu cập nhật của khách sạn \"" + ks.getTenKhachSan() + "\"",
                    adminId
            );

            ks.setTrangThai(TrangThaiKhachSan.DA_DUYET);
        }
        else {
            throw new RuntimeException("Chỉ có thể từ chối khách sạn đang chờ duyệt hoặc cập nhật thông tin");
        }

        khachSanRepository.save(ks);

        // 📌 Log cho admin
        notificationService.push(
                adminId,
                "Bạn (ADMIN) đã từ chối khách sạn \"" + ks.getTenKhachSan() + "\"",
                adminId
        );

        return ApiResponse.success("Từ chối khách sạn thành công", null);
    }



}
