package com.woori.studylogin.Config;

import com.woori.studylogin.Util.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DateTimeUtil dateTimeUtil() {
        return new DateTimeUtil();
    }
}
//라이브러리에 사용하기 편하게 사용자 메소드를 등록(생략가능)