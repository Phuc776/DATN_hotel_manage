package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.PhongResponse;
import com.do_issac.hotel_manage.entity.Phong;
import com.do_issac.hotel_manage.mapper.PhongMapper;
import com.do_issac.hotel_manage.repository.NhanVienRepository;
import com.do_issac.hotel_manage.repository.PhongRepository;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhongService {
    private final PhongRepository phongRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NotificationService notificationService;
    private final NhanVienRepository nhanVienRepository;


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

    public ApiResponse<List<PhongResponse>>  getAllRooms(Long userId, Long hotelId) {

        validateHotelPermission(userId, hotelId);

        List<Phong> phongList = phongRepository.findByKhachSan_Id(hotelId);
        return ApiResponse.success("Lay danh sách phòng thành công",
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

}
