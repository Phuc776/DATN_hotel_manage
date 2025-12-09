package com.do_issac.hotel_manage.util;

import com.do_issac.hotel_manage.entity.TaiKhoan;
import com.do_issac.hotel_manage.entity.ThongBao;
import com.do_issac.hotel_manage.model.NotificationEvent;
import com.do_issac.hotel_manage.repository.TaiKhoanRepository;
import com.do_issac.hotel_manage.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ThongBaoRepository thongBaoRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    @EventListener
    public void handleNotification(NotificationEvent event) {

        TaiKhoan tk = taiKhoanRepository.findById(event.getTaiKhoanId())
                .orElse(null);

        if (tk == null) return;

        ThongBao tb = new ThongBao();
        tb.setNoiDung(event.getNoiDung());
        tb.setNgayTao(LocalDateTime.now());
        tb.setTaiKhoan(tk);
        tb.setActorId(event.getActorId());

        thongBaoRepository.save(tb);
    }
}

