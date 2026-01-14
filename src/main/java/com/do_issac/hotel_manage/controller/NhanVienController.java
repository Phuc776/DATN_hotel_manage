package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.request.ThanhToanThuCongRequest;
import com.do_issac.hotel_manage.entity.ThanhToan;
import com.do_issac.hotel_manage.repository.KhachHangRepository;
import com.do_issac.hotel_manage.service.impl.DatPhongService;
import com.do_issac.hotel_manage.service.impl.HoaDonService;
import com.do_issac.hotel_manage.service.impl.PhongService;
import com.do_issac.hotel_manage.service.impl.ThanhToanService;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nhan-vien")
@RequiredArgsConstructor
public class NhanVienController {
    private final AuthUtil authUtil;
    private final KhachHangRepository khachHangRepo;
    private final PhongService phongService;
    private final DatPhongService datPhongService;
    private final HoaDonService hoaDonService;
    private final ThanhToanService thanhToanService;

    @GetMapping("/phong")
    public ApiResponse<?> getAllPhongByKhachSan() {
        Long userId = authUtil.getCurrentUserId();

        return phongService.getAllRoomsForNhanVien(userId);
    }

    @GetMapping("/phong/{id}")
    public ApiResponse<?> getPhongDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return phongService.getRoomById(userId, id);
    }

    @GetMapping("/booking")
    public ApiResponse<?> getAllBookings() {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.getAllBookingsForNhanVien(userId);
    }
    @GetMapping("/booking/{id}")
    public ApiResponse<?> getBookingDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.getDetailBookingForNhanVien(userId, id);
    }
    @PostMapping("/booking/{id}/check-in")
    public ApiResponse<?> checkIn(@PathVariable Long id) {
        datPhongService.checkIn(id);
        return ApiResponse.success("Check-in thành công", null);
    }
    @PostMapping("/booking/{id}/check-out")
    public ApiResponse<?> checkOut(@PathVariable Long id) {
        datPhongService.checkOut(id);
        return ApiResponse.success("Check-out thành công", null);
    }
    @PostMapping("/booking/{id}/cancel")
    public ApiResponse<?> huyBooking(@PathVariable Long id) {
        datPhongService.huyBooking(id);
        return ApiResponse.success("Hủy booking thành công", null);
    }

    @GetMapping("/hoa-don")
    public ApiResponse<?> getAllHoaDon() {
        Long userId = authUtil.getCurrentUserId();
        return hoaDonService.xemDanhSachHoaDonCuaKhachSanNhanVien(userId);
    }

    @GetMapping("/hoa-don/{id}")
    public ApiResponse<?> getHoaDonByBooking(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return hoaDonService.xemHoaDonNhanVien(id,
                userId);
    }

    @GetMapping("/hoa-don/{id}/pdf")
    public ResponseEntity<byte[]> downloadHoaDonPdf(
            @PathVariable Long id) {

        Long userId = authUtil.getCurrentUserId();

        byte[] pdf = hoaDonService
                .taiHoaDonPdfChoNhanVien(id, userId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=hoa-don-" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/hoa-don/{hoaDonId}/thanh-toan")
    public ApiResponse<?> getDanhSachThanhToan(
            @PathVariable Long hoaDonId
    ) {
        Long nhanVienId = authUtil.getCurrentUserId();

        return ApiResponse.success(
                "Danh sách thanh toán",
                thanhToanService.xemDanhSachThanhToanTheoHoaDon(
                        hoaDonId, nhanVienId
                )
        );
    }

    @PostMapping("/hoa-don/{hoaDonId}/thanh-toan")
    public ApiResponse<?> thanhToanThuCong(
            @PathVariable Long hoaDonId,
            @RequestBody ThanhToanThuCongRequest req
    ) {
        Long nhanVienId = authUtil.getCurrentUserId();

        ThanhToan tt = thanhToanService
                .thanhToanThuCongTheoHoaDon(hoaDonId, nhanVienId, req.getSoTien());

        return ApiResponse.success("Thu tiền thành công", tt.getId());
    }

}
