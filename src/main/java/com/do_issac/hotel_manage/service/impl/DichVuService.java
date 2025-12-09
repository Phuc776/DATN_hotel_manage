package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.DichVuRequest;
import com.do_issac.hotel_manage.dto.response.DichVuResponse;
import com.do_issac.hotel_manage.entity.DichVu;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.TrangThaiKhachSan;
import com.do_issac.hotel_manage.mapper.DichVuMapper;
import com.do_issac.hotel_manage.repository.DichVuRepository;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DichVuService {

    private final KhachSanRepository khachSanRepository;
    private final DichVuRepository dichVuRepository;
    private final DichVuMapper dichVuMapper;

    public ApiResponse<DichVuResponse> createDichVuForKhachSan(Long khachSanId, DichVuRequest request, Long chuKhachSanId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        if(!ks.getTrangThai().equals(TrangThaiKhachSan.DA_DUYET)) {
            throw new RuntimeException("Khách sạn đang bị khóa, không thể thêm dịch vụ");
        }

        // Kiểm tra quyền sở hữu
        if (!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền thêm loại phòng cho khách sạn này");
        }
        DichVu dv = dichVuMapper.toEntity(request);

        // Gán vào khách sạn (mối quan hệ N-N)
        ks.getDichVus().add(dv);
        dichVuRepository.save(dv);
        khachSanRepository.save(ks);

        return ApiResponse.success("Thêm dịch vụ thành công", dichVuMapper.toResponse(dv));
    }
    public ApiResponse<DichVuResponse> updateDichVuForKhachSan(Long dichVuId, DichVuRequest request, Long chuKhachSanId) {
        DichVu dv = dichVuRepository.findById(dichVuId)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));

        boolean isOwner = dv.getKhachSans().stream()
                .anyMatch(ks -> ks.getChuKhachSan().getId().equals(chuKhachSanId));

        if (!isOwner) {
            throw new RuntimeException("Bạn không có quyền sửa dịch vụ này");
        }

        dv.setTenDichVu(request.getTenDichVu());
        dv.setMoTa(request.getMoTa());
        dv.setDonGia(request.getDonGia());

        dichVuRepository.save(dv);

        return ApiResponse.success("Cập nhật dịch vụ thành công", dichVuMapper.toResponse(dv));
    }


    public ApiResponse<List<DichVuResponse>> getDichVuByKhachSan(Long khachSanId, Long chuKhachSanId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        if (!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền xem dịch vụ của khách sạn này");
        }

        return ApiResponse.success("Danh sách dịch vụ" ,dichVuMapper.toResponseList(ks.getDichVus()));
    }


    public ApiResponse<List<DichVuResponse>> getAllDichVu(Long chuKhachSanId) {
        return ApiResponse.success("Danh sách dịch vụ",
                dichVuMapper.toResponseList(
                        dichVuRepository.findAllByKhachSans_ChuKhachSan_Id(chuKhachSanId)
                ));
    }

    public ApiResponse<DichVuResponse> getDichVuDetail(Long dichVuId, Long chuKhachSanId) {
        DichVu dv = dichVuRepository.findById(dichVuId)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));

        boolean isOwner = dv.getKhachSans().stream()
                .anyMatch(ks -> ks.getChuKhachSan().getId().equals(chuKhachSanId));

        if (!isOwner) {
            throw new RuntimeException("Bạn không có quyền xem dịch vụ này");
        }

        return ApiResponse.success("Chi tiết dịch vụ", dichVuMapper.toResponse(dv));
    }
}
