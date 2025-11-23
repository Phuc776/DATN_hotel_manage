package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.DichVuRequest;
import com.do_issac.hotel_manage.entity.DichVu;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.mapper.DichVuMapper;
import com.do_issac.hotel_manage.repository.DichVuRepository;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.service.DichVuService;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DichVuServiceImpl implements DichVuService {

    private final KhachSanRepository khachSanRepository;
    private final DichVuRepository dichVuRepository;
    private final DichVuMapper dichVuMapper;
    @Override
    public ApiResponse<?> createDichVuForKhachSan(Long khachSanId, DichVuRequest request, Long chuKhachSanId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

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

    @Override
    public ApiResponse<?> getDichVuByKhachSan(Long khachSanId, Long chuKhachSanId) {
        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        if (!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền xem dịch vụ của khách sạn này");
        }

        return ApiResponse.success("Danh sách dịch vụ" ,dichVuMapper.toResponseList(ks.getDichVus()));
    }
}
