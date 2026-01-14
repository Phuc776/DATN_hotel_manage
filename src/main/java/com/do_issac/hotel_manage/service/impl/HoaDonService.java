package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.HoaDonDetailResponse;
import com.do_issac.hotel_manage.entity.*;
import com.do_issac.hotel_manage.mapper.HoaDonMapper;
import com.do_issac.hotel_manage.repository.ChiTietHoaDonRepository;
import com.do_issac.hotel_manage.repository.HoaDonRepository;
import com.do_issac.hotel_manage.repository.NhanVienRepository;
import com.do_issac.hotel_manage.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.do_issac.hotel_manage.entity.LoaiChiTietHoaDon.DICH_VU;
import static com.do_issac.hotel_manage.entity.LoaiChiTietHoaDon.TIEN_PHONG;

@Service
@RequiredArgsConstructor
public class HoaDonService {
    private static final DateTimeFormatter DATETIME_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final NhanVienRepository nhanVienRepo;

    private final HoaDonRepository hoaDonRepo;
    private final ChiTietHoaDonRepository chiTietHoaDonRepo;
    private final HoaDonMapper hoaDonMapper;
    private final HoaDonPdfService hoaDonPdfService;

    @Transactional
    public void taoHoaDonChoPhien(PhienLuuTru phien) {

        if (phien.getHoaDon() != null)
            return;

        HoaDon hd = new HoaDon();
        hd.setNgayTao(LocalDateTime.now());
        hd.setPhienLuuTru(phien);

        List<ChiTietHoaDon> details = new ArrayList<>();
        double tong = 0;

        // TIỀN PHÒNG
        for (ChiTietDatPhong dp : phien.getDatPhongs()) {
            if (dp.getTrangThai() == TrangThaiDatPhong.DA_HUY) continue;

            long soDem = tinhSoDem(dp.getNgayNhan(), dp.getNgayTra());
            if (soDem <= 0) continue;

            double giaMotDem = dp.getPhong()
                    .getLoaiPhong()
                    .getGia();

            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setHoaDon(hd);
            ct.setLoai(TIEN_PHONG);
            ct.setMoTa(
                    "Phòng " + dp.getPhong().getSoPhong()
                            + " (" + dp.getPhong().getLoaiPhong().getTenLoaiPhong() + ")"
                            + ", từ " + dp.getNgayNhan().format(DATETIME_FMT)
                            + " - đến " + dp.getNgayTra().format(DATETIME_FMT)
                            + " (" + soDem + " đêm)"
            );

            ct.setDonGia(giaMotDem);
            ct.setSoLuong((int) soDem);

            tong += giaMotDem * soDem;
            details.add(ct);
        }

        // DỊCH VỤ
        for (SuDungDichVu sd : phien.getSuDungDichVus()) {
            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setHoaDon(hd);
            ct.setLoai(DICH_VU);
            ct.setMoTa(sd.getDichVu().getTenDichVu());
            ct.setDonGia(sd.getDonGiaTaiThoiDiem());
            ct.setSoLuong(sd.getSoLuong());

            tong += ct.getDonGia() * ct.getSoLuong();
            details.add(ct);
        }

        hd.setTongTien(tong);
        hd.setNoiDung("Hóa đơn lưu trú");

        hoaDonRepo.save(hd);
        chiTietHoaDonRepo.saveAll(details);

    }

    // ===== KHÁCH HÀNG =====

    public ApiResponse<?> lichSuHoaDonCuaKhach(Long taiKhoanId) {
        return ApiResponse.success("Lịch sử hóa đơn của khách hàng",
                hoaDonMapper.toResponseList(hoaDonRepo.findAllByPhienLuuTru_KhachHang_TaiKhoan_Id(taiKhoanId) )
        );
    }

    public ApiResponse<?>  xemChiTietHoaDonCuaKhach(Long hoaDonId, Long taiKhoanId) {
        HoaDon hd = layHoaDonHopLeChoKhach(hoaDonId, taiKhoanId);
        return ApiResponse.success("Chi tiết hóa đơn", hoaDonMapper.toDetailResponse(hd));
    }

    @Transactional(readOnly = true)
    public byte[] taiHoaDonPdfChoKhach(Long hoaDonId, Long taiKhoanId) {
        HoaDon hd = layHoaDonHopLeChoKhach(hoaDonId, taiKhoanId);
        HoaDonDetailResponse res =
                hoaDonMapper.toDetailResponse(hd);
        return hoaDonPdfService.generatePdf(res);
    }

    // ===== NHÂN VIÊN =====

    public ApiResponse<?> xemDanhSachHoaDonCuaKhachSanNhanVien(Long taiKhoanId) {
        NhanVien nv = nhanVienRepo.findByTaiKhoan_Id(taiKhoanId);
        Long khachSanId = nv.getKhachSan().getId();
        return ApiResponse.success(
                "Danh sách hóa đơn khách sạn",
                hoaDonMapper.toResponseList(
                        hoaDonRepo.findAllByKhachSanId(khachSanId)
                )
        );
    }

    public ApiResponse<?> xemHoaDonNhanVien(Long hoaDonId, Long taiKhoanId) {
        HoaDon hd = layHoaDonHopLeChoNhanVien(hoaDonId, taiKhoanId);
        return ApiResponse.success(
                "Chi tiết hóa đơn",
                hoaDonMapper.toDetailResponse(hd)
        );
    }

    @Transactional(readOnly = true)
    public byte[] taiHoaDonPdfChoNhanVien(Long hoaDonId, Long taiKhoanId) {

        HoaDon hd = layHoaDonHopLeChoNhanVien(hoaDonId, taiKhoanId);

        HoaDonDetailResponse res =
                hoaDonMapper.toDetailResponse(hd);

        return hoaDonPdfService.generatePdf(res);
    }



    // ===== CHỦ KHÁCH SẠN =====

    public ApiResponse<?> xemDanhSachHoaDonChuKhachSan(Long chuKsId) {
        return ApiResponse.success(
                "Danh sách hóa đơn các khách sạn",
                hoaDonMapper.toResponseList(
                        hoaDonRepo.findAllByChuKhachSan_Id(chuKsId)
                )
        );
    }

    public ApiResponse<?> xemHoaDonChuKhachSan(Long hoaDonId, Long chuKsId) {
        HoaDon hd = layHoaDonHopLeChoChuKhachSan(hoaDonId, chuKsId);
        return ApiResponse.success(
                "Chi tiết hóa đơn",
                hoaDonMapper.toDetailResponse(hd)
        );
    }
    @Transactional(readOnly = true)
    public byte[] taiHoaDonPdfChoChuKhachSan(Long hoaDonId, Long chuKsId) {
        HoaDon hd = layHoaDonHopLeChoChuKhachSan(hoaDonId, chuKsId);

        HoaDonDetailResponse res =
                hoaDonMapper.toDetailResponse(hd);

        return hoaDonPdfService.generatePdf(res);
    }


    // ===== PRIVATE CHECK =====

    private long tinhSoDem(LocalDateTime ngayNhan, LocalDateTime ngayTra) {
        if (ngayNhan == null || ngayTra == null) return 0;

        LocalDate inDate = ngayNhan.toLocalDate();
        LocalDate outDate = ngayTra.toLocalDate();

        long soDem = ChronoUnit.DAYS.between(inDate, outDate);
        return Math.max(soDem, 0);
    }

    private HoaDon layHoaDonHopLeChoKhach(Long hoaDonId, Long taiKhoanId) {
        HoaDon hd = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        if (!hd.getPhienLuuTru()
                .getKhachHang()
                .getTaiKhoan()
                .getId()
                .equals(taiKhoanId)) {
            throw new RuntimeException("Không có quyền xem hóa đơn");
        }
        return hd;
    }

    private HoaDon layHoaDonHopLeChoNhanVien(Long hoaDonId, Long taiKhoanId) {
        HoaDon hd = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        boolean hopLe = hd.getPhienLuuTru()
                .getDatPhongs()
                .stream()
                .anyMatch(dp ->
                        dp.getPhong()
                                .getKhachSan()
                                .getNhanViens()
                                .stream()
                                .anyMatch(nv -> nv.getTaiKhoan().getId().equals(taiKhoanId))
                );

        if (!hopLe)
            throw new RuntimeException("Không có quyền xem hóa đơn");

        return hd;
    }

    private HoaDon layHoaDonHopLeChoChuKhachSan(Long hoaDonId, Long chuKsId) {
        HoaDon hd = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        boolean hopLe = hd.getPhienLuuTru()
                .getDatPhongs()
                .stream()
                .anyMatch(dp ->
                        dp.getPhong()
                                .getKhachSan()
                                .getChuKhachSan()
                                .getId()
                                .equals(chuKsId)
                );

        if (!hopLe)
            throw new RuntimeException("Không có quyền xem hóa đơn");

        return hd;
    }
}
