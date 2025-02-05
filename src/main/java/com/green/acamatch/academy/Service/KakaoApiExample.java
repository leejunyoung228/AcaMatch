package com.green.acamatch.academy.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.green.acamatch.academy.model.AddressDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class KakaoApiExample {
    public static String addressSearchMain(AddressDto dto) {
        try {
            // 요청 주소
            String address = dto.getAddress();
            if (address == null || address.trim().isEmpty()) {
                return "입력 주소가 비어 있습니다.";
            }

            String query = URLEncoder.encode(address.trim(), "UTF-8");
            String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + query;

            // HTTP 요청 설정
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "KakaoAK dabb297e430234c97c659e4b1fc21228");

            // 응답 처리
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray documents = jsonResponse.getJSONArray("documents");

                if (documents.length() > 0) {
                    JSONObject document = documents.getJSONObject(0);

                    // 지번 주소의 동 정보 추출
                    String region3depthName = document.getJSONObject("address").getString("region_3depth_name");
                    return region3depthName;
                } else {
                    return "검색 결과가 없습니다.";
                }
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                String inputLine;
                StringBuilder errorResponse = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                in.close();
                return "API 요청 실패. 응답 코드: " + responseCode + ", 오류 메시지: " + errorResponse.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "예외 발생: " + e.getMessage();
        }
    }
}
