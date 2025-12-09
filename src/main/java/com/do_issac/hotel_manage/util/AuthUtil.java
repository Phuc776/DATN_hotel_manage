package com.do_issac.hotel_manage.util;

import com.do_issac.hotel_manage.model.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public Long getCurrentUserId() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}

