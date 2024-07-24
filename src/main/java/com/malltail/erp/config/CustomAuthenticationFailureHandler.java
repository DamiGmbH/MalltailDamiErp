package com.malltail.erp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException{

        String errorMessage = "";

        if(exception instanceof BadCredentialsException){
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다.";
        }else if(exception instanceof InsufficientAuthenticationException){
            errorMessage = "내부시스템 문제로 로그인 요청을 할 수 없습니다.";
        }else if(exception instanceof UsernameNotFoundException){
            errorMessage = "존재하지 않는 계정입니다.";
        }else if(exception instanceof AuthenticationCredentialsNotFoundException){
            errorMessage = "인증요청이 거부되었습니다.";
        }else{
            errorMessage = "알 수 없는 오류입니다.";
        }

        errorMessage = URLEncoder.encode(errorMessage,"UTF-8");
        setDefaultFailureUrl("/login.do?error=true&exception="+errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }


}
