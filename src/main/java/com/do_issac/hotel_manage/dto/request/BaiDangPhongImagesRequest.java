package com.do_issac.hotel_manage.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BaiDangPhongImagesRequest {
    private List<String> imageUrls;
}
