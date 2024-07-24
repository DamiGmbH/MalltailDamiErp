package com.malltail.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MessageDto {

    private String message;  //알림 메세지
    private String redirectUri; // url
    private RequestMethod method; // http 요청 메서드
    private Map<String, Object> data; // 화면으로 전달할 param
}
