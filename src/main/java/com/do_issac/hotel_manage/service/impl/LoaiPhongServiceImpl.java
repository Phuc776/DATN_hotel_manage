package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.LoaiPhongRequest;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.LoaiPhong;
import com.do_issac.hotel_manage.mapper.LoaiPhongMapper;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.repository.LoaiPhongRepository;
import com.do_issac.hotel_manage.service.LoaiPhongService;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoaiPhongServiceImpl implements LoaiPhongService {

    private final KhachSanRepository khachSanRepository;
    private final LoaiPhongRepository loaiPhongRepository;
    private final LoaiPhongMapper loaiPhongMapper;

    @Override
    public ApiResponse<?> createLoaiPhongForKhachSan(Long khachSanId, LoaiPhongRequest request, Long chuKhachSanId) {

        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        // Kiểm tra quyền sở hữu
        if (!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền thêm loại phòng cho khách sạn này");
        }

        LoaiPhong lp = loaiPhongMapper.toEntity(request);

        // Gán vào khách sạn (mối quan hệ N-N)
        ks.getLoaiPhongs().add(lp);

        loaiPhongRepository.save(lp);
        khachSanRepository.save(ks);

        return ApiResponse.success("Thêm loại phòng thành công", loaiPhongMapper.toResponse(lp));
    }

    @Override
    public ApiResponse<?> getLoaiPhongByKhachSan(Long khachSanId, Long chuKhachSanId) {

        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        if (!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền xem loại phòng của khách sạn này");
        }

        return ApiResponse.success("Danh sách loại phòng" ,loaiPhongMapper.toResponseList(ks.getLoaiPhongs()));
    }
}
