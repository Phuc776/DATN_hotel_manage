package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.dto.response.HoaDonDetailResponse;
import com.do_issac.hotel_manage.dto.response.ThanhToanResponse;
import com.do_issac.hotel_manage.entity.*;
import com.do_issac.hotel_manage.mapper.HoaDonMapper;
import com.do_issac.hotel_manage.mapper.ThanhToanMapper;
import com.do_issac.hotel_manage.repository.HoaDonRepository;
import com.do_issac.hotel_manage.repository.PhienLuuTruRepository;
import com.do_issac.hotel_manage.repository.ThanhToanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThanhToanService {
    @Value("${vnpay.tmnCode}")
    private String VNPAY_TMNCODE;

    @Value("${vnpay.hashSecret}")
    private String VNPAY_HASH_SECRET;

    @Value("${vnpay.payUrl}")
    private String VNPAY_URL;

    @Value("${vnpay.returnUrl}")
    private String VNPAY_RETURN_URL;

    private final PhienLuuTruRepository phienRepo;
    private final ThanhToanRepository thanhToanRepo;
    private final HoaDonMapper hoaDonMapper;
    private final HoaDonPdfService hoaDonPdfService;
    private final EmailService emailService;
    private final HoaDonRepository hoaDonRepo;

    // 1. Nhân viên thanh toán thủ công
    @Transactional
    public ThanhToan thanhToanThuCongTheoHoaDon(
            Long hoaDonId,
            Long nhanVienId,
            long soTienThu
    ) {
        HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        PhienLuuTru phien = hoaDon.getPhienLuuTru();

        return thanhToanThuCongNhanVien(phien.getId(), nhanVienId, soTienThu);
    }
    @Transactional
    public ThanhToan thanhToanThuCongNhanVien(
            Long phienId,
            Long nhanVienId,
            long soTienThu
    ) {

        PhienLuuTru phien = phienRepo.findById(phienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên"));

        // kiểm tra nhân viên thuộc khách sạn
        if (phien.getDatPhongs().get(0).getPhong().getKhachSan()
                .getNhanViens()
                .stream()
                .noneMatch(nv -> nv.getTaiKhoan().getId().equals(nhanVienId))) {
            throw new RuntimeException("Nhân viên không thuộc khách sạn này");
        }

        if (phien.getTrangThai() != TrangThaiPhien.CHO_THANH_TOAN) {
            throw new RuntimeException("Phiên chưa sẵn sàng thanh toán");
        }

        long conNo = tinhConNoVnd(phien);
        if (conNo <= 0) {
            throw new RuntimeException("Phiên đã thanh toán đủ");
        }

        if (soTienThu <= 0) {
            throw new RuntimeException("Số tiền không hợp lệ");
        }

        if (soTienThu > conNo) {
            throw new RuntimeException("Số tiền vượt quá số nợ còn lại");
        }

        ThanhToan tt = new ThanhToan();
        tt.setPhienLuuTru(phien);
        tt.setSoTien(soTienThu);
        tt.setPhuongThuc("TIEN_MAT");
        tt.setTrangThai(TrangThaiThanhToan.SUCCESS);
        tt.setNgayThanhToan(LocalDateTime.now());
        tt.setMaGiaoDich("NV-" + System.currentTimeMillis());

        ThanhToan saved = thanhToanRepo.save(tt);

        hoanTatPhienVaGuiHoaDonNeuCan(phien);

        return saved;
    }

    public List<ThanhToanResponse> xemDanhSachThanhToanTheoHoaDon(
            Long hoaDonId,
            Long nhanVienId
    ) {
        HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        PhienLuuTru phien = hoaDon.getPhienLuuTru();

        return xemDanhSachThanhToanNhanVien(phien.getId(), nhanVienId);
    }

    public List<ThanhToanResponse> xemDanhSachThanhToanNhanVien(
            Long phienId,
            Long nhanVienId) {

        PhienLuuTru phien = phienRepo.findById(phienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên"));

        // kiểm tra nhân viên thuộc khách sạn của phiên
        if (phien.getDatPhongs().get(0).getPhong().getKhachSan()
                .getNhanViens()
                .stream()
                .noneMatch(nv -> nv.getTaiKhoan().getId().equals(nhanVienId))) {
            throw new RuntimeException("Không có quyền");
        }

        return thanhToanRepo
                .findByPhienLuuTru_IdOrderByNgayThanhToanAsc(phienId)
                .stream()
                .map(ThanhToanMapper::toResponse)
                .toList();
    }



    // 2. Khách tạo thanh toán VNPAY (PENDING)
    public ThanhToan taoThanhToanVnpay(Long phienLuuTruId) {

        PhienLuuTru phien = phienRepo.findById(phienLuuTruId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên lưu trú"));

        HoaDon hoaDon = phien.getHoaDon();
        if (hoaDon == null) {
            throw new RuntimeException("Phiên chưa có hóa đơn");
        }

        ThanhToan tt = new ThanhToan();
        tt.setPhienLuuTru(phien);
        tt.setSoTien(hoaDon.getTongTien());
        tt.setPhuongThuc("VNPAY");
        tt.setTrangThai(TrangThaiThanhToan.PENDING);

        // sinh mã giao dịch
        tt.setMaGiaoDich(UUID.randomUUID().toString());

        return thanhToanRepo.save(tt);
    }

    // 3. Tạo URL redirect sang VNPAY sandbox
    public String taoLinkVnpay(ThanhToan tt) {

        Map<String, String> params = new HashMap<>();

        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", VNPAY_TMNCODE);
        params.put("vnp_Amount", String.valueOf((long) (tt.getSoTien() * 100)));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", tt.getMaGiaoDich());
        params.put("vnp_OrderInfo", "Thanh toan hoa don " + tt.getId());
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", VNPAY_RETURN_URL);
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_CreateDate",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        // sort + hash (bước này để sau, mình làm tiếp nếu bạn muốn)
        String query = buildQuery(params);
        String secureHash = hmacSHA512(VNPAY_HASH_SECRET, query);

        return VNPAY_URL + "?" + query + "&vnp_SecureHash=" + secureHash;
    }


    // 4. Xử lý callback từ VNPAY
    @Transactional
    public void xuLyCallbackVnpay(Map<String, String> params) {

        if (!verifyVnpayChecksum(new HashMap<>(params))) {
            throw new RuntimeException("Sai chữ ký VNPAY");
        }

        String maGiaoDich = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");

        ThanhToan tt = thanhToanRepo
                .findByMaGiaoDich(maGiaoDich)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch"));

        if ("00".equals(responseCode)) {
            tt.setTrangThai(TrangThaiThanhToan.SUCCESS);
            tt.setNgayThanhToan(LocalDateTime.now());
        } else {
            tt.setTrangThai(TrangThaiThanhToan.FAILED);
        }

        thanhToanRepo.save(tt);
        hoanTatPhienVaGuiHoaDonNeuCan(tt.getPhienLuuTru());
    }


    @Transactional
    public void hoanTatPhienVaGuiHoaDonNeuCan(PhienLuuTru phien) {

        if (phien.getTrangThai() != TrangThaiPhien.CHO_THANH_TOAN) {
            return; // không đúng phase thì bỏ
        }

        if (!daThanhToanDu(phien)) {
            return; // còn nợ → KHÔNG gửi mail
        }

        // 1. Đóng phiên
        phien.setTrangThai(TrangThaiPhien.DONG);
        phien.setKetThuc(LocalDateTime.now());
        phienRepo.save(phien);

        // 2. Render + gửi hóa đơn
        HoaDon hoaDon = phien.getHoaDon();
        HoaDonDetailResponse res = hoaDonMapper.toDetailResponse(hoaDon);

        byte[] pdf = hoaDonPdfService.generatePdf(res);

        emailService.sendHoaDonEmail(
                phien.getKhachHang().getTaiKhoan().getEmail(),
                pdf
        );
    }


    // Hàm build query string từ params
    private String buildQuery(Map<String, String> params) {
        return params.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
    // Hàm tạo mã băm HMAC SHA512
    private String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey =
                    new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKey);

            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(2 * rawHmac.length);

            for (byte b : rawHmac) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo chữ ký VNPAY", e);
        }
    }

    // Hàm xác minh chữ ký VNPAY
    private boolean verifyVnpayChecksum(Map<String, String> params) {

        String receivedHash = params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        String data = buildQuery(params);
        String calculatedHash = hmacSHA512(VNPAY_HASH_SECRET, data);

        return calculatedHash.equalsIgnoreCase(receivedHash);
    }

    private long tinhDaThanhToanVnd(PhienLuuTru phien) {
        return thanhToanRepo.tongTienDaThanhToan(phien.getId());
    }
    private long tinhConNoVnd(PhienLuuTru phien) {
        long tongTien = Math.round(phien.getHoaDon().getTongTien());
        return tongTien - tinhDaThanhToanVnd(phien);
    }
    private boolean daThanhToanDu(PhienLuuTru phien) {
        return tinhConNoVnd(phien) == 0;
    }

}

