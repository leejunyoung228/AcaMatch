package com.green.acamatch.academy.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.green.acamatch.academy.model.AddressDto;
import com.green.acamatch.academy.model.JW.KakaoMapAddress;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
//

@Component
public class KakaoApiExample {
    //도로명 주소에서 지번 주소 추출하는 api
    public static KakaoMapAddress addressSearchMain(String addressOriginal) {
        try {
            // 요청 주소
            String address = addressOriginal;
            if (address == null || address.trim().isEmpty()) {
                return null;
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
                    String region1depthName = document.getJSONObject("address").getString("region_1depth_name");  // 시 이름
                    String region2depthName = document.getJSONObject("address").getString("region_2depth_name"); // 구 이름
                    String region3depthName = document.getJSONObject("address").getString("region_3depth_name"); // 동 이름


                    KakaoMapAddress kakaoMapAddress = new KakaoMapAddress();
                    kakaoMapAddress.setCityName(region1depthName);
                    kakaoMapAddress.setStreetName(cutAddress(region2depthName));
                    kakaoMapAddress.setDongName(cutAddress(region3depthName));
                    return kakaoMapAddress;
                } else {
                    return null;
                }
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                String inputLine;
                StringBuilder errorResponse = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                in.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //군 면 리 잘라내기
    private static String cutAddress(String address) {
        if((address.contains("시")) && ((address.contains("구") || address.contains("군")) )){
            return address.substring(0,address.indexOf("시") + 1);
        }
        if((address.contains("면") && (address.contains("리")))) {
            return address.substring(0,address.indexOf("면") + 1);
        }
        return address;
    }

    //도로명 주소에서 위도, 경도 추출하기
    public static KakaoMapAddress addressXY(String addressOriginal) {
        try {
            // 요청 주소
            String address = addressOriginal;
            if (address == null || address.trim().isEmpty()) {
                return null;
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
                    String longitudeX = document.getJSONObject("address").getString("x");  // 경도
                    Double x = Double.parseDouble(longitudeX);
                    String latitudeY = document.getJSONObject("address").getString("y");  // 위도
                    Double y = Double.parseDouble(latitudeY);



                    KakaoMapAddress kakaoMapAddress = new KakaoMapAddress();
                    kakaoMapAddress.setLongitude(x);
                    kakaoMapAddress.setLatitude(y);

                    return kakaoMapAddress;
                } else {
                    return null;
                }
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                String inputLine;
                StringBuilder errorResponse = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                in.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //사업자등록번호 조회(실제로 있는지 확인)


}


