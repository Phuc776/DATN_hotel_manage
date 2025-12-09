package com.do_issac.hotel_manage.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final Long taiKhoanId;
    private final String noiDung;
    private final Long actorId;
    public NotificationEvent(Object source, Long taiKhoanId, String noiDung, Long actorId) {
        super(source);
        this.taiKhoanId = taiKhoanId;
        this.noiDung = noiDung;
        this.actorId = actorId;
    }
}
