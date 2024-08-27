package com.woori.studylogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyloginApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyloginApplication.class, args);
    }

}
