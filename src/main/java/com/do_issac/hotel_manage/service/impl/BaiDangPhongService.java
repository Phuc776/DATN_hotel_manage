package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.BaiDangPhongRequest;
import com.do_issac.hotel_manage.dto.response.BaiDangPhongResponse;
import com.do_issac.hotel_manage.entity.*;
import com.do_issac.hotel_manage.mapper.BaiDangPhongMapper;
import com.do_issac.hotel_manage.repository.*;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaiDangPhongService {
    private final BaiDangPhongRepository baiDangPhongRepository;
    private final LoaiPhongRepository loaiPhongRepository;
    private final KhachSanRepository khachSanRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PhongRepository phongRepository;
    private final BaiDangPhongMapper baiDangPhongMapper;

    public ApiResponse<BaiDangPhongResponse> getById(Long ownerId, Long baiDangId) {
        BaiDangPhong bdp = baiDangPhongRepository.findById(baiDangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng phòng"));
        if (!bdp.getChuKhachSan().getId().equals(ownerId)) {
            throw new AccessDeniedException("Bạn không có quyền xem bài đăng này");
        }
        BaiDangPhongResponse response = baiDangPhongMapper.toResponse(bdp);
        return ApiResponse.success("Lấy thông tin bài đăng phòng thành công", response);
    }

    public ApiResponse<BaiDangPhongResponse> getById(Long baiDangId) {
        BaiDangPhong bdp = baiDangPhongRepository.findById(baiDangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng phòng"));

        BaiDangPhongResponse response = baiDangPhongMapper.toResponse(bdp);
        return ApiResponse.success("Lấy thông tin bài đăng phòng thành công", response);
    }



    public ApiResponse<List<BaiDangPhongResponse>> getByOwnerId(Long ownerId) {
        List<BaiDangPhong> posts = baiDangPhongRepository.findAll().stream()
                .filter(bdp -> bdp.getChuKhachSan().getId().equals(ownerId))
                .toList();
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Lấy danh sách bài đăng phòng của chủ khách sạn thành công", responseList);
    }


    public ApiResponse<List<BaiDangPhongResponse>> getPostsByKhachSanId(Long khachSanId) {
        List<BaiDangPhongResponse> responseList = getByKhachSanId(khachSanId);
        return ApiResponse.success("Lấy danh sách bài đăng phòng của khách sạn thành công", responseList);
    }

    public ApiResponse<List<BaiDangPhongResponse>> getPostsByKhachSanId(Long ownerId, Long khachSanId) {
        if (!khachSanRepository.findById(khachSanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"))
                .getChuKhachSan().getId().equals(ownerId)) {
            throw new AccessDeniedException("Bạn không có quyền xem bài đăng của khách sạn này");
        }

        List<BaiDangPhongResponse> responseList = getByKhachSanId(khachSanId);
        return ApiResponse.success("Lấy danh sách bài đăng phòng của khách sạn thành công", responseList);
    }

    public ApiResponse<List<BaiDangPhongResponse>> getByTrangThai(Long ownerId, TrangThaiBaiDang trangThai) {
        List<BaiDangPhong> posts = baiDangPhongRepository.findByTrangThaiBaiDang(trangThai).stream()
                .filter(bdp -> bdp.getChuKhachSan().getId().equals(ownerId))
                .toList();
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Danh sách bài đăng phòng", responseList);
    }

    public ApiResponse<List<BaiDangPhongResponse>> getByTrangThai(TrangThaiBaiDang trangThai) {

        List<BaiDangPhong> posts = baiDangPhongRepository.findByTrangThaiBaiDang(trangThai);
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Danh sách bài đăng phòng", responseList);
    }

    public ApiResponse<List<BaiDangPhongResponse>> getByOwnerIdAndTrangThai(Long ownerId, TrangThaiBaiDang trangThai) {
        List<BaiDangPhong> posts = baiDangPhongRepository.findByTrangThaiBaiDang(trangThai).stream()
                .filter(bdp -> bdp.getChuKhachSan().getId().equals(ownerId))
                .toList();
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Danh sách bài đăng phòng", responseList);
    }
    public ApiResponse<List<BaiDangPhongResponse>> getByKhachSanIdAndTrangThai(Long ownerId, Long khachSanId, TrangThaiBaiDang trangThai) {
        List<BaiDangPhong> posts = baiDangPhongRepository.findByTrangThaiBaiDang(trangThai).stream()
                .filter(bdp -> bdp.getKhachSan().getId().equals(khachSanId))
                .filter(bdp -> bdp.getChuKhachSan().getId().equals(ownerId))
                .toList();
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Danh sách bài đăng phòng", responseList);
    }

    public ApiResponse<List<BaiDangPhongResponse>> getAll() {
        List<BaiDangPhong> posts = baiDangPhongRepository.findAll();
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Lấy danh sách tất cả bài đăng phòng thành công", responseList);
    }
    public ApiResponse<List<BaiDangPhongResponse>> getAll(Long ownerId) {
        List<BaiDangPhong> posts = baiDangPhongRepository.findAll().stream()
                .filter(bdp -> bdp.getChuKhachSan().getId().equals(ownerId))
                .toList();
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Lấy danh sách tất cả bài đăng phòng thành công", responseList);
    }

    public ApiResponse<List<BaiDangPhongResponse>> getAllApproved() {
        List<BaiDangPhong> posts = baiDangPhongRepository.findByTrangThaiBaiDang(TrangThaiBaiDang.DA_DUYET);
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Lấy danh sách bài đăng phòng đã duyệt thành công", responseList);
    }

    public ApiResponse<BaiDangPhongResponse> getApprovedById(Long baiDangId) {
        BaiDangPhong bdp = baiDangPhongRepository.findById(baiDangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng phòng"));

        if (bdp.getTrangThaiBaiDang() != TrangThaiBaiDang.DA_DUYET) {
            throw new RuntimeException("Bài đăng phòng chưa được duyệt");
        }

        BaiDangPhongResponse response = baiDangPhongMapper.toResponse(bdp);
        return ApiResponse.success("Lấy thông tin bài đăng phòng đã duyệt thành công", response);
    }

    public ApiResponse<List<BaiDangPhongResponse>> getApprovedByKhachSanId(Long khachSanId) {
        List<BaiDangPhong> posts = baiDangPhongRepository.findByTrangThaiBaiDang(TrangThaiBaiDang.DA_DUYET).stream()
                .filter(bdp -> bdp.getKhachSan().getId().equals(khachSanId))
                .toList();
        List<BaiDangPhongResponse> responseList = baiDangPhongMapper.toResponseList(posts);
        return ApiResponse.success("Lấy danh sách bài đăng phòng đã duyệt của khách sạn thành công", responseList);
    }


    public ApiResponse<BaiDangPhongResponse> create(Long ownerId, BaiDangPhongRequest request) {
        TaiKhoan owner = taiKhoanRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        KhachSan ks = khachSanRepository.findById(request.getKhachSanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));

        if (!ks.getChuKhachSan().getId().equals(ownerId)) {
            throw new AccessDeniedException("Bạn không có quyền tạo bài đăng cho khách sạn này");
        }

        LoaiPhong lp = loaiPhongRepository.findById(request.getLoaiPhongId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng"));

        BaiDangPhong bdp = new BaiDangPhong();
        bdp.setTieuDe(request.getTieuDe());
        bdp.setMoTa(request.getMoTa());
        bdp.setNgayDang(LocalDateTime.now());
        bdp.setSoLuongPhong(request.getSoLuongPhong());
        bdp.setTrangThaiBaiDang(TrangThaiBaiDang.CHO_DUYET);

        bdp.setLoaiPhong(lp);
        bdp.setKhachSan(ks);
        bdp.setChuKhachSan(owner);

        baiDangPhongRepository.save(bdp);
        return ApiResponse.success("Tạo bài đăng phòng thành công", baiDangPhongMapper.toResponse(bdp));
    }

    public ApiResponse<Void> approve(Long baiDangId) {

        BaiDangPhong bdp = baiDangPhongRepository.findById(baiDangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng"));

        if (bdp.getTrangThaiBaiDang().equals(TrangThaiBaiDang.DA_DUYET)) {
            throw new RuntimeException("Bài đăng đã được duyệt trước đó");
        }

        bdp.setTrangThaiBaiDang(TrangThaiBaiDang.DA_DUYET);
        baiDangPhongRepository.save(bdp);


        generateRoomsFromPost(bdp);

        return ApiResponse.success("Duyệt bài đăng phòng thành công", null);
    }

    public ApiResponse<Void> reject(Long baiDangId) {
        BaiDangPhong bdp = baiDangPhongRepository.findById(baiDangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng"));

        if (bdp.getTrangThaiBaiDang().equals(TrangThaiBaiDang.DA_DUYET)) {
            throw new RuntimeException("Bài đăng đã được duyệt, không thể từ chối");
        }

        bdp.setTrangThaiBaiDang(TrangThaiBaiDang.TU_CHOI);
        baiDangPhongRepository.save(bdp);

        return ApiResponse.success("Từ chối bài đăng phòng thành công", null);
    }

    private void generateRoomsFromPost(BaiDangPhong bdp) {
        int soLuong = bdp.getSoLuongPhong();
        LoaiPhong lp = bdp.getLoaiPhong();
        KhachSan ks = bdp.getKhachSan();

        int startIndex = (int) phongRepository.countByKhachSan_Id(ks.getId()) + 1;

        List<Phong> newRooms = new ArrayList<>();

        for (int i = 0; i < soLuong; i++) {
            Phong p = new Phong();
            p.setSoPhong("P" + (startIndex + i));
            p.setTrangThaiPhong(TrangThaiPhong.TRONG);
            p.setKhachSan(ks);
            p.setLoaiPhong(lp);

            newRooms.add(p);
        }

        phongRepository.saveAll(newRooms);
    }

    private List<BaiDangPhongResponse> getByKhachSanId(Long khachSanId) {
        List<BaiDangPhong> posts = baiDangPhongRepository.findAll().stream()
                .filter(bdp -> bdp.getKhachSan().getId().equals(khachSanId))
                .toList();
        return baiDangPhongMapper.toResponseList(posts);

    }
}
