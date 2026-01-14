package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.request.ChiTietDatPhongRequest;
import com.do_issac.hotel_manage.dto.request.KiemTraPhongTrongRequest;
import com.do_issac.hotel_manage.dto.response.BaiDangPhongKhaDungResponse;
import com.do_issac.hotel_manage.dto.response.ChiTietDatPhongResponse;

import com.do_issac.hotel_manage.entity.*;
import com.do_issac.hotel_manage.mapper.ChiTietDatPhongMapper;
import com.do_issac.hotel_manage.repository.*;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatPhongService {
    private final BaiDangPhongRepository baiDangPhongRepository;
    private final PhongRepository phongRepository;
    private final ChiTietDatPhongRepository datPhongRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final QrKhoaPhongService qrService;
    private final RenderDocumentService documentService;
    private final EmailService emailService;
    private final PhienLuuTruService phienLuuTruService;
    private final DanhGiaService danhGiaService;

    private final ChiTietDatPhongMapper bookingMapper;

    List<TrangThaiDatPhong> activeStatuses = List.of(
            TrangThaiDatPhong.CHO_XAC_NHAN,
            TrangThaiDatPhong.DA_XAC_NHAN,
            TrangThaiDatPhong.DANG_O,
            TrangThaiDatPhong.CHO_TRA
    );


    public List<Phong> findAvailableRooms(
            Long loaiPhongId,
            LocalDateTime ngayNhan,
            LocalDateTime ngayTra
    ) {
        List<Phong> allRooms = phongRepository.findByLoaiPhong_Id(loaiPhongId);

        return allRooms.stream()
                .filter(p -> p.getTrangThaiPhong() == TrangThaiPhong.TRONG)
                .filter(p ->
                        datPhongRepository
                                .findConflict(p.getId(), ngayNhan, ngayTra, activeStatuses)
                                .isEmpty()
                )
                .toList();
    }

    public List<BaiDangPhongKhaDungResponse> checkAvailability(
            KiemTraPhongTrongRequest req
    ) {

        List<BaiDangPhong> posts =
                baiDangPhongRepository.findByTrangThaiBaiDang(TrangThaiBaiDang.DA_DUYET);

        List<BaiDangPhongKhaDungResponse> result = new ArrayList<>();

        for (BaiDangPhong post : posts) {


            // 1. Lọc theo sức chứa
            if (post.getLoaiPhong().getSoNguoiLon() < req.getSoNguoiLon()
                    || post.getLoaiPhong().getSoTreEm() < req.getSoTreEm()) {
                continue;
            }

            // 2. Đếm phòng đã đặt
            long soPhongDaDat = datPhongRepository.countBookedRooms(
                    post.getLoaiPhong().getId(),
                    req.getNgayNhan(),
                    req.getNgayTra(),
                    activeStatuses
            );

            int soPhongCon = post.getSoLuongPhong() - (int) soPhongDaDat;

            // 3. Lọc theo số phòng cần đặt
            if (soPhongCon < req.getSoLuongPhong()) {
                continue;
            }

            // 6. Map response
            BaiDangPhongKhaDungResponse res =
                    mapToResponse(post, soPhongDaDat);

            result.add(res);
        }

        return result;
    }
    public BaiDangPhongKhaDungResponse getDetailAvailability(
            Long baiDangPhongId,
            KiemTraPhongTrongRequest req
    ) {
        BaiDangPhong post = baiDangPhongRepository.findById(baiDangPhongId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng"));

        if (post.getTrangThaiBaiDang() != TrangThaiBaiDang.DA_DUYET) {
            throw new RuntimeException("Bài đăng chưa được duyệt");
        }

        // check sức chứa
        if (post.getLoaiPhong().getSoNguoiLon() < req.getSoNguoiLon()
                || post.getLoaiPhong().getSoTreEm() < req.getSoTreEm()) {
            throw new RuntimeException("Sức chứa không phù hợp");
        }

        long soPhongDaDat = datPhongRepository.countBookedRooms(
                post.getLoaiPhong().getId(),
                req.getNgayNhan(),
                req.getNgayTra(),
                activeStatuses
        );

        int soPhongCon = post.getSoLuongPhong() - (int) soPhongDaDat;

        if (soPhongCon < req.getSoLuongPhong()) {
            throw new RuntimeException("Không đủ phòng trống");
        }

        return mapToResponse(post, soPhongDaDat);
    }
    private BaiDangPhongKhaDungResponse mapToResponse(
            BaiDangPhong post,
            long soPhongDaDat
    ) {
        int soPhongCon = Math.max(
                post.getSoLuongPhong() - (int) soPhongDaDat, 0
        );

        BaiDangPhongKhaDungResponse res = new BaiDangPhongKhaDungResponse();

        res.setBaiDangPhongId(post.getId());
        res.setTieuDe(post.getTieuDe());
        res.setMoTa(post.getMoTa());

        res.setTenLoaiPhong(post.getLoaiPhong().getTenLoaiPhong());
        res.setGiaLoaiPhong(post.getLoaiPhong().getGia());
        res.setSoNguoiLon(post.getLoaiPhong().getSoNguoiLon());
        res.setSoTreEm(post.getLoaiPhong().getSoTreEm());

        res.setKhachSanId(post.getKhachSan().getId());
        res.setTenKhachSan(post.getKhachSan().getTenKhachSan());
        res.setDiaChiKhachSan(post.getKhachSan().getDiaChi());

        // đánh giá
        res.setDiemDanhGiaTrungBinh(
                danhGiaService.tinhDiemTrungBinh(post.getKhachSan().getId())
        );
        res.setSoLuongDanhGia(
                danhGiaService.demSoDanhGia(post.getKhachSan().getId())
        );

        // phòng
        res.setTongSoPhong(post.getSoLuongPhong());
        res.setSoPhongCon(soPhongCon);
        res.setConPhong(soPhongCon > 0);

        // hình ảnh
        res.setHinhAnhBaiDang(
                post.getHinhAnh()
                        .stream()
                        .map(HinhAnh::getImageUrl)
                        .toList()
        );

        return res;
    }



    @Transactional
    public ApiResponse<List<ChiTietDatPhongResponse>> datPhong(
            Long userId,
            ChiTietDatPhongRequest req
    ) {
        BaiDangPhong baiDang = baiDangPhongRepository.findById(req.getBaiDangPhongId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng"));

        if (baiDang.getTrangThaiBaiDang() != TrangThaiBaiDang.DA_DUYET) {
            throw new RuntimeException("Bài đăng phòng chưa được duyệt");
        }

        KhachHang khachHang = khachHangRepository
                .findByTaiKhoan_Id(userId);
        if (khachHang.getCCCD() == null || khachHang.getCCCD().isEmpty()) {
            throw new RuntimeException("Vui lòng cập nhật số CCCD/CMND trước khi đặt phòng");
        }

        List<Phong> availableRooms = findAvailableRooms(
                baiDang.getLoaiPhong().getId(),
                req.getNgayNhan(),
                req.getNgayTra()
        );

        if (availableRooms.size() < req.getSoLuongPhongDat()) {
            throw new RuntimeException("Không đủ phòng trống");
        }
        if (req.getSoLuongPhongDat() <= 0) {
            throw new RuntimeException("Số lượng phòng đặt phải lớn hơn 0");
        }

        if (req.getSoNguoiLon() > baiDang.getLoaiPhong().getSoNguoiLon()
                || req.getSoTreEm() > baiDang.getLoaiPhong().getSoTreEm()) {
            throw new RuntimeException("Số người vượt quá sức chứa phòng");
        }

        if (!req.getNgayNhan().isBefore(req.getNgayTra())) {
            throw new RuntimeException("Ngày nhận phải trước ngày trả");
        }


        PhienLuuTru phien = phienLuuTruService.taoPhienChoKhach(khachHang);
        List<ChiTietDatPhong> result = new ArrayList<>();

        for (int i = 0; i < req.getSoLuongPhongDat(); i++) {
            Phong phong = availableRooms.get(i);

            ChiTietDatPhong booking = new ChiTietDatPhong();
            booking.setPhong(phong);
            booking.setKhachHang(khachHang);
            booking.setNgayDat(LocalDateTime.now());
            booking.setNgayNhan(req.getNgayNhan());
            booking.setNgayTra(req.getNgayTra());
            booking.setSoNguoiLon(req.getSoNguoiLon());
            booking.setSoTreEm(req.getSoTreEm());
            booking.setGhiChu(req.getGhiChu());
            booking.setTrangThai(TrangThaiDatPhong.CHO_XAC_NHAN);

            booking.setPhienLuuTru(phien);
            phien.getDatPhongs().add(booking);

            datPhongRepository.save(booking);
            result.add(booking);
        }

        return ApiResponse.success(
                "Đặt phòng thành công",
                bookingMapper.toResponseList(result)
        );
    }

    @Transactional
    public void yeucauHuyBooking(Long userId, Long bookingId) {
        ChiTietDatPhong b = datPhongRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));
        if (!b.getKhachHang().getTaiKhoan().getId().equals(userId)) {
            throw new RuntimeException("Không có quyền thao tác booking này");
        }

        if (b.getTrangThai() != TrangThaiDatPhong.CHO_XAC_NHAN
                && b.getTrangThai() != TrangThaiDatPhong.DA_XAC_NHAN) {
            throw new RuntimeException("Không thể hủy booking ở trạng thái hiện tại");
        }

        b.setTrangThai(TrangThaiDatPhong.CHO_HUY);
        datPhongRepository.save(b);
    }
    @Transactional
    public void yeucauTraPhong(Long userId, Long bookingId) {
        ChiTietDatPhong b = datPhongRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));
        if (!b.getKhachHang().getTaiKhoan().getId().equals(userId)) {
            throw new RuntimeException("Không có quyền thao tác booking này");
        }
        if (b.getTrangThai() != TrangThaiDatPhong.DANG_O) {
            throw new RuntimeException("Không thể trả phòng ở trạng thái hiện tại");
        }
        b.setTrangThai(TrangThaiDatPhong.CHO_TRA);
        datPhongRepository.save(b);
    }

    public ApiResponse<List<ChiTietDatPhongResponse>> checkBookingHistory(Long userId) {
        KhachHang kh = khachHangRepository.findByTaiKhoan_Id(userId);

        return ApiResponse.success(
                "Danh sách booking",
                bookingMapper.toResponseList(
                        datPhongRepository.findByKhachHang_Id(kh.getId())
                )
        );
    }
    public ApiResponse<ChiTietDatPhongResponse> getBookingDetail(Long userId, Long bookingId) {
        KhachHang kh = khachHangRepository.findByTaiKhoan_Id(userId);

        ChiTietDatPhong booking = datPhongRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));
        if (!booking.getKhachHang().getId().equals(kh.getId())) {
            throw new RuntimeException("Không có quyền truy cập booking này");
        }
        return ApiResponse.success(
                "Chi tiết booking",
                bookingMapper.toResponse(booking)
        );
    }

    public ApiResponse<?> getAllBookingsForNhanVien(Long userId) {
        Long khachSanId = nhanVienRepository
                .findByTaiKhoan_Id(userId)
                .getKhachSan()
                .getId();

        return ApiResponse.success(
                "Danh sách booking",
                bookingMapper.toResponseList(
                        datPhongRepository.findByKhachSan(khachSanId)
                )
        );
    }

    public ApiResponse<?> getDetailBookingForNhanVien(Long userId, Long bookingId) {
        Long khachSanId = nhanVienRepository
                .findByTaiKhoan_Id(userId)
                .getKhachSan()
                .getId();

        ChiTietDatPhong booking = datPhongRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));
        if (!booking.getPhong().getKhachSan().getId().equals(khachSanId)) {
            throw new RuntimeException("Không có quyền truy cập booking này");
        }
        return ApiResponse.success(
                "Chi tiết booking",
                bookingMapper.toResponse(booking)
        );
    }

    @Transactional
    public void checkIn(Long bookingId) {
        ChiTietDatPhong b = datPhongRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));

        if (b.getTrangThai() != TrangThaiDatPhong.CHO_XAC_NHAN) {
            throw new RuntimeException("Booking không ở trạng thái chờ xác nhận");
        }

        if( b.getKhachHang().getCCCD() == null || b.getKhachHang().getCCCD().isEmpty()) {
            throw new RuntimeException("Khách hàng chưa cập nhật số CCCD/CMND");
        }

        // 1. Cập nhật trạng thái booking và phòng
        b.setTrangThai(TrangThaiDatPhong.DA_XAC_NHAN);
        b.getPhong().setTrangThaiPhong(TrangThaiPhong.DA_DAT);

        phongRepository.save(b.getPhong());
        datPhongRepository.save(b);

        // 2. Render hợp đồng thuê (PDF)
        byte[] hopDongPdf = documentService.generateHopDongPdf(b);

        // 3. Sinh QR khóa phòng
        QrKhoaPhong qr = qrService.generateQrForBooking(b);

        // 4. Gửi email cho khách
        emailService.sendCheckInEmail(
                b.getKhachHang().getTaiKhoan().getEmail(),
                hopDongPdf,
                qr
        );

    }

    @Transactional
    public void checkOut(Long bookingId) {
        ChiTietDatPhong b = datPhongRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));
        if (b.getTrangThai() != TrangThaiDatPhong.CHO_TRA) {
            throw new RuntimeException("Booking chưa có yêu cầu trả phòng");
        }

        b.getQrs().forEach(qrService::invalidateQr);
        b.getPhong().setTrangThaiPhong(TrangThaiPhong.TRONG);
        b.setTrangThai(TrangThaiDatPhong.DA_TRA_PHONG);

        phienLuuTruService.kiemTraDongPhien(b.getPhienLuuTru());

        phongRepository.save(b.getPhong());
        datPhongRepository.save(b);
    }

    @Transactional
    public void huyBooking(Long bookingId) {
        ChiTietDatPhong b = datPhongRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));
        if (b.getTrangThai() != TrangThaiDatPhong.CHO_HUY) {
            throw new RuntimeException("Booking không ở trạng thái chờ hủy");
        }

        b.getQrs().forEach(qrService::invalidateQr);

        b.getPhong().setTrangThaiPhong(TrangThaiPhong.TRONG);
        b.setTrangThai(TrangThaiDatPhong.DA_HUY);

        phienLuuTruService.kiemTraDongPhien(b.getPhienLuuTru());

        phongRepository.save(b.getPhong());
        datPhongRepository.save(b);
    }

}
