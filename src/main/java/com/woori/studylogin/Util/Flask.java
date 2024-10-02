package com.woori.studylogin.Util;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Flask {
    @Value("${flask.Server.Url}")
    private String url;

    @Value("${tempFolder}")
    private String tempFolder;

    private JSONObject jsonobject = new JSONObject();

    private String getBase64String(MultipartFile multipartFile) throws Exception {
        byte[] bytes = multipartFile.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public Map<String, Object> requestToFlask(MultipartFile file) throws Exception {
        // 다른 서버와 통신을 위한 RestTemplate 선언
        RestTemplate restTemplate = new RestTemplate();
        String originalFileName = file.getOriginalFilename(); // 파일명 추출
        // 확장자 추출
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 메세지 헤더부분 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 메세지 본문부분 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String imageFileString = getBase64String(file);
        body.add("extension", extension); // 확장자만 전송시
        body.add("image", imageFileString);

        // 전송할 메세지 만들기
        HttpEntity<?> requestMessage = new HttpEntity<>(body, httpHeaders);

        // 서버에 요청하기
        String response = restTemplate.postForObject(url, requestMessage, String.class);

        // 플라스크로 부터 받은 JSON값을 분리
        JSONParser parser = new JSONParser();
        JSONObject jsonobject = (JSONObject) parser.parse(response);

        // 플라스크에서 전달받은 파일을 임시저장
        byte[] decodedImageData = Base64.getDecoder()
                .decode((String) jsonobject.get("image"));
        String outputFilePath = tempFolder + "result.jpg"; // AI 결과 파일명

        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(decodedImageData);
        }

        // labels 및 crops 정보 수집
        Map<String, Object> result = new HashMap<>();

        // labels 추가
        if (jsonobject.containsKey("labels")) {
            JSONArray labelsArray = (JSONArray) jsonobject.get("labels");
            result.put("labels", labelsArray);
        } else {
            result.put("labels", new JSONArray());
        }

        // crops 정보 추가
        if (jsonobject.containsKey("crop_folders")) {
            JSONObject cropInfo = (JSONObject) jsonobject.get("crop_folders");
            result.put("crops", cropInfo);
        } else {
            result.put("crops", new JSONObject());
        }

        return result;
    }
}
