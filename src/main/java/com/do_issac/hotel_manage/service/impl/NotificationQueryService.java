package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.ThongBaoResponse;
import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.ThongBao;
import com.do_issac.hotel_manage.entity.VaiTro;
import com.do_issac.hotel_manage.mapper.ThongBaoMapper;
import com.do_issac.hotel_manage.repository.ThongBaoRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {
    private final ThongBaoRepository repo;
    private final ThongBaoMapper mapper;

    // ADMIN: xem tất cả thông báo của toàn hệ thống
    public ApiResponse<List<ThongBaoResponse>> getAllForAdmin() {
        List<ThongBao> list = repo.findAllByOrderByNgayTaoDesc();
        return ApiResponse.success("Tất cả thông báo", mapper.toResponseList(list));
    }

    // USER: xem thông báo của chính họ
    public ApiResponse<List<ThongBaoResponse>> getByUser(Long userId) {
        List<ThongBao> list = repo
                .findByTaiKhoanIdOrderByNgayTaoDesc(userId);
        return ApiResponse.success("Thông báo của bạn", mapper.toResponseList(list));
    }

    // Đánh dấu đã xem
    public ApiResponse<Void> markAsRead(Long notiId, Long userId) {
        ThongBao tb = repo.findById(notiId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        if (!tb.getTaiKhoan().getId().equals(userId)) {
            throw new AccessDeniedException("Không được phép đánh dấu thông báo của người khác");
        }

        tb.setDaXem(true);
        repo.save(tb);

        return ApiResponse.success("Đã đánh dấu đã xem", null);
    }
}
