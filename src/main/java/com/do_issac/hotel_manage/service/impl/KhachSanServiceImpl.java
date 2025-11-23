package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.KhachSanRequest;
import com.do_issac.hotel_manage.dto.response.KhachSanResponse;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.TrangThaiKhachSan;
import com.do_issac.hotel_manage.mapper.KhachSanMapper;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import com.do_issac.hotel_manage.service.KhachSanService;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachSanServiceImpl implements KhachSanService {
    private final KhachSanRepository khachSanRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final KhachSanMapper khachSanMapper;

    //CHU_KHACH_SAN: xem danh sách KS của mình
    @Override
    public ApiResponse<List<KhachSan>> getAllHotelsByOwner(Long ownerId) {
        return ApiResponse.success(
                "Danh sách khách sạn",
                khachSanRepository.findByChuKhachSanId(ownerId)
        );
    }

    //CHU_KHACH_SAN: xem chi tiết KS của mình
    @Override
    public ApiResponse<KhachSan> getHotelDetail(Long id, Long ownerId) {
        KhachSan ks = khachSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if (!ks.getChuKhachSan().getId().equals(ownerId)) {
            return ApiResponse.failure("Bạn không có quyền xem khách sạn này");
        }

        return ApiResponse.success("Chi tiết khách sạn", ks);
    }

    //CHU_KHACH_SAN: Tạo khách sạn mới
    @Override
    public ApiResponse<KhachSanResponse> createHotel(KhachSanRequest request, Long ownerId) {
        TaiKhoan owner = taiKhoanRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        KhachSan ks = new KhachSan();
        ks.setTenKhachSan(request.getTenKhachSan());
        ks.setDiaChi(request.getDiaChi());
        ks.setChuKhachSan(owner);
        ks.setTrangThai(TrangThaiKhachSan.CHO_DUYET);

        ks = khachSanRepository.save(ks);

        return ApiResponse.success("Tạo khách sạn thành công", khachSanMapper.toResponse(ks));
    }

    // CHU_KHACH_SAN: cập nhật KS
    @Override
    public ApiResponse<KhachSanResponse> updateHotel(Long khachSanId, KhachSanRequest request, Long userId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if (!ks.getChuKhachSan().getId().equals(userId)) {
            return ApiResponse.failure("Bạn không có quyền cập nhật khách sạn này");
        }

        ks.setTenKhachSan(request.getTenKhachSan());
        ks.setDiaChi(request.getDiaChi());
        ks.setTrangThai(TrangThaiKhachSan.CHO_DUYET);

        ks = khachSanRepository.save(ks);

        return ApiResponse.success("Cập nhật khách sạn thành công", khachSanMapper.toResponse(ks));
    }

    //ADMIN: danh sách tất cả KS
    @Override
    public ApiResponse<List<KhachSanResponse>> adminGetAll() {
        return ApiResponse.success("Tất cả khách sạn", khachSanMapper.toResponseList(khachSanRepository.findAll()) );
    }

    //ADMIN: Xem chi tiết KS
    @Override
    public ApiResponse<KhachSan> adminGetDetail(Long id) {
        return ApiResponse.success(
                "Chi tiết khách sạn",
                khachSanRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"))
        );
    }

    //ADMIN: danh sách chờ duyệt
    @Override
    public ApiResponse<List<KhachSanResponse>> getPending() {
        List<KhachSan> list = khachSanRepository.findByTrangThai(TrangThaiKhachSan.CHO_DUYET);
        return ApiResponse.success( "Danh sách chờ duyệt", khachSanMapper.toResponseList(list));
    }

    //ADMIN: duyệt KS
    @Override
    public ApiResponse<Void> approve(Long id) {
        KhachSan ks = khachSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        ks.setTrangThai(TrangThaiKhachSan.DA_DUYET);
        khachSanRepository.save(ks);

        return ApiResponse.success("Duyệt khách sạn thành công", null);
    }

    //ADMIN: từ chối KS
    @Override
    public ApiResponse<Void> reject(Long id) {
        KhachSan ks = khachSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        ks.setTrangThai(TrangThaiKhachSan.TU_CHOI);
        khachSanRepository.save(ks);
        return ApiResponse.success("Từ chối khách sạn thành công", null);
    }

}
