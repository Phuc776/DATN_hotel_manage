package com.do_issac.hotel_manage.service.impl;

import com.do_issac.hotel_manage.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ApplicationEventPublisher eventPublisher;

    public void push(Long taiKhoanId, String noiDung, Long actorId) {
        eventPublisher.publishEvent(
                new NotificationEvent(this, taiKhoanId, noiDung, actorId));
    }
}
