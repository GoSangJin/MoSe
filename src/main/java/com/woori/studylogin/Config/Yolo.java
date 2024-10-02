package com.woori.studylogin.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//4. 자원과 외부폴더를 연결 설정
//사용자 클래스를 Bean에 등록
//WebMvcConfigurer인터페이스 상속받아서 내용을 채운다.
@Configuration
public class Yolo implements WebMvcConfigurer {
    //application에 사용자 변수 읽어온다.
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public  void  addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath); //자원위치 = 물리적위치
    } //images/item, images/member
}