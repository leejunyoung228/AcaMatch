package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.model.JW.BusinessApiNumber;
import com.nimbusds.jose.shaded.gson.Gson;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class BusinessNumberValidation {
    // 사업자등록번호 조회 (체크)
    public static BusinessApiNumber isBusinessNumberValid(String businessNumber) {
        try {
            String serviceKey = "1ZvSanxr1rr7ubUcTeVhRMlD%2FrX%2BoZeyKWA%2Bdr%2FiL632Klj7LWL2Wej1Gq1eoq44WOHxMVv%2Bcj680T4TMoLYhg%3D%3D";
            // API URL
            String apiUrl = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + serviceKey;

            // POST 요청에 사용할 JSON 본문
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("serviceKey", serviceKey);  // 서비스 키
            jsonObject.put("b_no", new String[]{businessNumber});  // 사업자 등록 번호 (배열로 전달)

            // URL 생성
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");  // 요청 헤더에 Content-Type 설정
            connection.setDoOutput(true);  // 요청 본문 전송 허용

            // 요청 본문 작성
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(jsonObject.toString());  // JSON 객체를 문자열로 변환하여 전송
            writer.flush();
            writer.close();

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {  // 200 OK 응답 확인
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // JSON 응답 파싱
                JSONObject responseJson = new JSONObject(response.toString());
                // 응답에서 data 배열을 가져와 첫 번째 요소를 확인
                String taxType = responseJson.getJSONArray("data").getJSONObject(0).getString("tax_type");

                // 결과 객체 반환
                BusinessApiNumber businessApiNumber = new BusinessApiNumber();
                if ("국세청에 등록되지 않은 사업자등록번호입니다.".equals(taxType)) {
                    businessApiNumber.setBusinessNumCheck(false);  // 유효하지 않음
                } else {
                    businessApiNumber.setBusinessNumCheck(true);  // 유효함
                }
                return businessApiNumber;
            } else {
                System.out.println("API 호출 실패. 응답 코드: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

