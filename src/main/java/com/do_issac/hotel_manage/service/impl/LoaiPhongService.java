package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.LoaiPhongRequest;
import com.do_issac.hotel_manage.entity.KhachSan;
import com.do_issac.hotel_manage.entity.LoaiPhong;
import com.do_issac.hotel_manage.entity.TrangThaiKhachSan;
import com.do_issac.hotel_manage.mapper.LoaiPhongMapper;
import com.do_issac.hotel_manage.repository.KhachSanRepository;
import com.do_issac.hotel_manage.repository.LoaiPhongRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoaiPhongService {

    private final KhachSanRepository khachSanRepository;
    private final LoaiPhongRepository loaiPhongRepository;
    private final LoaiPhongMapper loaiPhongMapper;


    public ApiResponse<?> createLoaiPhongForKhachSan(Long khachSanId, LoaiPhongRequest request, Long chuKhachSanId) {

        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        Set<TrangThaiKhachSan> allowed = Set.of(
                TrangThaiKhachSan.DA_DUYET,
                TrangThaiKhachSan.CAP_NHAT_THONG_TIN
        );

        if(!allowed.contains(ks.getTrangThai()) ) {
            throw new RuntimeException("Khách sạn đang bị khóa, không thể thêm loại phòng");
        }

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


    public ApiResponse<?> getLoaiPhongByKhachSan(Long khachSanId, Long chuKhachSanId) {

        KhachSan ks = khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Khách sạn không tồn tại"));

        if (!ks.getChuKhachSan().getId().equals(chuKhachSanId)) {
            throw new RuntimeException("Bạn không có quyền xem loại phòng của khách sạn này");
        }

        return ApiResponse.success("Danh sách loại phòng" ,loaiPhongMapper.toResponseList(ks.getLoaiPhongs()));
    }


    public ApiResponse<?> getAllLoaiPhong(Long chuKhachSanId) {
        return ApiResponse.success("Danh sách loại phòng",
                loaiPhongMapper.toResponseList(
                        loaiPhongRepository.findAllByKhachSans_ChuKhachSan_Id(chuKhachSanId)
                ));
    }

    public ApiResponse<?> getLoaiPhongById(Long loaiPhongId, Long chuKhachSanId) {
        LoaiPhong lp = loaiPhongRepository.findById(loaiPhongId)
                .orElseThrow(() -> new RuntimeException("Loại phòng không tồn tại"));

        boolean isOwner = lp.getKhachSans().stream()
                .anyMatch(ks -> ks.getChuKhachSan().getId().equals(chuKhachSanId));

        if (!isOwner) {
            throw new RuntimeException("Bạn không có quyền xem loại phòng này");
        }

        return ApiResponse.success("Chi tiết loại phòng", loaiPhongMapper.toResponse(lp));
    }
}
