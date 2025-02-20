package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.KakaoApproveResponse;
import com.green.acamatch.academyCost.model.KakaoPayPostReq;
import com.green.acamatch.academyCost.model.KakaoReadyResponse;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academyCost.AcademyCost;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.joinClass.model.JoinClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
    private final AcademyCostRepository academyCostRepository;
    private final JoinClassRepository joinClassRepository;
    private final KakaoPayProperties payProperties;
    private RestTemplate restTemplate = new RestTemplate();


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + payProperties.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    /**
     * 결제 완료 요청
     */
    public KakaoReadyResponse kakaoPayReady(KakaoPayPostReq req) {
        AcademyCost academyCost = new AcademyCost();
        academyCost.setAmount(req.getQuantity());
        academyCost.setUserId(req.getUserId());
        academyCost.setPrice(req.getTotalPrice() + (req.getTotalPrice()/10));
        academyCost.setStatus(0);
        academyCost.setOrderType(0);
        academyCost.setSettlementPrice(0);
        academyCost.setCost_status(0);
        if (req.getJoinClassId() != 0) {
            JoinClass joinClass = joinClassRepository.findById(req.getJoinClassId()).orElse(null);
            academyCost.setJoinClass(joinClass);
        }

        academyCostRepository.save(academyCost);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("cid", payProperties.getCid());
        parameters.put("partner_order_id", req.getOrderId()); // 실제 주문 번호로 교체
        parameters.put("partner_user_id", req.getUserId());   // 실제 사용자 ID로 교체
        parameters.put("item_name", req.getItemName());       // 실제 상품명으로 교체
        parameters.put("quantity", req.getQuantity());                 // 수량, 숫자는 문자열로 전달
        parameters.put("total_amount", req.getTotalPrice() + (req.getTotalPrice()/10));          // 총 금액, 숫자는 문자열로 전달
        parameters.put("vat_amount", (req.getTotalPrice()/10));             // 부가세, 숫자는 문자열로 전달
        parameters.put("tax_free_amount", "0");          // 비과세 금액, 숫자는 문자열로 전달
        parameters.put("approval_url", "http://localhost:8080/success");
        parameters.put("fail_url", "http://localhost:8080/fail");
        parameters.put("cancel_url", "http://localhost:8080/cancel");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());


        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoReadyResponse kakaoReady = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);
        return kakaoReady;
    }

    /**
     * 결제 완료 승인
     */
    public KakaoApproveResponse approveResponse (String pgToken){

        // 카카오 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("tid", "a");
        parameters.put("partner_order_id", "ORDER_ID");
        parameters.put("partner_user_id", "USER_ID");
        parameters.put("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        System.out.println();
        System.out.println();
        System.out.println(requestEntity);
        System.out.println();
        System.out.println();

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(approveResponse);
        System.out.println();
        System.out.println();
        System.out.println();
        return approveResponse;
    }
}