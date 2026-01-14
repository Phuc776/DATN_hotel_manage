package com.do_issac.hotel_manage.controller;

import com.do_issac.hotel_manage.dto.request.ChiTietDatPhongRequest;
import com.do_issac.hotel_manage.dto.request.DanhGiaRequest;
import com.do_issac.hotel_manage.dto.request.KhachHangUpdateProfileRequest;
import com.do_issac.hotel_manage.dto.request.SuDungDichVuRequest;
import com.do_issac.hotel_manage.entity.ThanhToan;
import com.do_issac.hotel_manage.repository.DanhGiaRepository;
import com.do_issac.hotel_manage.repository.KhachHangRepository;
import com.do_issac.hotel_manage.service.impl.*;
import com.do_issac.hotel_manage.util.ApiResponse;
import com.do_issac.hotel_manage.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {
    private final DanhGiaRepository danhGiaRepo;
    private final KhachHangRepository khachHangRepo;

    private final KhachHangService khachHangService;
    private final DatPhongService datPhongService;
    private final PhienLuuTruService phienLuuTruService;
    private final PhongService phongService;
    private final DichVuService dichVuService;
    private final HoaDonService hoaDonService;
    private final ThanhToanService thanhToanService;
    private final AuthUtil authUtil;
    private final DanhGiaService danhGiaService;

    @GetMapping("/profile")
    public ApiResponse<?> getMyProfile() {
        return ApiResponse.success("Lấy thông tin khách hàng thành công", khachHangService.getMyProfile());
    }
    @GetMapping("/profile/reveal-cccd")
    public ApiResponse<?> revealCccd() {
        return ApiResponse.success("Lấy CCCD khách hàng thành công", khachHangService.revealCCCD());
    }

    @PutMapping("/profile")
    public ApiResponse<?> updateMyProfile(
            @RequestBody KhachHangUpdateProfileRequest request
    ) {
        return ApiResponse.success("Cập nhật thông tin khách hàng thành công", khachHangService.updateMyProfile(request));
    }

    @GetMapping("/booking")
    public ApiResponse<?> myBookings() {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.checkBookingHistory(userId);
    }

    @GetMapping("/booking/{id}")
    public ApiResponse<?> getBookingDetail(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.getBookingDetail(userId, id);
    }

    @PostMapping("/booking")
    public ApiResponse<?> datPhong(
            @RequestBody ChiTietDatPhongRequest request
    ) {
        Long userId = authUtil.getCurrentUserId();
        return datPhongService.datPhong(userId, request);
    }

    @PostMapping("/booking/{id}/confirm")
    public ApiResponse<?> confirmReturnBooking(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        datPhongService.yeucauTraPhong(userId, id);
        return ApiResponse.success("Yêu cầu trả phòng thành công", null);
    }

    @PostMapping("/booking/{id}/cancel")
    public ApiResponse<?> cancelBooking(@PathVariable Long id) {
        Long userId = authUtil.getCurrentUserId();
        datPhongService.yeucauHuyBooking(userId, id);
        return ApiResponse.success("Yêu cầu hủy booking thành công", null);
    }

    @PostMapping("/phong/{idPhong}/open-door")
    public ApiResponse<?> openDoor(@PathVariable Long idPhong, @RequestParam String token) {
        phongService.moCuaBangQr(idPhong, token);
        return ApiResponse.success("Mở cửa thành công", null);
    }

    @GetMapping("/phien/current")
    public ApiResponse<?> getCurrentPhien() {
        Long userId = authUtil.getCurrentUserId();
        return phienLuuTruService.getPhienDangMo(userId);
    }

    @GetMapping("/phien/{phienId}")
    public ApiResponse<?> getPhienDetail(@PathVariable Long phienId) {
        Long userId = authUtil.getCurrentUserId();
        return phienLuuTruService.getPhienDetail(userId, phienId);
    }

    @GetMapping("/phien")
    public ApiResponse<?> getPhienHistory() {
        Long userId = authUtil.getCurrentUserId();
        return phienLuuTruService.getPhienHistory(userId);
    }

    @GetMapping("/dich-vu")
    public ApiResponse<?> getDichVuDangO() {
        Long userId = authUtil.getCurrentUserId();
        return dichVuService.getDichVuChoKhachDangO(userId);
    }

    @PostMapping("/dich-vu/use")
    public ApiResponse<?> suDungDichVu(@RequestBody SuDungDichVuRequest req) {
        Long userId = authUtil.getCurrentUserId();
        return dichVuService.suDungDichVu(userId, req);
    }

    @GetMapping("/hoa-don/history")
    public ApiResponse<?> history() {
        return hoaDonService.lichSuHoaDonCuaKhach(authUtil.getCurrentUserId());
    }

    @GetMapping("/hoa-don/{id}")
    public ApiResponse<?> detail(@PathVariable Long id) {
        return hoaDonService.xemChiTietHoaDonCuaKhach(
                id, authUtil.getCurrentUserId());
    }

    @GetMapping("/hoa-don/{id}/pdf")
    public ResponseEntity<byte[]> downloadHoaDonPdf(
            @PathVariable Long id) {

        Long userId = authUtil.getCurrentUserId();

        byte[] pdf = hoaDonService
                .taiHoaDonPdfChoKhach(id, userId);
        var khachHang = khachHangRepo.findByTaiKhoan_Id(userId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=hoa-don-" + id + " - " +
                                khachHang.getTaiKhoan().getHoTen() + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/thanh-toan/vnpay/{phienId}")
    public ApiResponse<?> taoThanhToanVnpay(@PathVariable Long phienId) {

        ThanhToan tt = thanhToanService.taoThanhToanVnpay(phienId);

        String redirectUrl = thanhToanService.taoLinkVnpay(tt);

        return ApiResponse.success(
                "Redirect to VNPAY",
                Map.of("url", redirectUrl)
        );
    }

    @GetMapping("/thanh-toan/vnpay/callback")
    public ResponseEntity<?> vnpayCallback(
            @RequestParam Map<String, String> params) {

        thanhToanService.xuLyCallbackVnpay(params);

        // redirect về FE
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:5173/payment-result"))
                .build();
    }

    @GetMapping("/danh-gia")
    public ApiResponse<?> xemDanhGiaCuaToi() {

        Long userId = authUtil.getCurrentUserId();

        return ApiResponse.success(
                "Danh sách đánh giá của tôi",
                danhGiaService.xemDanhGiaCuaToi(userId)
        );
    }

    @GetMapping("/khach-san/{khachSanId}/da-danh-gia")
    public ApiResponse<?> daDanhGia(@PathVariable Long khachSanId) {

        Long userId = authUtil.getCurrentUserId();

        boolean exists = danhGiaRepo
                .existsByKhachHang_TaiKhoan_IdAndKhachSan_Id(userId, khachSanId);

        return ApiResponse.success("Kiểm tra đã đánh giá", exists);
    }

    @PostMapping("/khach-san/{khachSanId}/danh-gia")
    public ApiResponse<?> taoDanhGia(
            @PathVariable Long khachSanId,
            @RequestBody DanhGiaRequest danhGiaRequest
            ) {
        Long userId = authUtil.getCurrentUserId();
        return danhGiaService.taoDanhGia(khachSanId, userId, danhGiaRequest);
    }
}
